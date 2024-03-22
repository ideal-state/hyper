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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>CollectionUtils</p>
 *
 * <p>创建于 2024/2/16 18:40</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class CollectionUtils {

    @NotNull
    public static <E> List<E> promise(@Nullable List<E> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }

    @NotNull
    public static <E> Set<E> promise(@Nullable Set<E> collection) {
        return collection == null ? Collections.emptySet() : collection;
    }

    @NotNull
    public static <E> Collection<E> promise(@Nullable Collection<E> collection) {
        return collection == null ? Collections.emptyList() : collection;
    }
}
