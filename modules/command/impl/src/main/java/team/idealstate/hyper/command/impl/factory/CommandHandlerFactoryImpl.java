/*
 *    hyper-command-impl
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

package team.idealstate.hyper.command.impl.factory;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.command.api.CommandHandler;
import team.idealstate.hyper.command.api.factory.CommandContextFactory;
import team.idealstate.hyper.command.api.factory.CommandHandlerFactory;
import team.idealstate.hyper.command.api.std.detail.StdCommandDetailResolver;
import team.idealstate.hyper.command.impl.CommandHandlerImpl;

/**
 * <p>CommandHandlerFactoryImpl</p>
 *
 * <p>创建于 2024/3/27 12:25</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandHandlerFactoryImpl implements CommandHandlerFactory {
    @Override
    public @NotNull CommandHandler createCommandHandler(@NotNull CommandContextFactory commandContextFactory) {
        return new CommandHandlerImpl(new StdCommandDetailResolver(), commandContextFactory);
    }
}
