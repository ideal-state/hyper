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
import team.idealstate.hyper.common.array.ArrayUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>SetUtils</p>
 *
 * <p>创建于 2024/3/24 19:39</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class SetUtils {

    @NotNull
    public static <E> Set<E> emptyIfNull(@Nullable Set<E> set) {
        return set == null ? emptySet() : set;
    }

    @NotNull
    public static <E> Set<E> defaultIfEmpty(@NotNull Set<E> set, @NotNull Set<E> def) {
        AssertUtils.notNull(set, "无效的集合");
        AssertUtils.notNull(def, "无效的默认值");
        return CollectionUtils.isEmpty(set) ? def : set;
    }

    @NotNull
    public static <E> Set<E> defaultIfNullOrEmpty(@Nullable Set<E> set, @NotNull Set<E> def) {
        AssertUtils.notNull(def, "无效的默认值");
        if (CollectionUtils.isNullOrEmpty(set)) {
            return def;
        } else {
            assert set != null;
            return set;
        }
    }

    @NotNull
    public static <E> Set<E> emptySet() {
        return Collections.emptySet();
    }

    @NotNull
    @SafeVarargs
    public static <E> Set<E> setOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new HashSet<>(16, 0.6F);
        }
        Set<E> set = new HashSet<>(elements.length);
        CollectionUtils.addAll(set, elements);
        return set;
    }

    @NotNull
    @SafeVarargs
    public static <E> Set<E> linkedSetOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new LinkedHashSet<>(16, 0.6F);
        }
        Set<E> set = new LinkedHashSet<>(elements.length);
        CollectionUtils.addAll(set, elements);
        return set;
    }

    @NotNull
    @SafeVarargs
    public static <E> Set<E> concurrentSetOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new CopyOnWriteArraySet<>();
        }
        return new CopyOnWriteArraySet<>(ListUtils.listOf(elements));
    }

    @NotNull
    public static <E> Set<E> setOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashSet<>(16, 0.6F);
        }
        return (new HashSet<>(collection));
    }

    @NotNull
    public static <E> Set<E> linkedSetOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new LinkedHashSet<>(16, 0.6F);
        }
        return (new LinkedHashSet<>(collection));
    }

    @NotNull
    public static <E> Set<E> concurrentSetOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new CopyOnWriteArraySet<>();
        }
        return new CopyOnWriteArraySet<>(collection);
    }
}
