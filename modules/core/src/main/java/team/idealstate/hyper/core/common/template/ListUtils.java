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

package team.idealstate.hyper.core.common.template;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.array.ArrayUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>ListUtils</p>
 *
 * <p>创建于 2024/3/24 19:39</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ListUtils {

    @NotNull
    public static <E> List<E> emptyIfNull(@Nullable List<E> list) {
        return list == null ? emptyList() : list;
    }

    @NotNull
    public static <E> List<E> defaultIfEmpty(@NotNull List<E> list, @NotNull List<E> def) {
        AssertUtils.notNull(list, "无效的列表");
        AssertUtils.notNull(def, "无效的默认值");
        return CollectionUtils.isEmpty(list) ? def : list;
    }

    @NotNull
    public static <E> List<E> defaultIfNullOrEmpty(@Nullable List<E> list, @NotNull List<E> def) {
        AssertUtils.notNull(def, "无效的默认值");
        if (CollectionUtils.isNullOrEmpty(list)) {
            return def;
        } else {
            assert list != null;
            return list;
        }
    }

    @NotNull
    public static <E> List<E> emptyList() {
        return Collections.emptyList();
    }

    @NotNull
    @SafeVarargs
    public static <E> List<E> listOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new ArrayList<>(16);
        }
        List<E> list = new ArrayList<>(elements.length);
        CollectionUtils.addAll(list, elements);
        return (list);
    }

    @NotNull
    @SafeVarargs
    public static <E> List<E> linkedListOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new LinkedList<>();
        }
        List<E> list = new LinkedList<>();
        CollectionUtils.addAll(list, elements);
        return (list);
    }

    @NotNull
    @SafeVarargs
    public static <E> List<E> concurrentListOf(E... elements) {
        if (ArrayUtils.isEmpty(elements)) {
            return new CopyOnWriteArrayList<>();
        }
        return new CopyOnWriteArrayList<>(elements);
    }

    @NotNull
    public static <E> List<E> listOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>(16);
        }
        return (new ArrayList<>(collection));
    }

    @NotNull
    public static <E> List<E> linkedListOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new LinkedList<>();
        }
        return (new LinkedList<>(collection));
    }

    @NotNull
    public static <E> List<E> concurrentListOf(@NotNull Collection<E> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new CopyOnWriteArrayList<>();
        }
        return new CopyOnWriteArrayList<>(collection);
    }
}
