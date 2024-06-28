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
import team.idealstate.hyper.context.api.bean.definition.BeanConstructor;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;

/**
 * <p>StdBeanDefinition</p>
 *
 * <p>创建于 2024/6/26 上午9:22</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class StdBeanDefinition implements BeanDefinition {

    private final String name;
    private final String className;
    private final String scope;
    private final BeanConstructor constructor;

    public StdBeanDefinition(
            @NotNull String name,
            @NotNull String className,
            @NotNull String scope,
            @NotNull BeanConstructor constructor
    ) {
        AssertUtils.notNullOrBlank(name, "无效的 Bean 名称");
        AssertUtils.notNullOrBlank(className, "无效的 Bean 类名");
        AssertUtils.notNullOrBlank(scope, "无效的 Bean 作用域");
        AssertUtils.notNull(constructor, "无效的 Bean 构造器");
        this.name = name;
        this.className = className;
        this.scope = scope;
        this.constructor = constructor;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getClassName() {
        return className;
    }

    @NotNull
    @Override
    public String getScope() {
        return scope;
    }

    @NotNull
    @Override
    public BeanConstructor getConstructor() {
        return constructor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StdBeanDefinition)) {
            return false;
        }
        final StdBeanDefinition that = (StdBeanDefinition) object;

        if (!name.equals(that.name)) {
            return false;
        }
        if (!className.equals(that.className)) {
            return false;
        }
        if (!scope.equals(that.scope)) {
            return false;
        }
        return constructor.equals(that.constructor);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + className.hashCode();
        result = 31 * result + scope.hashCode();
        result = 31 * result + constructor.hashCode();
        return result;
    }
}
