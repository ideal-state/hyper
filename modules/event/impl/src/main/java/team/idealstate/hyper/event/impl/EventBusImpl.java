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

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.annotation.Annotated;
import team.idealstate.hyper.core.common.reflect.annotation.reflect.ReflectAnnotatedUtils;
import team.idealstate.hyper.core.common.reflect.type.TypeReference;
import team.idealstate.hyper.core.common.sort.OrderComparator;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.MapUtils;
import team.idealstate.hyper.event.api.AsynchronousEventBus;
import team.idealstate.hyper.event.api.Cancellable;
import team.idealstate.hyper.event.api.EventListener;
import team.idealstate.hyper.event.api.EventListenerUid;
import team.idealstate.hyper.event.api.annotation.Asynchronous;
import team.idealstate.hyper.event.api.annotation.EventType;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public final class EventBusImpl implements AsynchronousEventBus {

    private static final Logger logger = Logger.getLogger(EventBusImpl.class.getName());
    private final Map<TypeReference<?>, Map<EventListenerUid, EventListener<?>>> eventListeners =
            MapUtils.concurrentMapOf();
    private final Map<Class<?>, EventDetail> eventDetails =
            MapUtils.concurrentMapOf();
    private final ExecutorService executorService;
    private final Lock subscribeLock = new ReentrantLock();

    public EventBusImpl(@NotNull AsynchronousPublisherProvider asynchronousPublisherProvider) {
        this.executorService = new AsynchronousPublisher(asynchronousPublisherProvider);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void publishEvent(Object event, Collection<? extends EventListener> listeners) {
        if (CollectionUtils.isNullOrEmpty(listeners)) {
            return;
        }
        listeners = ListUtils.listOf(listeners);
        if (CollectionUtils.isEmpty(listeners)) {
            return;
        }
        listeners = listeners.stream().sorted(OrderComparator.getDefault())
                .collect(Collectors.toList());
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
                    logger.log(Level.SEVERE, "执行事件监听时发生异常。", ex);
                }
            }
        }
    }

    @Override
    public void publish(@NotNull Object event) {
        AssertUtils.notNull(event, "无效的事件");
        EventDetail detail = rememberEvent(event);
        if (detail.isAsynchronous()) {
            publishAsync(event, MapUtils.emptyIfNull(eventListeners.get(detail.getType())).values());
        } else {
            publishEvent(event, MapUtils.emptyIfNull(eventListeners.get(detail.getType())).values());
        }
    }

    @Override
    public void publishAsync(@NotNull Object event) {
        AssertUtils.notNull(event, "无效的事件");
        EventDetail detail = rememberEvent(event);
        publishAsync(event, MapUtils.emptyIfNull(eventListeners.get(detail.getType())).values());
    }

    @Override
    @NotNull
    public EventListenerUid subscribe(@NotNull EventListener<?> eventListener) {
        AssertUtils.notNull(eventListener, "无效的事件监听器");
        Class<?> listenerClass = eventListener.getClass();
        TypeReference<?> type = TypeReference.of(
                listenerClass, EventListener.class
        );
        Map<EventListenerUid, EventListener<?>> listenerMap = eventListeners
                .computeIfAbsent(
                        type, (key) -> MapUtils.concurrentMapOf()
                );
        EventListenerUidImpl uid;
        subscribeLock.lock();
        try {
            uid = new EventListenerUidImpl(type);
            while (listenerMap.containsKey(uid)) {
                uid.reset();
            }
            listenerMap.put(uid, eventListener);
        } finally {
            subscribeLock.unlock();
        }
        return uid;
    }

    @Override
    @Nullable
    public EventListener<?> unsubscribe(@NotNull EventListenerUid eventListenerUid) {
        AssertUtils.notNull(eventListenerUid, "无效的事件监听器唯一序号");
        if (eventListenerUid instanceof EventListenerUidImpl) {
            EventListenerUidImpl uid = (EventListenerUidImpl) eventListenerUid;
            Map<EventListenerUid, EventListener<?>> listenerMap = eventListeners.get(uid.getType());
            if (MapUtils.isNullOrEmpty(listenerMap)) {
                return null;
            }
            return listenerMap.remove(uid);
        }
        return null;
    }

    @Override
    public void reset() {
        eventListeners.clear();
        executorService.shutdownNow();
    }

    @NotNull
    private EventDetail rememberEvent(Object event) {
        Class<?> cls = event.getClass();
        return eventDetails.computeIfAbsent(cls, (key) -> {
            Annotated<? extends Class<?>> annotated = ReflectAnnotatedUtils.of(cls);
            EventType eventType = annotated.getAnnotation(EventType.class);
            Class<?> ancestorClass;
            if (ObjectUtils.isNull(eventType)) {
                ancestorClass = cls;
            } else {
                ancestorClass = eventType.value();
                if (EventType.DEFAULT.equals(ancestorClass)) {
                    ancestorClass = cls;
                }
            }
            TypeReference<?> type = TypeReference.of(cls, ancestorClass, ObjectUtils.isNotEquals(cls, ancestorClass));
            return new EventDetail(
                    type,
                    annotated.hasAnnotation(Asynchronous.class)
            );
        });
    }

    @SuppressWarnings({"rawtypes"})
    private void publishAsync(Object event, Collection<? extends EventListener> listeners) {
        executorService.submit(() -> publishEvent(event, listeners));
    }

    private static class EventDetail {
        private final TypeReference<?> type;
        private final boolean asynchronous;

        private EventDetail(@NotNull TypeReference<?> type, boolean asynchronous) {
            AssertUtils.notNull(type, "无效的事件类型");
            this.type = type;
            this.asynchronous = asynchronous;
        }

        @NotNull
        public TypeReference<?> getType() {
            return type;
        }

        public boolean isAsynchronous() {
            return asynchronous;
        }
    }

}
