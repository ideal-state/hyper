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

package team.idealstate.hyper.context.impl.bean.definition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.common.template.SetUtils;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinitionRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>StdBeanDefinitionRegistry</p>
 *
 * <p>创建于 2024/6/28 上午11:27</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdBeanDefinitionRegistry implements BeanDefinitionRegistry {

    private static final Logger logger = LogManager.getLogger(StdBeanDefinitionRegistry.class);

    private static final long DEFAULT_TIMEOUT = 3L;

    private OverridePolicy overridePolicy = OverridePolicy.DEFAULT;
    private final Map<String, BeanDefinition> byName = MapUtils.linkedMapOf();
    private final Map<String, Set<BeanDefinition>> byClassName = MapUtils.linkedMapOf();
    private final Lock lock = new ReentrantLock();

    @Override
    public void setOverridePolicy(@NotNull OverridePolicy overridePolicy) {
        AssertUtils.notNull(overridePolicy, "无效的覆盖策略");
        this.overridePolicy = overridePolicy;
    }

    private void putByName(String name, BeanDefinition beanDefinition) {
        byName.put(name, beanDefinition);
    }

    private void putByClassName(String className, BeanDefinition beanDefinition) {
        Set<BeanDefinition> beanDefinitions = byClassName.get(className);
        if (ObjectUtils.isNotNull(beanDefinitions)) {
            beanDefinitions.add(beanDefinition);
        } else {
            byClassName.put(className, SetUtils.linkedSetOf(beanDefinition));
        }
    }

    private void doRegisterBeanDefinition(BeanDefinition beanDefinition) {
        if (ObjectUtils.isNull(beanDefinition)) {
            return;
        }
        String name = beanDefinition.getName();
        String className = beanDefinition.getClassName();
        BeanDefinition old = byName.get(name);
        if (ObjectUtils.isNotNull(old)) {
            if (
                    ObjectUtils.isNotEquals(old, beanDefinition) &&
                            overridePolicy.shouldOverride(old, beanDefinition)
            ) {
                putByName(name, beanDefinition);
                putByClassName(className, beanDefinition);
            }
        } else {
            putByName(name, beanDefinition);
            putByClassName(className, beanDefinition);
        }
    }

    @Override
    public void registerBeanDefinition(@NotNull BeanDefinition beanDefinition) {
        AssertUtils.notNull(beanDefinition, "无效的 Bean 定义");
        try {
            if (!lock.tryLock(DEFAULT_TIMEOUT, TimeUnit.SECONDS)) {
                logger.error("系统繁忙，注册操作已超时");
                return;
            }
            try {
                doRegisterBeanDefinition(beanDefinition);
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("已取消注册操作，线程已中断");
        }
    }

    @Override
    public void registerBeanDefinitions(@NotNull Collection<BeanDefinition> beanDefinitions) {
        AssertUtils.notNull(beanDefinitions, "无效的 Bean 定义集合");
        if (CollectionUtils.isEmpty(beanDefinitions)) {
            return;
        }
        try {
            if (!lock.tryLock(DEFAULT_TIMEOUT, TimeUnit.SECONDS)) {
                logger.error("系统繁忙，注册操作已超时");
                return;
            }
            try {
                for (BeanDefinition beanDefinition : beanDefinitions) {
                    doRegisterBeanDefinition(beanDefinition);
                }
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("已取消注册操作，线程已中断");
        }
    }

    @Nullable
    @Override
    public BeanDefinition getBeanDefinitionByName(@NotNull String name) {
        return byName.get(name);
    }

    @NotNull
    @Override
    public List<BeanDefinition> getBeanDefinitionByClassName(@NotNull String className) {
        Set<BeanDefinition> beanDefinitions = byClassName.get(className);
        if (CollectionUtils.isNullOrEmpty(beanDefinitions)) {
            return ListUtils.emptyList();
        }
        return ListUtils.linkedListOf(beanDefinitions);
    }
}
