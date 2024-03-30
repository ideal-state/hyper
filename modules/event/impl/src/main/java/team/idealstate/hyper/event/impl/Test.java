/*
 *    hyper-event-impl
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

package team.idealstate.hyper.event.impl;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.string.StringJoiner;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.event.api.EventListener;
import team.idealstate.hyper.event.api.annotation.EventType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * <p>Test</p>
 *
 * <p>创建于 2024/3/30 11:08</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class Test {

    public static void main(String[] args) {
        EventBusImpl eventBus = new EventBusImpl(
                () -> Executors.newFixedThreadPool(2)
        );
        eventBus.subscribe(
                (EventListener<String>) event -> System.out.println("A: " + event)
        );
        eventBus.subscribe(new EventListener<List<String>>() {
                               @Override
                               public void on(@NotNull List<String> event) throws Throwable {
                                   System.out.println("B: " + new StringJoiner(" ").append(event));
                               }
                           }
        );
        eventBus.subscribe(new StringEventListener());
        eventBus.publishAsync("ni hao");
        eventBus.publish(new StringList(ListUtils.listOf("hello", "world")));
        eventBus.reset();
    }
}

@EventType(List.class)
class StringList extends ArrayList<String> {
    private static final long serialVersionUID = -19638672830893638L;

    public StringList(int initialCapacity) {
        super(initialCapacity);
    }

    public StringList() {
    }

    public StringList(Collection<? extends String> c) {
        super(c);
    }
}

class StringEventListener implements EventListener<String> {

    @Override
    public void on(@NotNull String event) throws Throwable {
        System.out.println("C: " + event);
    }
}
