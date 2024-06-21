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

package team.idealstate.hyper.common.reflect.annotation.reflect;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <p>ReflectFieldAnnotated</p>
 *
 * <p>创建于 2024/3/27 0:35</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ReflectFieldAnnotated extends ReflectDeclaredAnnotated<Field> {
    public ReflectFieldAnnotated(@NotNull Field annotatedElement) {
        super(annotatedElement);
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        return hasDeclaredAnnotation(annotationType);
    }

    @Override
    public boolean hasNotAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        return hasNotDeclaredAnnotation(annotationType);
    }

    @SafeVarargs
    @Override
    public final boolean hasAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        return hasDeclaredAllAnnotations(annotationTypes);
    }

    @SafeVarargs
    @Override
    public final boolean hasNotAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        return hasNotDeclaredAllAnnotations(annotationTypes);
    }

    @SafeVarargs
    @Override
    public final boolean hasAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        return hasDeclaredAnyAnnotations(annotationTypes);
    }

    @SafeVarargs
    @Override
    public final boolean hasNotAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        return hasNotDeclaredAnyAnnotations(annotationTypes);
    }

    @Override
    @Nullable
    public <A extends Annotation> A getAnnotation(@NotNull Class<A> annotationType) {
        return getDeclaredAnnotation(annotationType);
    }

    @Override
    @NotNull
    public <A extends Annotation> A[] getAnnotations(@NotNull Class<A> annotationType) {
        return getDeclaredAnnotations(annotationType);
    }

    @Override
    @NotNull
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }
}
