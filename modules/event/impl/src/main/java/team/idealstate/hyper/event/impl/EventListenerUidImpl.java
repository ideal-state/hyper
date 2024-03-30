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
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.reflect.type.TypeReference;
import team.idealstate.hyper.event.api.EventListenerUid;

import java.util.UUID;

/**
 * <p>EventListenerUidImpl</p>
 *
 * <p>创建于 2024/3/30 10:36</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class EventListenerUidImpl implements EventListenerUid {

    private final TypeReference<?> type;
    private UUID uuid;

    EventListenerUidImpl(@NotNull TypeReference<?> type) {
        AssertUtils.notNull(type, "无效的事件类型");
        this.uuid = UUID.randomUUID();
        this.type = type;
    }

    void reset() {
        uuid = UUID.randomUUID();
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    @NotNull
    public TypeReference<?> getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventListenerUidImpl)) {
            return false;
        }
        final EventListenerUidImpl that = (EventListenerUidImpl) object;

        if (!uuid.equals(that.uuid)) {
            return false;
        }
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EventListenerUidImpl{" +
                "uuid=" + uuid +
                ", type=" + type +
                '}';
    }
}
