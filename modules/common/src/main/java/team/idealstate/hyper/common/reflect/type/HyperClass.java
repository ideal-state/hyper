/*
 *    hyper-common
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

package team.idealstate.hyper.common.reflect.type;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.AssertUtils;

import java.lang.reflect.Type;

/**
 * <p>HyperClass</p>
 *
 * <p>创建于 2024/3/30 7:02</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class HyperClass implements SuperHyperType {

    final Class<?> cls;

    HyperClass(@NotNull Class<?> cls) {
        AssertUtils.notNull(cls, "无效的类");
        this.cls = cls;
    }

    @Override
    public boolean isAssignableFrom(@NotNull Type subtype) {
        return subtype instanceof Class && cls.isAssignableFrom((Class<?>) subtype);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HyperClass)) {
            return false;
        }
        final HyperClass that = (HyperClass) o;

        return cls.equals(that.cls);
    }

    @Override
    public int hashCode() {
        return cls.hashCode();
    }

    @Override
    public String toString() {
        return cls.toString();
    }
}
