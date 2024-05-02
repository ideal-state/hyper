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

/**
 * <p>ActionInterceptor</p>
 *
 * <p>创建于 2024/2/16 9:51</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
@FunctionalInterface
public interface ActionInterceptor {

    ActionInterceptor DEFAULT = (context, action, arguments, depth, argument) -> false;

    default boolean onAccept(@NotNull CommandContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, CommandAction.ACCEPT, arguments, depth, argument);
    }

    default boolean onExecute(@NotNull CommandContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, CommandAction.EXECUTE, arguments, depth, argument);
    }

    default boolean onComplete(@NotNull CommandContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, CommandAction.COMPLETE, arguments, depth, argument);
    }

    boolean intercept(@NotNull CommandContext context, @NotNull CommandAction action, @NotNull String[] arguments, int depth, @NotNull String argument);

    enum CommandAction {

        ACCEPT,
        EXECUTE,
        COMPLETE;
    }
}
