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
import team.idealstate.hyper.common.object.ObjectUtils;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * <p>HyperGenericArrayType</p>
 *
 * <p>创建于 2024/3/30 5:36</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @see sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl
 * @since 1.0.0
 */
final class HyperGenericArrayType implements SuperHyperType, GenericArrayType, TypeVariableValueAssignable {

    private Type genericComponentType;

    HyperGenericArrayType(@NotNull GenericArrayType genericArrayType) {
        AssertUtils.notNull(genericArrayType, "无效的泛型数组类型");
        this.genericComponentType = toHyperType(genericArrayType.getGenericComponentType());
    }

    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }

    @Override
    public <GD extends GenericDeclaration> void setTypeVariableValue(@NotNull TypeVariable<GD> typeVariable, @NotNull Type value) {
        if (genericComponentType instanceof TypeVariableValueAssignable) {
            ((TypeVariableValueAssignable) genericComponentType).setTypeVariableValue(typeVariable, value);
        } else if (ObjectUtils.isEquals(genericComponentType, typeVariable)) {
            genericComponentType = toHyperType(value);
        }
    }

    @Override
    public boolean hasTypeVariableOfAssignableValue() {
        if (genericComponentType instanceof TypeVariableValueAssignable) {
            if (((TypeVariableValueAssignable) genericComponentType).hasTypeVariableOfAssignableValue()) {
                return true;
            }
        }
        return genericComponentType instanceof TypeVariable;
    }

    @Override
    public boolean isAssignableFrom(@NotNull Type subtype) {
        if (genericComponentType instanceof SuperHyperType && !hasTypeVariableOfAssignableValue()) {
            return ((SuperHyperType) genericComponentType).isAssignableFrom(subtype);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HyperGenericArrayType)) {
            return false;
        }
        final HyperGenericArrayType that = (HyperGenericArrayType) o;

        return Objects.equals(genericComponentType, that.genericComponentType);
    }

    @Override
    public int hashCode() {
        return genericComponentType != null ? genericComponentType.hashCode() : 0;
    }

    @NotNull
    @Override
    public String toString() {
        Type componentType = getGenericComponentType();
        StringBuilder sb = new StringBuilder();

        if (componentType instanceof Class) {
            sb.append(((Class<?>) componentType).getName());
        } else {
            sb.append(componentType.toString());
        }
        sb.append("[]");
        return sb.toString();
    }
}
