/*
 *    hyper-director
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

package team.idealstate.hyper.director.impl;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.director.api.DirectionContext;

import java.util.*;

/**
 * <p>StdDirectionContext</p>
 *
 * <p>
 * 这个实现是一个简单的指令上下文实现，它并不是并发安全的（话虽如此，但通常情况下它不应该在并发环境下使用）。
 * </p>
 *
 * <p>创建于 2024/2/16 16:55</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdDirectionContext implements DirectionContext {

    private final Map<String, Object> valueMap = new LinkedHashMap<>(16, 0.6F);

    @Override
    @Nullable
    public Object put(@NotNull String key, @Nullable Object value) {
        AssertUtils.notBlank(key, "无效的键");
        return valueMap.put(key, value);
    }

    @Override
    @Nullable
    public Object remove(@NotNull String key) {
        AssertUtils.notBlank(key, "无效的键");
        return valueMap.remove(key);
    }

    @Override
    @NotNull
    public Set<String> getKeys() {
        if (valueMap.isEmpty()) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(valueMap.keySet());
    }

    @Override
    public boolean containsKey(@NotNull String key) {
        AssertUtils.notBlank(key, "无效的键");
        return valueMap.containsKey(key);
    }

    @Override
    public boolean containsValue(@NotNull String key) {
        AssertUtils.notBlank(key, "无效的键");
        return valueMap.get(key) != null;
    }

    @Override
    @Nullable
    public Object getValue(@NotNull String key) {
        AssertUtils.notBlank(key, "无效的键");
        return valueMap.get(key);
    }

    @Override
    public <T> @Nullable T getValue(@NotNull String key, @NotNull Class<T> valueType) {
        AssertUtils.notBlank(key, "无效的键");
        AssertUtils.notNull(valueType, "无效的值类型");
        if (valueMap.containsKey(key)) {
            Object valueObject = valueMap.get(key);
            if (valueType.isInstance(valueObject)) {
                return valueType.cast(valueObject);
            }
        }
        return null;
    }

    @Override
    public void clear() {
        valueMap.clear();
    }

    @Override
    @NotNull
    public Map<String, Object> asMap() {
        return MapUtils.linkedMapOf(valueMap);
    }
}
