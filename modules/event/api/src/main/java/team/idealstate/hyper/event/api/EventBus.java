/*
 *    hyper-event-api
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

package team.idealstate.hyper.event.api;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;

/**
 * <p>EventBus</p>
 *
 * <p>创建于 2024/3/12 13:23</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface EventBus {

    void publish(@NotNull Object event);

    @NotNull
    <EVENT> EventListener.UID<EVENT> subscribe(@NotNull EventListener<EVENT> eventListener);

    @Nullable
    <EVENT> EventListener<EVENT> unsubscribe(@NotNull EventListener.UID<EVENT> eventListenerUid);
}
