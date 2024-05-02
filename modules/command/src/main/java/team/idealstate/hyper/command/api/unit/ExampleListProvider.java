/*
 *    hyper-command
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

package team.idealstate.hyper.command.api.unit;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.command.api.CommandContext;

import java.util.Collections;
import java.util.List;

/**
 * <p>ExampleProvider</p>
 *
 * <p>创建于 2024/2/16 10:17</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
@FunctionalInterface
public interface ExampleListProvider {

    ExampleListProvider DEFAULT = (context, arguments, depth, argument, incomplete) -> Collections.emptyList();

    @NotNull
    List<String> provide(@NotNull CommandContext context, @NotNull String[] arguments,
                         int depth, @NotNull String argument, @NotNull String incomplete);
}
