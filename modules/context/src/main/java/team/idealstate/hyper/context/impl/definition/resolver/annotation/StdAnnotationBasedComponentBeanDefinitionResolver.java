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

package team.idealstate.hyper.context.impl.definition.resolver.annotation;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.string.StringUtils;
import team.idealstate.hyper.context.api.definition.BeanDefinition;
import team.idealstate.hyper.context.api.definition.BeanDefinitionResolver;
import team.idealstate.hyper.context.impl.definition.StdBeanDefinition;
import team.idealstate.hyper.context.impl.definition.resolver.exception.UnresolvableBeanDefinitionSourceException;

/**
 * <p>ComponentBasedBeanDefinitionResolver</p>
 *
 * <p>创建于 2024/6/26 上午9:06</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdAnnotationBasedComponentBeanDefinitionResolver implements BeanDefinitionResolver<Component> {

    @NotNull
    @Override
    public BeanDefinition resolve(@NotNull Component component, @NotNull String className) throws UnresolvableBeanDefinitionSourceException {
        AssertUtils.notNull(component, "无效的组件 Bean 注解");
        AssertUtils.notNullOrBlank(className, "无效的组件 Bean 类名");
        String name = component.value();
        if (StringUtils.isNullOrBlank(name)) {
            name = className;
        }
        return new StdBeanDefinition(name, className, component.scope());
    }
}
