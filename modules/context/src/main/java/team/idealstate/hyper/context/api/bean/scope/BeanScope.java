/*
 *    hyper-context
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

package team.idealstate.hyper.context.api.bean.scope;

import com.sun.istack.internal.Nullable;
import team.idealstate.hyper.common.annotation.lang.NotNull;

import java.util.List;

/**
 * <p>BeanScope</p>
 *
 * <p>创建于 2024/6/26 上午8:49</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface BeanScope {

    String NEW = "new";

    String SINGLETON = "singleton";

    String PROTOTYPE = "prototype";

    void addEarlyBean(@NotNull String name, @NotNull Object object);

    void addBean(@NotNull String name, @NotNull Object object);

    @Nullable
    <T> T getBean(@NotNull String name, @NotNull Class<T> type);

    @Nullable
    Object getBeanByName(@NotNull String name);

    @Nullable
    <T> T getBeanByType(@NotNull Class<T> type);

    @NotNull
    <T> List<T> getBeansByType(@NotNull Class<T> type);
}
