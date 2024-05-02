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

package team.idealstate.hyper.command.api.std.builder;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.command.api.builder.CommandLineBuilder;
import team.idealstate.hyper.command.api.factory.CommandLineFactory;
import team.idealstate.hyper.command.api.factory.CommandUnitFactory;

/**
 * <p>StdCommandBuilder</p>
 *
 * <p>创建于 2024/3/27 11:34</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class StdCommandBuilder {

    @NotNull
    public static CommandLineBuilder builder(@NotNull CommandLineFactory commandLineFactory, @NotNull CommandUnitFactory commandUnitFactory) {
        return new CommandLineBuilderImpl(commandLineFactory, commandUnitFactory);
    }
}
