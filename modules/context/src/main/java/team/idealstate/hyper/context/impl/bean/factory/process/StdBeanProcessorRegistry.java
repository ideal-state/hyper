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

package team.idealstate.hyper.context.impl.bean.factory.process;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.SetUtils;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessor;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessorRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>StdBeanProcessorRegistry</p>
 *
 * <p>创建于 2024/6/28 下午9:14</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdBeanProcessorRegistry implements BeanProcessorRegistry {

    private final Set<BeanProcessor> beanProcessors = SetUtils.concurrentSetOf();
    @Override
    public void registerBeanProcessor(@NotNull BeanProcessor beanProcessor) {
        AssertUtils.notNull(beanProcessor, "无效的 Bean 处理器");
        beanProcessors.add(beanProcessor);
    }

    @Override
    public void registerBeanProcessors(@NotNull Collection<BeanProcessor> beanProcessors) {
        AssertUtils.notNull(beanProcessors, "无效的 Bean 处理器集合");
        this.beanProcessors.addAll(beanProcessors);
    }

    @NotNull
    @Override
    public List<BeanProcessor> getBeanProcessors() {
        if (CollectionUtils.isEmpty(beanProcessors)) {
            return ListUtils.emptyList();
        }
        return ListUtils.linkedListOf(beanProcessors);
    }
}
