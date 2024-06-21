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

package team.idealstate.hyper.common.template;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.object.ObjectUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>CollectionUtils</p>
 *
 * <p>创建于 2024/2/16 18:40</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes"})
public abstract class CollectionUtils {

    @NotNull
    public static <E> Collection<E> emptyIfNull(@Nullable Collection<E> collection) {
        return collection == null ? emptyCollection() : collection;
    }

    @NotNull
    public static <E> Collection<E> defaultIfEmpty(@NotNull Collection<E> collection, @NotNull Collection<E> def) {
        AssertUtils.notNull(collection, "无效的合集");
        AssertUtils.notNull(def, "无效的默认值");
        return isEmpty(collection) ? def : collection;
    }

    @NotNull
    public static <E> Collection<E> defaultIfNullOrEmpty(@Nullable Collection<E> collection, @NotNull Collection<E> def) {
        AssertUtils.notNull(def, "无效的默认值");
        if (isNullOrEmpty(collection)) {
            return def;
        } else {
            assert collection != null;
            return collection;
        }
    }

    public static boolean isEmpty(@NotNull Collection collection) {
        AssertUtils.notNull(collection, "无效的合集");
        return collection.isEmpty();
    }

    public static boolean isNullOrEmpty(@Nullable Collection collection) {
        return ObjectUtils.isNull(collection) || collection.isEmpty();
    }

    public static boolean isNotEmpty(@NotNull Collection collection) {
        AssertUtils.notNull(collection, "无效的合集");
        return !collection.isEmpty();
    }

    public static boolean isNotNullOrEmpty(@Nullable Collection collection) {
        return ObjectUtils.isNotNull(collection) && !collection.isEmpty();
    }

    @NotNull
    public static <E> Collection<E> emptyCollection() {
        return Collections.emptyList();
    }

    @SafeVarargs
    public static <T> boolean addAll(@NotNull Collection<? super T> collection, T... elements) {
        AssertUtils.notNull(collection, "无效的合集");
        return Collections.addAll(collection, elements);
    }

    public static <T> boolean addAll(@NotNull Collection<? super T> into, @NotNull Collection<? extends T> from) {
        AssertUtils.notNull(into, "无效的合集");
        AssertUtils.notNull(from, "无效的合集");
        return into.addAll(from);
    }
}
