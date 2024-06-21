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
import team.idealstate.hyper.common.template.ListUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>HyperParameterizedType</p>
 *
 * <p>创建于 2024/3/30 4:19</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @see sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
 * @since 1.0.0
 */
final class HyperParameterizedType implements SuperHyperType, ParameterizedType, TypeVariableValueAssignable {

    private static final Type[] EMPTY_ARRAY = new Type[0];
    private final List<Type> actualTypeArgumentList;
    private final HyperClass rawType;
    private final Type ownerType;
    private Type[] actualTypeArguments;

    HyperParameterizedType(@NotNull ParameterizedType parameterizedType) {
        AssertUtils.notNull(parameterizedType, "无效的参数化类型");
        if (isHyperType(parameterizedType)) {
            this.actualTypeArgumentList = ListUtils.listOf(parameterizedType.getActualTypeArguments());
            this.rawType = (HyperClass) toHyperType(parameterizedType.getRawType());
            this.ownerType = parameterizedType.getOwnerType();
        } else {
            this.actualTypeArgumentList = ListUtils.listOf(parameterizedType.getActualTypeArguments());
            this.actualTypeArgumentList.replaceAll(this::toHyperType);
            this.rawType = (HyperClass) toHyperType(parameterizedType.getRawType());
            this.ownerType = toHyperType(parameterizedType.getOwnerType());
        }
        this.actualTypeArguments = actualTypeArgumentList.toArray(EMPTY_ARRAY);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public <GD extends GenericDeclaration> void setTypeVariableValue(@NotNull TypeVariable<GD> typeVariable, @NotNull Type value) {
        AssertUtils.notNull(typeVariable, "无效的类型变量");
        AssertUtils.notNull(value, "无效的类型");
        boolean hasChanged = false;
        for (int i = 0; i < actualTypeArgumentList.size(); i++) {
            Type actualTypeArgument = actualTypeArgumentList.get(i);
            if (actualTypeArgument instanceof TypeVariableValueAssignable) {
                ((TypeVariableValueAssignable) actualTypeArgument).setTypeVariableValue(typeVariable, value);
            } else if (ObjectUtils.isEquals(actualTypeArgument, typeVariable)) {
                actualTypeArgumentList.set(i, toHyperType(value));
                hasChanged = true;
            }
        }
        if (hasChanged) {
            this.actualTypeArguments = actualTypeArgumentList.toArray(EMPTY_ARRAY);
        }
    }

    @Override
    public boolean hasTypeVariableOfAssignableValue() {
        for (Type actualTypeArgument : actualTypeArgumentList) {
            if (actualTypeArgument instanceof TypeVariableValueAssignable) {
                if (((TypeVariableValueAssignable) actualTypeArgument).hasTypeVariableOfAssignableValue()) {
                    return true;
                }
            } else if (actualTypeArgument instanceof TypeVariable) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAssignableFrom(@NotNull Type subtype) {
        if (hasTypeVariableOfAssignableValue()) {
            return false;
        }
        if (!(subtype instanceof ParameterizedType)) {
            if (subtype instanceof Class) {
                return rawType.isAssignableFrom((Class<?>) subtype);
            } else if (subtype instanceof HyperClass) {
                return rawType.isAssignableFrom(((HyperClass) subtype).cls);
            }
            return false;
        }
        if (!(subtype instanceof HyperParameterizedType)) {
            subtype = toHyperType(subtype);
            assert subtype instanceof HyperParameterizedType;
            if (((HyperParameterizedType) subtype).hasTypeVariableOfAssignableValue()) {
                return false;
            }
        }
        HyperParameterizedType that = (HyperParameterizedType) subtype;
        if (!rawType.isAssignableFrom(that.rawType)) {
            return false;
        }
        if (!(ownerType instanceof SuperHyperType)) {
            return false;
        }
        if (!((SuperHyperType) ownerType).isAssignableFrom(that.ownerType)) {
            return false;
        }
        Type[] thatActualTypeArguments = that.getActualTypeArguments();
        if (thatActualTypeArguments.length != actualTypeArguments.length) {
            return false;
        }
        for (int i = 0; i < actualTypeArguments.length; i++) {
            Type actualTypeArgument = actualTypeArguments[i];
            if (!(actualTypeArgument instanceof SuperHyperType)) {
                return false;
            }
            if (!((SuperHyperType) actualTypeArgument).isAssignableFrom(thatActualTypeArguments[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HyperParameterizedType)) {
            return false;
        }
        final HyperParameterizedType that = (HyperParameterizedType) o;

        if (!Objects.equals(actualTypeArgumentList, that.actualTypeArgumentList)) {
            return false;
        }
        if (!Objects.equals(rawType, that.rawType)) {
            return false;
        }
        if (!Objects.equals(ownerType, that.ownerType)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(actualTypeArguments, that.actualTypeArguments);
    }

    @Override
    public int hashCode() {
        int result = actualTypeArgumentList != null ? actualTypeArgumentList.hashCode() : 0;
        result = 31 * result + (rawType != null ? rawType.hashCode() : 0);
        result = 31 * result + (ownerType != null ? ownerType.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(actualTypeArguments);
        return result;
    }

    @NotNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (ownerType != null) {
            if (ownerType instanceof Class) {
                sb.append(((Class<?>) ownerType).getName());
            } else {
                sb.append(ownerType);
            }

            sb.append("$");

            if (ownerType instanceof HyperParameterizedType) {
                // Find simple name of nested type by removing the
                // shared prefix with owner.
                sb.append(rawType.cls.getName().replace(((HyperParameterizedType) ownerType).rawType.cls.getName() + "$",
                        ""));
            } else {
                sb.append(rawType.cls.getSimpleName());
            }
        } else {
            sb.append(rawType.cls.getName());
        }

        if (actualTypeArguments != null &&
                actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t : actualTypeArguments) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(t.getTypeName());
                first = false;
            }
            sb.append(">");
        }

        return sb.toString();
    }
}
