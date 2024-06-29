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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>StdAtComponentBeanConstructor</p>
 *
 * <p>创建于 2024/6/29 下午3:38</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdAtComponentBeanConstructor implements BeanConstructor {

    private final String className;
    private Constructor<?> constructor;

    public StdAtComponentBeanConstructor(@NotNull String className) {
        AssertUtils.notNullOrBlank(className, "无效的类名称");
        this.className = className;
    }

    @NotNull
    @Override
    public Object constructBean(@NotNull BeanContext beanContext) {
        AssertUtils.notNull(beanContext, "无效的 Bean 上下文");
        try {
            if (ObjectUtils.isNull(constructor)) {
                try {
                    Class<?> cls = ClassUtils.forName(className, false, beanContext.getClassLoader());
                    Constructor<?> constructor = cls.getConstructor();
                    constructor.setAccessible(true);
                    this.constructor = constructor;
                } catch (ClassNotFoundException e) {
                    throw new BeanContextException("无法加载指定类，请检查 Bean 上下文中的类加载器", e);
                } catch (NoSuchMethodException e) {
                    throw new BeanContextException("类 '" + className + "' 不存在默认构造器", e);
                }
            }
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new BeanContextException(e);
        }
    }
}
