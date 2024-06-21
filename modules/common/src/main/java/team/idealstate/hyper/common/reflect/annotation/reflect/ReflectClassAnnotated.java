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
import team.idealstate.hyper.common.array.ArrayUtils;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.reflect.ClassUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.SetUtils;

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
    @Nullable
    public <A extends Annotation> A getAnnotation(@NotNull Class<A> annotationType) {
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
    @NotNull
    public <A extends Annotation> A[] getAnnotations(@NotNull Class<A> annotationType) {
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
    @NotNull
    public Annotation[] getAnnotations() {
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
