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
import team.idealstate.hyper.core.common.AssertUtils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * <p>IteratorUtils</p>
 *
 * <p>创建于 2024/3/26 19:20</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class IteratorUtils {

    @NotNull
    public static <E> Iterator<E> emptyIterator() {
        return (Iterator<E>) EmptyIterator.INSTANCE;
    }

    private static final class EmptyIterator<E> implements Iterator<E> {

        private static final EmptyIterator INSTANCE = new EmptyIterator<>();

        private EmptyIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            AssertUtils.notNull(action, "无效的消费者");
        }
    }
}
