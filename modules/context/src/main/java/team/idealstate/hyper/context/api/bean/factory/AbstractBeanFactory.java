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
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.context.api.bean.BeanContext;
import team.idealstate.hyper.context.api.bean.aware.*;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.api.bean.definition.lifecycle.BeanConstructor;
import team.idealstate.hyper.context.api.bean.definition.lifecycle.BeanInitializer;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessor;
import team.idealstate.hyper.context.api.bean.factory.process.BeanProcessorRegistry;
import team.idealstate.hyper.context.api.bean.factory.proxy.BeanProxy;
import team.idealstate.hyper.context.api.bean.factory.proxy.BeanProxyRegistry;
import team.idealstate.hyper.context.api.bean.inject.BeanPropertyInjector;
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

    private final BeanProxyRegistry beanProxyRegistry;
    private final BeanProcessorRegistry beanProcessorRegistry;
    private final BeanPropertyInjector beanPropertyInjector;

    protected AbstractBeanFactory(@NotNull BeanProxyRegistry beanProxyRegistry, @NotNull BeanProcessorRegistry beanProcessorRegistry, @NotNull BeanPropertyInjector beanPropertyInjector) {
        AssertUtils.notNull(beanProxyRegistry, "无效的 Bean 代理器注册表");
        AssertUtils.notNull(beanProcessorRegistry, "无效的 Bean 处理器注册表");
        AssertUtils.notNull(beanPropertyInjector, "无效的 Bean 属性注入器");
        this.beanProcessorRegistry = beanProcessorRegistry;
        this.beanProxyRegistry = beanProxyRegistry;
        this.beanPropertyInjector = beanPropertyInjector;
    }

    @NotNull
    @Override
    public Object getBean(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition) {
        AssertUtils.notNull(beanContext, "无效的 Bean 上下文");
        AssertUtils.notNull(beanScope, "无效的 Bean 作用域");
        AssertUtils.notNull(beanDefinition, "无效的 Bean 定义");

        Object beanObject = construct(beanContext, beanDefinition);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");

        Object proxyBeanObject = beanObject;
        List<BeanProxy> beanProxies = getBeanProxyRegistry().getBeanProxies();
        if (CollectionUtils.isNotEmpty(beanProxies)) {
            proxyBeanObject = proxy(beanProxies, beanContext, beanDefinition, beanObject);
        }

        applyToEarlyScope(beanContext, beanScope, beanDefinition, proxyBeanObject);

        populateAware(beanContext, beanDefinition, beanObject, proxyBeanObject);

        injectProperties(getBeanPropertyInjector(), beanContext, beanDefinition, beanObject);

        List<BeanProcessor> beanProcessors = getBeanProcessorRegistry().getBeanProcessors();
        if (CollectionUtils.isNotEmpty(beanProcessors)) {
            preProcess(beanProcessors, beanContext, beanDefinition, beanObject, proxyBeanObject);
        }

        initialize(beanContext, beanDefinition, beanObject, proxyBeanObject);

        applyToScope(beanContext, beanScope, beanDefinition, proxyBeanObject);

        postProcess(beanProcessors, beanContext, beanDefinition, beanObject, proxyBeanObject);

        return proxyBeanObject;
    }

    protected void initialize(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject, @NotNull Object proxyBeanObject) {
        BeanInitializer initializer = beanDefinition.getInitializer();
        if (ObjectUtils.isNull(initializer)) {
            return;
        }
        initializer.initialize(beanObject);
    }

    @NotNull
    protected Object construct(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition) {
        BeanConstructor beanConstructor = beanDefinition.getConstructor();
        AssertUtils.notNull(beanConstructor, "无效的 Bean 构造器");
        Object beanObject = beanConstructor.constructBean(beanContext);
        AssertUtils.notNull(beanObject, "无效的 Bean 对象");
        return beanObject;
    }

    protected void populateAware(@NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject, @NotNull Object proxyBeanObject) {
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
            if (beanObject instanceof BeanProxyAware) {
                ((BeanProxyAware) beanObject).setBeanProxy(proxyBeanObject);
            }
        }
    }

    @NotNull
    protected Object proxy(@NotNull List<BeanProxy> beanProxies, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        Object proxyBeanObject = beanObject;
        for (BeanProxy beanProxy : beanProxies) {
            proxyBeanObject = beanProxy.proxyBean(beanContext, beanDefinition, beanObject, proxyBeanObject);
        }
        return proxyBeanObject;
    }

    protected void injectProperties(@NotNull BeanPropertyInjector beanPropertyInjector, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        beanPropertyInjector.inject(beanContext, beanDefinition, beanObject);
    }

    protected void preProcess(@NotNull List<BeanProcessor> beanProcessors, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject, @NotNull Object proxyBeanObject) {
        for (BeanProcessor beanProcessor : beanProcessors) {
            beanProcessor.preProcessBean(beanContext, beanDefinition, beanObject, proxyBeanObject);
        }
    }

    protected void applyToEarlyScope(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        beanScope.addEarlyBean(beanDefinition.getName(), beanObject);
    }

    protected void applyToScope(@NotNull BeanContext beanContext, @NotNull BeanScope beanScope, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject) {
        beanScope.addBean(beanDefinition.getName(), beanObject);
    }

    protected void postProcess(@NotNull List<BeanProcessor> beanProcessors, @NotNull BeanContext beanContext, @NotNull BeanDefinition beanDefinition, @NotNull Object beanObject, @NotNull Object proxyBeanObject) {
        for (BeanProcessor beanProcessor : beanProcessors) {
            beanProcessor.postProcessBean(beanContext, beanDefinition, beanObject, proxyBeanObject);
        }
    }

    @NotNull
    @Override
    public BeanProxyRegistry getBeanProxyRegistry() {
        return beanProxyRegistry;
    }

    @NotNull
    @Override
    public BeanProcessorRegistry getBeanProcessorRegistry() {
        return beanProcessorRegistry;
    }

    @NotNull
    @Override
    public BeanPropertyInjector getBeanPropertyInjector() {
        return beanPropertyInjector;
    }
}
