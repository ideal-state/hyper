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
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinition;
import team.idealstate.hyper.context.api.bean.definition.BeanDefinitionResolver;
import team.idealstate.hyper.context.api.bean.definition.exception.UnresolvableBeanDefinitionSourceException;

import java.io.File;
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
public final class StdBeanDefinitionResolver implements BeanDefinitionResolver<File> {
    @NotNull
    @Override
    public List<BeanDefinition> resolve(@NotNull File file) throws UnresolvableBeanDefinitionSourceException {
        AssertUtils.notNull(file, "无效的 Bean 定义来源");
        if (!(file.isFile() && file.getName().endsWith(".class"))) {
            return ListUtils.emptyList();
        }
        // TODO 解析 Bean 定义
        return null;
    }
}
