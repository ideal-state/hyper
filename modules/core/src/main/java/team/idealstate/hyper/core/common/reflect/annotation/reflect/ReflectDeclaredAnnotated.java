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
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.array.ArrayUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.annotation.Annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * <p>ReflectDeclaredAnnotated</p>
 *
 * <p>创建于 2024/3/26 22:05</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
abstract class ReflectDeclaredAnnotated<E extends AnnotatedElement> implements Annotated<E> {

    private final E annotatedElement;

    protected ReflectDeclaredAnnotated(@NotNull E annotatedElement) {
        AssertUtils.notNull(annotatedElement, "无效的可被注解元素");
        this.annotatedElement = annotatedElement;
    }

    @Override
    public boolean hasDeclaredAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        return ObjectUtils.isNotNull(getDeclaredAnnotation(annotationType));
    }

    @Override
    public boolean hasNotDeclaredAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        return ObjectUtils.isNull(getDeclaredAnnotation(annotationType));
    }

    @SafeVarargs
    @Override
    public final boolean hasDeclaredAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        AssertUtils.notNull(annotationTypes, "无效的注解类型数组");
        if (ArrayUtils.isEmpty(annotationTypes)) {
            return true;
        }
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (hasNotDeclaredAnnotation(annotationType)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    @Override
    public final boolean hasNotDeclaredAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        AssertUtils.notNull(annotationTypes, "无效的注解类型数组");
        if (ArrayUtils.isEmpty(annotationTypes)) {
            return true;
        }
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (hasNotDeclaredAnnotation(annotationType)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final boolean hasDeclaredAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        AssertUtils.notNull(annotationTypes, "无效的注解类型数组");
        if (ArrayUtils.isEmpty(annotationTypes)) {
            return true;
        }
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (hasDeclaredAnnotation(annotationType)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final boolean hasNotDeclaredAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        AssertUtils.notNull(annotationTypes, "无效的注解类型数组");
        if (ArrayUtils.isEmpty(annotationTypes)) {
            return true;
        }
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (hasDeclaredAnnotation(annotationType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nullable
    public <A extends Annotation> A getDeclaredAnnotation(@NotNull Class<A> annotationType) {
        AssertUtils.notNull(annotationType, "无效的注解类型");
        return getAnnotatedElement().getDeclaredAnnotation(annotationType);
    }

    @Override
    @NotNull
    public <A extends Annotation> A[] getDeclaredAnnotations(@NotNull Class<A> annotationType) {
        AssertUtils.notNull(annotationType, "无效的注解类型");
        return getAnnotatedElement().getDeclaredAnnotationsByType(annotationType);
    }

    @Override
    @NotNull
    public Annotation[] getDeclaredAnnotations() {
        return getAnnotatedElement().getDeclaredAnnotations();
    }

    @Override
    @NotNull
    public final E getAnnotatedElement() {
        return annotatedElement;
    }
}
