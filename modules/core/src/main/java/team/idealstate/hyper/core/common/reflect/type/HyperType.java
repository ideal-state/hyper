/*
 *    hyper-core
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

package team.idealstate.hyper.core.common.reflect.type;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * <p>HyperType</p>
 *
 * <p>创建于 2024/3/30 6:10</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class HyperType implements SuperHyperType, TypeVariableValueAssignable {

    Type type;

    HyperType(@NotNull Type type) {
        AssertUtils.notNull(type, "无效的类型");
        this.type = toHyperType(type);
    }

    @Override
    public <GD extends GenericDeclaration> void setTypeVariableValue(@NotNull TypeVariable<GD> typeVariable, @NotNull Type value) {
        if (type instanceof TypeVariableValueAssignable) {
            ((TypeVariableValueAssignable) type).setTypeVariableValue(typeVariable, value);
        } else if (ObjectUtils.isEquals(type, typeVariable)) {
            type = value;
        }
    }

    @Override
    public boolean hasTypeVariableOfAssignableValue() {
        if (type instanceof TypeVariableValueAssignable) {
            if (((TypeVariableValueAssignable) type).hasTypeVariableOfAssignableValue()) {
                return true;
            }
        }
        return type instanceof TypeVariable;
    }

    @Override
    public boolean isAssignableFrom(@NotNull Type subtype) {
        if (type instanceof SuperHyperType && !hasTypeVariableOfAssignableValue()) {
            return ((SuperHyperType) type).isAssignableFrom(subtype);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HyperType)) {
            return false;
        }
        final HyperType hyperType = (HyperType) o;

        return Objects.equals(type, hyperType.type);
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
