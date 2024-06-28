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

package team.idealstate.hyper.context.api.bean.factory;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.context.api.bean.BeanContext;
import team.idealstate.hyper.context.api.bean.aware.Aware;
import team.idealstate.hyper.context.api.bean.aware.BeanContextAware;
import team.idealstate.hyper.context.api.bean.aware.BeanDefinitionAware;
import team.idealstate.hyper.context.api.bean.aware.BeanNameAware;
import team.idealstate.hyper.context.api.bean.definition.BeanConstructor;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessor;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessorRegistry;
import team.idealstate.hyper.context.api.bean.scope.BeanScope;

import java.util.List;

/**
 * <p>AbstractBeanFactory</p>
 *
 * <p>创建于 2024/6/28 下午8:13</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractBeanFactory implements ConfigurableBeanFactory {

    private final BeanProcessorRegistry beanProcessorRegistry;

    protected AbstractBeanFactory(@NotNull BeanProcessorRegistry beanProcessorRegistry) {
        AssertUtils.notNull(beanProcessorRegistry, "无效的 Bean 处理器注册表");
        this.beanProcessorRegistry = beanProcessorRegistry;
    }

    @NotNull
    @Override
    public Object getBean(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition) {
        AssertUtils.notNull(beanContext, "无效的 Bean 上下文");
        AssertUtils.notNull(beanScope, "无效的 Bean 作用域");
        AssertUtils.notNull(beanDefinition, "无效的 Bean 定义");
        Object beanObject = construct(beanContext, beanDefinition);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");
        populateAware(beanContext, beanDefinition, beanObject);
        populateFields(beanContext, beanDefinition, beanObject);
        List<BeanProcessor> beanProcessors = getBeanProcessorRegistry().getBeanProcessors();
        beanObject = preProcess(beanProcessors, beanContext, beanDefinition, beanObject);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");
        applyToEarlyScope(beanContext, beanScope, beanDefinition, beanObject);
        beanObject = postProcess(beanProcessors, beanContext, beanDefinition, beanObject);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");
        applyToScope(beanContext, beanScope, beanDefinition, beanObject);
        return beanObject;
    }

    @NotNull
    protected Object construct(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition) {
        BeanConstructor beanConstructor = beanDefinition.getConstructor();
        AssertUtils.notNull(beanConstructor, "无效的 Bean 构造器");
        Object beanObject = beanConstructor.constructBean(beanContext);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");
        return beanObject;
    }

    protected void populateAware(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        if (beanObject instanceof Aware) {
            if (beanObject instanceof BeanContextAware) {
                ((BeanContextAware) beanObject).setBeanContext(beanContext);
            }
            if (beanObject instanceof BeanDefinitionAware) {
                ((BeanDefinitionAware) beanObject).setBeanDefinition(beanDefinition);
            }
            if (beanObject instanceof BeanNameAware) {
                ((BeanNameAware) beanObject).setBeanName(beanDefinition.getName());
            }
        }
    }

    protected abstract void populateFields(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject);

    @NotNull
    protected Object preProcess(@NotNull List<BeanProcessor> beanProcessors, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        for (BeanProcessor beanProcessor : beanProcessors) {
            beanObject = beanProcessor.preProcessBean(beanContext, beanDefinition, beanObject);
        }
        return beanObject;
    }

    protected void applyToEarlyScope(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        beanScope.addEarlyBean(beanDefinition.getName(), beanObject);
    }

    protected void applyToScope(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        beanScope.addBean(beanDefinition.getName(), beanObject);
    }

    @NotNull
    protected Object postProcess(@NotNull List<BeanProcessor> beanProcessors, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        for (BeanProcessor beanProcessor : beanProcessors) {
            beanObject = beanProcessor.postProcessBean(beanContext, beanDefinition, beanObject);
        }
        return beanObject;
    }

    @NotNull
    @Override
    public BeanProcessorRegistry getBeanProcessorRegistry() {
        return beanProcessorRegistry;
    }
}
