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

package team.idealstate.hyper.core.common.reflect.annotation;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * <p>Annotated</p>
 *
 * <p>创建于 2024/3/24 22:39</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public interface Annotated<E extends AnnotatedElement> {

    boolean hasAnnotation(@NotNull Class<? extends Annotation> annotationType);

    boolean hasNotAnnotation(@NotNull Class<? extends Annotation> annotationType);

    boolean hasAllAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasNotAllAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasAnyAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasNotAnyAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasDeclaredAnnotation(@NotNull Class<? extends Annotation> annotationType);

    boolean hasNotDeclaredAnnotation(@NotNull Class<? extends Annotation> annotationType);

    boolean hasDeclaredAllAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasNotDeclaredAllAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasDeclaredAnyAnnotations(Class<? extends Annotation>... annotationTypes);

    boolean hasNotDeclaredAnyAnnotations(Class<? extends Annotation>... annotationTypes);

    @Nullable <A extends Annotation> A getAnnotation(@NotNull Class<A> annotationType);

    <A extends Annotation> A @NotNull [] getAnnotations(@NotNull Class<A> annotationType);

    Annotation @NotNull [] getAnnotations();

    @Nullable <A extends Annotation> A getDeclaredAnnotation(@NotNull Class<A> annotationType);

    <A extends Annotation> A @NotNull [] getDeclaredAnnotations(@NotNull Class<A> annotationType);

    Annotation @NotNull [] getDeclaredAnnotations();

    @NotNull
    E getAnnotatedElement();
}
