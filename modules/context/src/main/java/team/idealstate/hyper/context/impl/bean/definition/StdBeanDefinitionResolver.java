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

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.string.StringUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinitionResolver;
import team.idealstate.hyper.context.api.bean.definition.exception.UnresolvableBeanDefinitionSourceException;
import team.idealstate.hyper.context.api.bean.definition.lifecycle.BeanDestroyer;
import team.idealstate.hyper.context.api.bean.definition.lifecycle.BeanInitializer;
import team.idealstate.hyper.context.api.bean.exception.BeanContextException;
import team.idealstate.hyper.context.impl.bean.annotation.Bean;
import team.idealstate.hyper.context.impl.bean.annotation.Component;
import team.idealstate.hyper.context.impl.bean.definition.lifecycle.StdAtBeanBeanConstructor;
import team.idealstate.hyper.context.impl.bean.definition.lifecycle.StdAtComponentBeanConstructor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p>StdBeanDefinitionResolver</p>
 *
 * <p>创建于 2024/6/28 下午4:40</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdBeanDefinitionResolver implements BeanDefinitionResolver<Class<?>> {

    @NotNull
    @Override
    public List<BeanDefinition> resolve(@NotNull Class<?> cls) throws UnresolvableBeanDefinitionSourceException {
        AssertUtils.notNull(cls, "无效的 Bean 定义来源");
        Component component = cls.getDeclaredAnnotation(Component.class);
        if (ObjectUtils.isNull(component)) {
            return ListUtils.emptyList();
        }
        String className = cls.getName();
        String componentName = component.value();
        if (StringUtils.isBlank(componentName)) {
            componentName = className;
        }
        try {
            cls.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new BeanContextException("类 '" + className + "' 不存在默认构造器", e);
        }
        List<BeanDefinition> ret = ListUtils.listOf(new StdBeanDefinition(
                componentName, className, component.scope(),
                component.lazy(),
                new StdAtComponentBeanConstructor(className),
                BeanInitializer.DEFAULT, BeanDestroyer.DEFAULT
        ));
        for (Method method : cls.getDeclaredMethods()) {
            Bean bean = method.getDeclaredAnnotation(Bean.class);
            if (ObjectUtils.isNull(bean)) {
                continue;
            }
            if (method.getParameterCount() != 0) {
                throw new BeanContextException("方法 '" + className + "#" + method.getName() + "' 必须是无参的");
            }
            Class<?> returnType = method.getReturnType();
            if (void.class.equals(returnType)) {
                throw new BeanContextException("方法 '" + className + "#" + method.getName() + "' 的返回类型不允许为 void");
            }
            String name = bean.value();
            if (StringUtils.isBlank(name)) {
                name = method.getName();
            }
            ret.add(new StdBeanDefinition(
                    name, returnType.getName(), bean.scope(), bean.lazy(),
                    new StdAtBeanBeanConstructor(
                            componentName, className, method.getName()
                    ),
                    BeanInitializer.DEFAULT, BeanDestroyer.DEFAULT
            ));
        }
        return ret;
    }
}
