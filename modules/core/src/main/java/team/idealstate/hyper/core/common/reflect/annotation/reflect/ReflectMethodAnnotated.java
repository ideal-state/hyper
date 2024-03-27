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
import team.idealstate.hyper.core.common.reflect.MethodUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.SetUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * <p>ReflectMethodAnnotated</p>
 *
 * <p>创建于 2024/3/27 1:50</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class ReflectMethodAnnotated extends ReflectDeclaredAnnotated<Method> {
    public ReflectMethodAnnotated(@NotNull Method annotatedElement) {
        super(annotatedElement);
    }

    @Override
    public boolean hasAnnotation(@NotNull Class<? extends Annotation> annotationType) {
        if (hasDeclaredAnnotation(annotationType)) {
            return true;
        }
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            if (ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))) {
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            if (ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    @Override
    public final boolean hasAllAnnotations(Class<? extends Annotation>... annotationTypes) {
        Method annotatedElement = getAnnotatedElement();
        Set<Class<? extends Annotation>> annotationTypeSet = SetUtils.setOf(annotationTypes);
        annotationTypeSet.removeIf(annotationType ->
                ObjectUtils.isNotNull(annotatedElement.getDeclaredAnnotation(annotationType))
        );
        if (CollectionUtils.isEmpty(annotationTypeSet)) {
            return true;
        }
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(annotatedElement)) {
            annotationTypeSet.removeIf(annotationType ->
                    ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))
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
        Method annotatedElement = getAnnotatedElement();
        Set<Class<? extends Annotation>> annotationTypeSet = SetUtils.setOf(annotationTypes);
        annotationTypeSet.removeIf(annotationType ->
                ObjectUtils.isNotNull(annotatedElement.getDeclaredAnnotation(annotationType))
        );
        if (CollectionUtils.isEmpty(annotationTypeSet)) {
            return false;
        }
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(annotatedElement)) {
            annotationTypeSet.removeIf(annotationType ->
                    ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            for (Class<? extends Annotation> annotationType : annotationTypes) {
                if (ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))) {
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            for (Class<? extends Annotation> annotationType : annotationTypes) {
                if (ObjectUtils.isNotNull(ancestorMethod.getDeclaredAnnotation(annotationType))) {
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            annotation = ancestorMethod.getDeclaredAnnotation(annotationType);
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            A[] annotations = ancestorMethod.getDeclaredAnnotationsByType(annotationType);
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
        for (Method ancestorMethod : MethodUtils.getAncestorMethods(getAnnotatedElement())) {
            Annotation[] annotations = ancestorMethod.getDeclaredAnnotations();
            if (ArrayUtils.isEmpty(annotations)) {
                continue;
            }
            CollectionUtils.addAll(result, annotations);
        }
        return result.toArray(declaredAnnotations);
    }
}
