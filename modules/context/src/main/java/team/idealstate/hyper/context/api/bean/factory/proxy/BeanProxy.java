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

package team.idealstate.hyper.context.api.bean.factory.proxy;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.context.api.bean.BeanContext;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;

/**
 * <p>BeanProxy</p>
 *
 * <p>创建于 2024/6/29 上午11:53</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface BeanProxy {

    @NotNull
    Object proxyBean(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject, @NotNull Object proxyBeanObject);
}
