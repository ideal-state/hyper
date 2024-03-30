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
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.array.ArrayUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.ClassUtils;
import team.idealstate.hyper.core.common.string.StringUtils;
import team.idealstate.hyper.core.common.template.ListUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * <p>TypeReference</p>
 *
 * <p>创建于 2024/3/30 0:11</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @see AmbiguousTypeException
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class TypeReference<T> implements SuperType {

    private final SuperType type;

    protected TypeReference() throws AmbiguousTypeException {
        this(null, TypeReference.class, false);
    }

    private TypeReference(@Nullable Class<?> actualClass, @NotNull Class<?> ancestorClass, boolean includeRawType) {
        AssertUtils.isTrue(
                ObjectUtils.isNotNull(actualClass) ||
                        TypeReference.class.equals(ancestorClass),
                "无效的类"
        );
        actualClass = ObjectUtils.defaultIfNull(actualClass, getClass());
        AssertUtils.notNull(ancestorClass, "无效的祖先类");
        AssertUtils.isTrue(
                ancestorClass.isAssignableFrom(actualClass),
                "无效的类"
        );
        HyperType hyperType = null;
        if (ObjectUtils.isEquals(actualClass, ancestorClass)) {
            hyperType = new HyperType(actualClass);
            if (hyperType.hasTypeVariableOfAssignableValue()) {
                throw new AmbiguousTypeException();
            }
            this.type = (SuperType) hyperType.type;
            return;
        }
        List<Class<?>> classes = getAncestorClasses(actualClass, ancestorClass);
        for (int i = 1; i < classes.size(); i++) {
            Class<?> cls = classes.get(i);
            Class<?> ancestorCls = classes.get(i - 1);
            Type genericCls;
            GENERIC:
            if (ancestorCls.isInterface()) {
                for (Type genericInterface : cls.getGenericInterfaces()) {
                    if (genericInterface instanceof ParameterizedType) {
                        if (ancestorCls.equals(((ParameterizedType) genericInterface).getRawType())) {
                            genericCls = genericInterface;
                            break GENERIC;
                        }
                    } else if (ancestorCls.equals(genericInterface)) {
                        genericCls = genericInterface;
                        break GENERIC;
                    }
                }
                throw new AmbiguousTypeException();
            } else {
                genericCls = cls.getGenericSuperclass();
            }
            if (ObjectUtils.isNull(hyperType)) {
                hyperType = new HyperType(genericCls);
                if (!hyperType.hasTypeVariableOfAssignableValue()) {
                    break;
                }
                continue;
            }
            if (!hyperType.hasTypeVariableOfAssignableValue()) {
                break;
            }
            if (!(genericCls instanceof ParameterizedType)) {
                break;
            }
            TypeVariable<? extends Class<?>>[] typeVariables = ancestorCls.getTypeParameters();
            if (ArrayUtils.isEmpty(typeVariables)) {
                break;
            }
            Type[] actualTypeArguments = ((ParameterizedType) genericCls).getActualTypeArguments();
            for (int j = 0; j < typeVariables.length; j++) {
                hyperType.setTypeVariableValue(typeVariables[j], actualTypeArguments[j]);
            }
        }
        if (ObjectUtils.isNull(hyperType) || hyperType.hasTypeVariableOfAssignableValue()) {
            throw new AmbiguousTypeException();
        }
        if (hyperType.type instanceof ParameterizedType && !includeRawType) {
            this.type = (SuperType) ((ParameterizedType) hyperType.type).getActualTypeArguments()[0];
        } else {
            this.type = (SuperType) hyperType.type;
        }
    }

    @NotNull
    public static TypeReference<?> of(@NotNull Class<?> actualClass, @NotNull Class<?> ancestorClass) {
        return of(actualClass, ancestorClass, false);
    }

    @NotNull
    public static TypeReference<?> of(@NotNull Class<?> actualClass, @NotNull Class<?> ancestorClass, boolean includeRawType) {
        AssertUtils.notNull(actualClass, "无效的类");
        AssertUtils.notNull(ancestorClass, "无效的祖先类");
        AssertUtils.isTrue(
                ancestorClass.isAssignableFrom(actualClass),
                "无效的类"
        );
        return new OfClassTypeReference<>(actualClass, ancestorClass, includeRawType);
    }

    private static List<Class<?>> getAncestorClasses(@NotNull Class<?> actualClass, @NotNull Class<?> ancestorClass) {
        List<Class<?>> ancestorClasses = ClassUtils.getAncestorClasses(actualClass);
        Collections.reverse(ancestorClasses);
        Iterator<Class<?>> iterator = ancestorClasses.iterator();
        boolean removing = true;
        while (iterator.hasNext()) {
            Class<?> next = iterator.next();
            if (ancestorClass.equals(next)) {
                removing = false;
                continue;
            }
            if (removing) {
                iterator.remove();
                continue;
            }
            if (ancestorClass.isAssignableFrom(next)) {
                continue;
            }
            iterator.remove();
        }
        ancestorClasses.add(actualClass);
        return ListUtils.listOf(ancestorClasses);
    }

    public static void main(String[] args) {
        System.out.println(new TypeReference<String>() {
        });
    }

    @Override
    public final boolean isAssignableFrom(@NotNull Type subtype) {
        return type.isAssignableFrom(subtype);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeReference)) {
            return false;
        }
        final TypeReference<?> that = (TypeReference<?>) o;

        return Objects.equals(type, that.type);
    }

    @Override
    public final int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public final String toString() {
        return StringUtils.valueOf(type);
    }

    private static final class OfClassTypeReference<O> extends TypeReference<O> {
        private OfClassTypeReference(@Nullable Class<?> actualClass, @NotNull Class<?> ancestorClass, boolean includeRawType) {
            super(actualClass, ancestorClass, includeRawType);
        }
    }
}
