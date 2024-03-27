/*
 *    hyper-command-api
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

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.command.api.CommandContext;

/**
 * <p>ArgumentAcceptor</p>
 *
 * <p>创建于 2024/2/16 9:48</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
@FunctionalInterface
public interface ArgumentAcceptor {

    ArgumentAcceptor DEFAULT = (context, arguments, depth, argument) -> false;

    boolean accept(@NotNull CommandContext context, @NotNull String[] arguments, int depth, @NotNull String argument);
}
