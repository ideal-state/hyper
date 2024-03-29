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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.core.common.array.ArrayUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.ClassUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.SetUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * <p>ReflectClassAnnotated</p>
 *
 * <p>创建于 2024/3/26 22:47</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ReflectClassAnnotated<T> extends ReflectDeclaredAnnotated<Class<T>> {

    public ReflectClassAnnotated(@NotNull Class<T> annotatedElement) {
        super(annotatedElement);
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        if (hasDeclaredAnnotation(annotationType)) {
            return true;
        }
        List<Class<?>> ancestorClasses = ClassUtils.getAncestorClasses(getAnnotatedElement());
        for (Class<?> ancestorClass : ancestorClasses) {
            if (ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasNotAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        if (hasNotDeclaredAnnotation(annotationType)) {
            return true;
        }
        List<Class<?>> ancestorClasses = ClassUtils.getAncestorClasses(getAnnotatedElement());
        for (Class<?> ancestorClass : ancestorClasses) {
            if (ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    @Override
    public final boolean hasAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        Class<T> annotatedElement = getAnnotatedElement();
        Set<Class<? extends Annotation>> annotationTypeSet = SetUtils.setOf(annotationTypes);
        annotationTypeSet.removeIf(annotationType ->
                ObjectUtils.isNotNull(annotatedElement.getDeclaredAnnotation(annotationType))
        );
        if (CollectionUtils.isEmpty(annotationTypeSet)) {
            return true;
        }
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(annotatedElement)) {
            annotationTypeSet.removeIf(annotationType ->
                    ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))
            );
            if (CollectionUtils.isEmpty(annotationTypeSet)) {
                return true;
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final boolean hasNotAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        Class<T> annotatedElement = getAnnotatedElement();
        Set<Class<? extends Annotation>> annotationTypeSet = SetUtils.setOf(annotationTypes);
        annotationTypeSet.removeIf(annotationType ->
                ObjectUtils.isNotNull(annotatedElement.getDeclaredAnnotation(annotationType))
        );
        if (CollectionUtils.isEmpty(annotationTypeSet)) {
            return false;
        }
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(annotatedElement)) {
            annotationTypeSet.removeIf(annotationType ->
                    ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))
            );
            if (CollectionUtils.isEmpty(annotationTypeSet)) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    @Override
    public final boolean hasAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        if (hasDeclaredAnyAnnotations(annotationTypes)) {
            return true;
        }
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(getAnnotatedElement())) {
            for (Class<? extends Annotation> annotationType : annotationTypes) {
                if (ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))) {
                    return true;
                }
            }
        }
        return false;
    }

    @SafeVarargs
    @Override
    public final boolean hasNotAnyAnnotations(Class<? extends Annotation>... annotationTypes) {
        if (hasNotDeclaredAnyAnnotations(annotationTypes)) {
            return true;
        }
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(getAnnotatedElement())) {
            for (Class<? extends Annotation> annotationType : annotationTypes) {
                if (ObjectUtils.isNotNull(ancestorClass.getDeclaredAnnotation(annotationType))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public <A extends Annotation> @Nullable A getAnnotation(@NotNull Class<A> annotationType) {
        A annotation = getDeclaredAnnotation(annotationType);
        if (ObjectUtils.isNotNull(annotation)) {
            return annotation;
        }
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(getAnnotatedElement())) {
            annotation = ancestorClass.getDeclaredAnnotation(annotationType);
            if (ObjectUtils.isNotNull(annotation)) {
                return annotation;
            }
        }
        return null;
    }

    @Override
    public <A extends Annotation> A @NotNull [] getAnnotations(@NotNull Class<A> annotationType) {
        A[] declaredAnnotations = getDeclaredAnnotations(annotationType);
        List<A> result = ListUtils.linkedListOf(declaredAnnotations);
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(getAnnotatedElement())) {
            A[] annotations = ancestorClass.getDeclaredAnnotationsByType(annotationType);
            if (ArrayUtils.isEmpty(annotations)) {
                continue;
            }
            CollectionUtils.addAll(result, annotations);
        }
        return result.toArray(declaredAnnotations);
    }

    @Override
    public Annotation @NotNull [] getAnnotations() {
        Annotation[] declaredAnnotations = getDeclaredAnnotations();
        List<Annotation> result = ListUtils.linkedListOf(declaredAnnotations);
        for (Class<?> ancestorClass : ClassUtils.getAncestorClasses(getAnnotatedElement())) {
            Annotation[] annotations = ancestorClass.getDeclaredAnnotations();
            if (ArrayUtils.isEmpty(annotations)) {
                continue;
            }
            CollectionUtils.addAll(result, annotations);
        }
        return result.toArray(declaredAnnotations);
    }
}