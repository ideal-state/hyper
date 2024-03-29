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

package team.idealstate.hyper.core.common.sort;

import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.reflect.annotation.reflect.ReflectAnnotatedUtils;

import java.util.Comparator;

/**
 * <p>OrderComparator</p>
 *
 * <p>创建于 2024/3/24 22:31</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class OrderComparator<O> implements Comparator<O> {

    protected final int nullOrder;

    OrderComparator() {
        this(Order.END);
    }

    public OrderComparator(int nullOrder) {
        this.nullOrder = nullOrder;
    }

    @Override
    public int compare(O first, O second) {
        return orderOf(first) - orderOf(second);
    }

    protected final int orderOf(O orderable) {
        if (orderable instanceof Order) {
            return ((Order) orderable).value();
        }
        if (orderable instanceof Orderable) {
            return ((Orderable) orderable).order();
        }
        if (ObjectUtils.isNull(orderable)) {
            return nullOrder;
        }
        Order order = ReflectAnnotatedUtils.of(orderable.getClass()).getDeclaredAnnotation(Order.class);
        return ObjectUtils.isNull(order) ? Order.DEFAULT : order.value();
    }
}