/*
 *    hyper-common
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

package team.idealstate.hyper.common.resource;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.AssertUtils;

import java.io.Closeable;

/**
 * <p>ResourceUtils</p>
 *
 * <p>创建于 2024/3/26 20:19</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class ResourceUtils {

    public static <T extends Closeable> void use(@NotNull T resource, @NotNull ResourceConsumer<T> resourceConsumer) throws ResourceException {
        AssertUtils.notNull(resource, "无效的资源");
        AssertUtils.notNull(resourceConsumer, "无效的资源消费者");
        try (T ignored = resource) {
            resourceConsumer.accept(resource);
        } catch (Throwable e) {
            throw new ResourceException(e);
        }
    }


}
