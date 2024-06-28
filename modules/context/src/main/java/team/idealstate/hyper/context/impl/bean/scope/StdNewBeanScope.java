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

package team.idealstate.hyper.context.impl.bean.scope;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.context.api.bean.scope.BeanScope;

import java.util.List;

/**
 * <p>StdNewBeanScope</p>
 *
 * <p>创建于 2024/6/28 下午8:52</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdNewBeanScope implements BeanScope {

    @Override
    public void addEarlyBean(@NotNull String name, @NotNull Object object) {
        // noting to do
    }

    @Override
    public void addBean(@NotNull String name, @NotNull Object object) {
        // noting to do
    }

    @Override
    public <T> T getBean(@NotNull String name, @NotNull Class<T> type) {
        return null;
    }

    @Override
    public Object getBeanByName(@NotNull String name) {
        return null;
    }

    @Override
    public <T> T getBeanByType(@NotNull Class<T> type) {
        return null;
    }

    @NotNull
    @Override
    public <T> List<T> getBeansByType(@NotNull Class<T> type) {
        return ListUtils.emptyList();
    }
}
