/*
 *    hyper-director
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

package team.idealstate.hyper.director.api.component;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.director.api.DirectionContext;

/**
 * <p>ActionInterceptor</p>
 *
 * <p>创建于 2024/2/16 9:51</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface ActionInterceptor {

    ActionInterceptor DEFAULT = (context, action, arguments, depth, argument) -> false;

    default boolean onAccept(@NotNull DirectionContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, DirectionAction.ACCEPT, arguments, depth, argument);
    }

    default boolean onExecute(@NotNull DirectionContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, DirectionAction.EXECUTE, arguments, depth, argument);
    }

    default boolean onComplete(@NotNull DirectionContext context, @NotNull String[] arguments, int depth, @NotNull String argument) {
        return intercept(context, DirectionAction.COMPLETE, arguments, depth, argument);
    }

    boolean intercept(@NotNull DirectionContext context, @NotNull DirectionAction action, @NotNull String[] arguments, int depth, @NotNull String argument);

    enum DirectionAction {

        ACCEPT,
        EXECUTE,
        COMPLETE;
    }
}
