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

package team.idealstate.hyper.context.api.bean;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinitionRegistry;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinitionResolver;
import team.idealstate.hyper.context.api.bean.scope.BeanScopeRegistry;

import java.util.Collection;

/**
 * <p>ConfigurableBeanContext</p>
 *
 * <p>创建于 2024/6/28 下午3:50</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ConfigurableBeanContext extends BeanContext {

    void addBeanDefinitionSource(@NotNull Object source);

    void addBeanDefinitionSources(@NotNull Collection<Object> sources);

    void addBeanDefinitionResolver(@NotNull BeanDefinitionResolver<?> resolver);

    void addBeanDefinitionResolvers(@NotNull Collection<BeanDefinitionResolver<?>> resolvers);

    @NotNull
    BeanDefinitionRegistry getBeanDefinitionRegistry();

    @NotNull
    BeanScopeRegistry getBeanScopeRegistry();

    @NotNull
    BeanScopeRegistry getBeanFactoryRegistry();
}
