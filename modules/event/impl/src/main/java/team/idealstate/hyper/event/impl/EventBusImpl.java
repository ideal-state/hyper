/*
 *    hyper-event-impl
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

package team.idealstate.hyper.event.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.annotation.reflect.ReflectAnnotatedUtils;
import team.idealstate.hyper.core.common.sort.OrderComparator;
import team.idealstate.hyper.core.common.sort.Orderable;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.SetUtils;
import team.idealstate.hyper.event.api.Cancellable;
import team.idealstate.hyper.event.api.EventBus;
import team.idealstate.hyper.event.api.EventListener;
import team.idealstate.hyper.event.api.annotation.Asynchronous;

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

    private static final OrderComparator<EventListener<?>> EVENT_LISTENER_COMPARATOR = OrderComparator.getDefault();

    private final Set<UUID> eventListenerUids = SetUtils.concurrentSetOf();
    private final List<UIDEventListener<?>> eventListeners = ListUtils.concurrentListOf();
    private final ExecutorService executorService;
    private final Lock subscribeLock = new ReentrantLock();

    public EventBusImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void syncPublish(Object event) {
        Class<?> eventType = event.getClass();
        List<UIDEventListener<?>> listeners = eventListeners.stream()
                .filter((uid) -> uid.getEventType().isAssignableFrom(eventType))
                .sorted(EVENT_LISTENER_COMPARATOR)
                .collect(Collectors.toCollection(ListUtils::linkedListOf));
        if (CollectionUtils.isNullOrEmpty(listeners)) {
            return;
        }
        EventListener lastListener = null;
        try {
            if (event instanceof Cancellable) {
                Cancellable cancellable = (Cancellable) event;
                for (EventListener listener : listeners) {
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                    lastListener = listener;
                    listener.on(event);
                }
            } else {
                for (EventListener listener : listeners) {
                    lastListener = listener;
                    listener.on(event);
                }
            }
        } catch (Throwable e) {
            if (lastListener != null) {
                try {
                    lastListener.exceptionCaught(e);
                } catch (Throwable ex) {
                    logger.catching(ex);
                }
            } else {
                logger.catching(e);
            }
        }
    }

    private void asyncPublish(Object event) {
        if (ObjectUtils.isNull(executorService)) {
            throw new UnsupportedOperationException("该事件总线无法处理异步事件。");
        }
        executorService.submit(() -> syncPublish(event));
    }

    @Override
    public void publish(@NotNull Object event) {
        AssertUtils.notNull(event, "无效的事件");
        if (CollectionUtils.isNullOrEmpty(eventListeners)) {
            return;
        }
        Class<?> eventType = event.getClass();
        boolean isAsync = ReflectAnnotatedUtils.of(eventType).hasAnnotation(Asynchronous.class);
        if (isAsync) {
            asyncPublish(event);
        } else {
            syncPublish(event);
        }
    }

    @Override
    @NotNull
    public <EVENT> EventListener.UID<EVENT> subscribe(@NotNull EventListener<EVENT> eventListener) {
        AssertUtils.notNull(eventListener, "无效的事件监听器");
        try {
            if (subscribeLock.tryLock(3L, TimeUnit.SECONDS)) {
                try {
                    UUID uuid = UUID.randomUUID();
                    while (eventListenerUids.contains(uuid)) {
                        uuid = UUID.randomUUID();
                    }
                    UIDEventListener<EVENT> uidEventListener = new UIDEventListener<>(uuid, eventListener);
                    eventListenerUids.add(uuid);
                    eventListeners.add(uidEventListener);
                    return uidEventListener;
                } finally {
                    subscribeLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new IllegalStateException("处理监听器订阅超时。");
    }

    @Override
    @Nullable
    public <EVENT> EventListener<EVENT> unsubscribe(@NotNull EventListener.UID<EVENT> eventListenerUid) {
        AssertUtils.notNull(eventListenerUid, "无效的事件监听器唯一序号");
        if (eventListenerUid instanceof UIDEventListener) {
            UIDEventListener<EVENT> uidEventListener = (UIDEventListener<EVENT>) eventListenerUid;
            UUID uuid = uidEventListener.getUuid();
            if (eventListenerUids.contains(uuid)) {
                eventListeners.remove(uidEventListener);
                eventListenerUids.remove(uuid);
                return uidEventListener.getEventListener();
            }
        }
        return null;
    }

    private static final class UIDEventListener<EVENT> implements EventListener.UID<EVENT>, EventListener<EVENT>, Orderable {

        private final UUID uuid;
        private final EventListener<EVENT> eventListener;
        private final Class<EVENT> eventType;
        private final int order;

        @SuppressWarnings({"unchecked"})
        private UIDEventListener(@NotNull UUID uuid, @NotNull EventListener<EVENT> eventListener) {
            AssertUtils.notNull(uuid, "无效的事件监听器唯一序号");
            AssertUtils.notNull(eventListener, "无效的事件监听器");
            for (Method method : eventListener.getClass().getMethods()) {
                if ("on".equals(method.getName())) {
                    if (method.isBridge()) {
                        continue;
                    }
                    if (method.getParameterCount() == 1) {
                        this.uuid = uuid;
                        this.eventListener = eventListener;
                        this.eventType = (Class<EVENT>) method.getParameterTypes()[0];
                        this.order = EVENT_LISTENER_COMPARATOR.orderOf(eventListener);
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
        public EventListener<EVENT> getEventListener() {
            return eventListener;
        }

        @Override
        public int order() {
            EventListener<EVENT> listener = getEventListener();
            if (listener instanceof Orderable) {
                return ((Orderable) listener).order();
            }
            return order;
        }

        @Override
        public void on(@NotNull EVENT event) throws Throwable {
            getEventListener().on(event);
        }

        @Override
        public void exceptionCaught(@NotNull Throwable throwable) throws Throwable {
            getEventListener().exceptionCaught(throwable);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof UIDEventListener)) {
                return false;
            }
            final UIDEventListener<?> that = (UIDEventListener<?>) o;

            if (order != that.order) {
                return false;
            }
            if (!uuid.equals(that.uuid)) {
                return false;
            }
            if (!eventListener.equals(that.eventListener)) {
                return false;
            }
            return Objects.equals(eventType, that.eventType);
        }

        @Override
        public int hashCode() {
            int result = uuid.hashCode();
            result = 31 * result + eventListener.hashCode();
            result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
            result = 31 * result + order;
            return result;
        }
    }
}
