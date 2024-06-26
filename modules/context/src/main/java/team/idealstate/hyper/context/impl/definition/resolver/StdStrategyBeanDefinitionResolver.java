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

package team.idealstate.hyper.context.impl.definition.resolver;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.context.api.definition.BeanDefinition;
import team.idealstate.hyper.context.api.definition.BeanDefinitionResolver;
import team.idealstate.hyper.context.impl.definition.resolver.exception.UnresolvableBeanDefinitionSourceException;

import java.util.Map;

/**
 * <p>StdStrategyBeanDefinitionResolver</p>
 *
 * <p>创建于 2024/6/26 上午8:59</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdStrategyBeanDefinitionResolver implements StrategyBeanDefinitionResolver {

    private final Map<Class<?>, BeanDefinitionResolver<?>> resolverMap = MapUtils.mapOf();

    @Override
    public <T> void register(@NotNull Class<T> sourceClass, @NotNull BeanDefinitionResolver<T> resolver) {
        AssertUtils.notNull(sourceClass, "无效的来源类");
        AssertUtils.notNull(resolver, "无效的 Bean 定义解析器");
        resolverMap.put(sourceClass,resolver);
    }

    @NotNull
    @Override
    @SuppressWarnings({"rawtypes","unchecked"})
    public BeanDefinition resolve(@NotNull Object source, @NotNull String className) throws UnresolvableBeanDefinitionSourceException {
        AssertUtils.notNull(source, "无效的来源对象");
        Class<?> sourceClass = source.getClass();
        BeanDefinitionResolver resolver = resolverMap.get(sourceClass);
        if (resolver == null) {
            throw new UnresolvableBeanDefinitionSourceException("无法解析 Bean 定义，因为没有找到对应的 Bean 定义解析器");
        }
        return resolver.resolve(source, className);
    }
}
