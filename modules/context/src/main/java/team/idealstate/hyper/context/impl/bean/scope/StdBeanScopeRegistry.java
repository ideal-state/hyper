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

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.context.api.bean.scope.BeanScope;
import team.idealstate.hyper.context.api.bean.scope.BeanScopeRegistry;

import java.util.Map;

/**
 * <p>StdBeanScopeRegistry</p>
 *
 * <p>创建于 2024/6/28 下午5:22</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdBeanScopeRegistry implements BeanScopeRegistry {

    private final Map<String, BeanScope> beanScopesByName = MapUtils.concurrentMapOf();

    @Override
    public void registerBeanScope(@NotNull String name, @NotNull BeanScope beanScope) {
        AssertUtils.notNullOrEmpty(name, "无效的作用域名称");
        AssertUtils.notNull(beanScope, "无效的 Bean 作用域");
        beanScopesByName.put(name, beanScope);
    }

    @Nullable
    @Override
    public BeanScope getBeanScope(@NotNull String name) {
        AssertUtils.notNullOrEmpty(name, "无效的作用域名称");
        return beanScopesByName.get(name);
    }
}
