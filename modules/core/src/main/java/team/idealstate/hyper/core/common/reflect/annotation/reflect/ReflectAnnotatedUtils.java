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

package team.idealstate.hyper.core.common.reflect.annotation.reflect;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.reflect.annotation.Annotated;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>ReflectAnnotatedUtils</p>
 *
 * <p>创建于 2024/3/27 1:57</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ReflectAnnotatedUtils {

    @NotNull
    public static <T> Annotated<Class<T>> of(@NotNull Class<T> cls) {
        AssertUtils.notNull(cls, "无效的类");
        return new ReflectClassAnnotated<>(cls);
    }

    @NotNull
    public static Annotated<Method> of(@NotNull Method method) {
        AssertUtils.notNull(method, "无效的方法");
        return new ReflectMethodAnnotated(method);
    }

    @NotNull
    public static Annotated<Field> of(@NotNull Field field) {
        AssertUtils.notNull(field, "无效的字段");
        return new ReflectFieldAnnotated(field);
    }
}
