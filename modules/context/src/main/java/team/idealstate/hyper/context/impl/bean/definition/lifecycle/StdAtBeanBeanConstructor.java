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

package team.idealstate.hyper.context.impl.bean.definition.lifecycle;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.reflect.ClassUtils;
import team.idealstate.hyper.context.api.bean.BeanContext;
import team.idealstate.hyper.context.api.bean.definition.lifecycle.BeanConstructor;
import team.idealstate.hyper.context.api.bean.exception.BeanContextException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>StdAtBeanBeanConstructor</p>
 *
 * <p>创建于 2024/6/29 下午3:57</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdAtBeanBeanConstructor implements BeanConstructor {

    private final String providerName;
    private final String providerClassName;
    private final String getterName;
    private Method constructor;

    public StdAtBeanBeanConstructor(
            @NotNull String providerName,
            @NotNull String providerClassName,
            @NotNull String getterName
    ) {
        AssertUtils.notNull(providerName, "无效的 Bean 提供者名称");
        AssertUtils.notNull(providerClassName, "无效的 Bean 提供者类名称");
        AssertUtils.notNull(getterName, "无效的 Bean 获取方法名称");
        this.providerName = providerName;
        this.providerClassName = providerClassName;
        this.getterName = getterName;
    }

    @NotNull
    @Override
    public Object constructBean(@NotNull BeanContext beanContext) {
        AssertUtils.notNull(beanContext, "无效的 Bean 上下文");
        try {
            Class<?> cls;
            try {
                cls = ClassUtils.forName(providerClassName, false, beanContext.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new BeanContextException("无法加载指定类，请检查 Bean 上下文中的类加载器", e);
            }
            Object providerObject = beanContext.getBean(providerName, cls);
            if (ObjectUtils.isNull(providerObject)) {
                throw new BeanContextException("无法找到 Bean 提供者对象");
            }
            if (ObjectUtils.isNull(constructor)) {
                try {
                    Method constructor = cls.getMethod(getterName);
                    constructor.setAccessible(true);
                    this.constructor = constructor;
                } catch (NoSuchMethodException e) {
                    throw new BeanContextException("类 '" + providerClassName + "' 不存在无参方法 '" + getterName + "'", e);
                }
            }
            return constructor.invoke(providerObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanContextException(e);
        }
    }
}
