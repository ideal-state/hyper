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

package team.idealstate.hyper.context.impl.bean.factory.proxy;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.SetUtils;
import team.idealstate.hyper.context.api.bean.factory.proxy.BeanProxy;
import team.idealstate.hyper.context.api.bean.factory.proxy.BeanProxyRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>StdBeanProxyRegistry</p>
 *
 * <p>创建于 2024/6/29 下午1:53</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdBeanProxyRegistry implements BeanProxyRegistry {

    private final Set<BeanProxy> beanProxies = SetUtils.concurrentSetOf();

    @Override
    public void registerBeanProxy(@NotNull BeanProxy beanProxy) {
        AssertUtils.notNull(beanProxy, "无效的 Bean 代理器");
        beanProxies.add(beanProxy);
    }

    @Override
    public void registerBeanProxies(@NotNull Collection<BeanProxy> beanProxies) {
        AssertUtils.notNull(beanProxies, "无效的 Bean 代理器集合");
        this.beanProxies.addAll(beanProxies);
    }

    @NotNull
    @Override
    public List<BeanProxy> getBeanProxies() {
        if (CollectionUtils.isEmpty(beanProxies)) {
            return Collections.emptyList();
        }
        return ListUtils.linkedListOf(beanProxies);
    }
}
