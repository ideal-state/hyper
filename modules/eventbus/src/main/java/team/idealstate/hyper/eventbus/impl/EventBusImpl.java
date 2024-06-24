/*
 *    hyper-eventbus
 *    Copyright [2024] [ideal-state]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package team.idealstate.hyper.eventbus.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.reflect.annotation.reflect.ReflectAnnotatedUtils;
import team.idealstate.hyper.common.sort.OrderComparator;
import team.idealstate.hyper.common.sort.Orderable;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.SetUtils;
import team.idealstate.hyper.eventbus.api.Cancellable;
import team.idealstate.hyper.eventbus.api.EventBus;
import team.idealstate.hyper.eventbus.api.EventSubscriber;
import team.idealstate.hyper.eventbus.api.annotation.Asynchronous;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * <p>EventBusImpl</p>
 *
 * <p>创建于 2024/3/30 9:03</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class EventBusImpl implements EventBus {

    private static final Logger logger = LogManager.getLogger(EventBusImpl.class);

    private static final OrderComparator<EventSubscriber<?>> EVENT_SUBSCRIBER_COMPARATOR = OrderComparator.getDefault();

    private final Set<UUID> eventSubscriberUids = SetUtils.concurrentSetOf();
    private final List<UIDEventSubscriber<?>> eventSubscribers = ListUtils.concurrentListOf();
    private final ExecutorService executorService;
    private final Lock registerLock = new ReentrantLock();

    public EventBusImpl(@Nullable ExecutorService executorService) {
        this.executorService = executorService;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void syncPost(Object event) {
        Class<?> eventType = event.getClass();
        List<UIDEventSubscriber<?>> subscribers = eventSubscribers.stream()
                .filter((uid) -> uid.getEventType().isAssignableFrom(eventType))
                .sorted(EVENT_SUBSCRIBER_COMPARATOR)
                .collect(Collectors.toCollection(ListUtils::linkedListOf));
        if (CollectionUtils.isNullOrEmpty(subscribers)) {
            return;
        }
        EventSubscriber lastSubscriber = null;
        try {
            if (event instanceof Cancellable) {
                Cancellable cancellable = (Cancellable) event;
                for (EventSubscriber subscriber : subscribers) {
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                    lastSubscriber = subscriber;
                    subscriber.onEvent(event);
                }
            } else {
                for (EventSubscriber subscriber : subscribers) {
                    lastSubscriber = subscriber;
                    subscriber.onEvent(event);
                }
            }
        } catch (Throwable e) {
            if (lastSubscriber != null) {
                try {
                    lastSubscriber.exceptionCaught(e);
                } catch (Throwable ex) {
                    logger.catching(ex);
                }
            } else {
                logger.catching(e);
            }
        }
    }

    private void asyncPost(Object event) {
        if (ObjectUtils.isNull(executorService)) {
            throw new UnsupportedOperationException("该事件总线无法处理异步事件。");
        }
        executorService.submit(() -> syncPost(event));
    }

    @Override
    public void post(@NotNull Object event) {
        AssertUtils.notNull(event, "无效的事件");
        if (CollectionUtils.isNullOrEmpty(eventSubscribers)) {
            return;
        }
        Class<?> eventType = event.getClass();
        boolean isAsync = ReflectAnnotatedUtils.of(eventType).hasAnnotation(Asynchronous.class);
        if (isAsync) {
            asyncPost(event);
        } else {
            syncPost(event);
        }
    }

    @Override
    @NotNull
    public <EVENT> EventSubscriber.UID<EVENT> register(@NotNull EventSubscriber<EVENT> eventSubscriber) {
        AssertUtils.notNull(eventSubscriber, "无效的事件订阅者");
        try {
            if (registerLock.tryLock(3L, TimeUnit.SECONDS)) {
                try {
                    UUID uuid = UUID.randomUUID();
                    while (eventSubscriberUids.contains(uuid)) {
                        uuid = UUID.randomUUID();
                    }
                    UIDEventSubscriber<EVENT> uidEventSubscriber = new UIDEventSubscriber<>(uuid, eventSubscriber);
                    eventSubscriberUids.add(uuid);
                    eventSubscribers.add(uidEventSubscriber);
                    return uidEventSubscriber;
                } finally {
                    registerLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException("处理订阅者订阅超时。");
    }

    @Override
    @Nullable
    public <EVENT> EventSubscriber<EVENT> unregister(@NotNull EventSubscriber.UID<EVENT> eventSubscriberUid) {
        AssertUtils.notNull(eventSubscriberUid, "无效的事件订阅者唯一序号");
        if (eventSubscriberUid instanceof UIDEventSubscriber) {
            UIDEventSubscriber<EVENT> uidEventSubscriber = (UIDEventSubscriber<EVENT>) eventSubscriberUid;
            UUID uuid = uidEventSubscriber.getUuid();
            if (eventSubscriberUids.contains(uuid)) {
                eventSubscribers.remove(uidEventSubscriber);
                eventSubscriberUids.remove(uuid);
                return uidEventSubscriber.getEventSubscriber();
            }
        }
        return null;
    }

    private static final class UIDEventSubscriber<EVENT> implements EventSubscriber.UID<EVENT>, EventSubscriber<EVENT>, Orderable {

        private final UUID uuid;
        private final EventSubscriber<EVENT> eventSubscriber;
        private final Class<EVENT> eventType;
        private final int order;

        @SuppressWarnings({"unchecked"})
        private UIDEventSubscriber(@NotNull UUID uuid, @NotNull EventSubscriber<EVENT> eventSubscriber) {
            AssertUtils.notNull(uuid, "无效的事件订阅者唯一序号");
            AssertUtils.notNull(eventSubscriber, "无效的事件订阅者");
            for (Method method : eventSubscriber.getClass().getMethods()) {
                if ("on".equals(method.getName())) {
                    if (method.isBridge()) {
                        continue;
                    }
                    if (method.getParameterCount() == 1) {
                        this.uuid = uuid;
                        this.eventSubscriber = eventSubscriber;
                        this.eventType = (Class<EVENT>) method.getParameterTypes()[0];
                        this.order = EVENT_SUBSCRIBER_COMPARATOR.orderOf(eventSubscriber);
                        return;
                    }
                }
            }
            throw new IllegalStateException();
        }

        public UUID getUuid() {
            return uuid;
        }

        @NotNull
        @Override
        public Class<EVENT> getEventType() {
            return eventType;
        }

        @NotNull
        @Override
        public EventSubscriber<EVENT> getEventSubscriber() {
            return eventSubscriber;
        }

        @Override
        public int order() {
            EventSubscriber<EVENT> subscriber = getEventSubscriber();
            if (subscriber instanceof Orderable) {
                return ((Orderable) subscriber).order();
            }
            return order;
        }

        @Override
        public void onEvent(@NotNull EVENT event) throws Throwable {
            getEventSubscriber().onEvent(event);
        }

        @Override
        public void exceptionCaught(@NotNull Throwable throwable) throws Throwable {
            getEventSubscriber().exceptionCaught(throwable);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof UIDEventSubscriber)) {
                return false;
            }
            final UIDEventSubscriber<?> that = (UIDEventSubscriber<?>) o;

            if (order != that.order) {
                return false;
            }
            if (!uuid.equals(that.uuid)) {
                return false;
            }
            if (!eventSubscriber.equals(that.eventSubscriber)) {
                return false;
            }
            return Objects.equals(eventType, that.eventType);
        }

        @Override
        public int hashCode() {
            int result = uuid.hashCode();
            result = 31 * result + eventSubscriber.hashCode();
            result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
            result = 31 * result + order;
            return result;
        }
    }
}
