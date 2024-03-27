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

package team.idealstate.hyper.command.api.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.command.api.CommandUnit;
import team.idealstate.hyper.command.api.builder.exception.NoSuchParentException;
import team.idealstate.hyper.command.api.unit.*;

import java.util.function.Consumer;

/**
 * <p>CommandUnitBuilder</p>
 *
 * <p>创建于 2024/3/27 8:07</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CommandUnitBuilder extends Builder<CommandUnit> {

    @NotNull
    CommandLineBuilder root();

    @NotNull
    CommandUnitBuilder parent() throws NoSuchParentException;

    @NotNull
    CommandUnitBuilder and() throws NoSuchParentException;

    @NotNull
    CommandUnitBuilder name(@NotNull String name);

    @NotNull
    CommandUnitBuilder description(@NotNull String description);

    @NotNull
    CommandUnitBuilder actionInterceptor(@Nullable ActionInterceptor actionInterceptor);

    @NotNull
    CommandUnitBuilder argumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor);

    @NotNull
    CommandUnitBuilder exampleListProvider(@Nullable ExampleListProvider exampleListProvider);

    @NotNull
    CommandUnitBuilder commandCompleter(@Nullable CommandCompleter commandCompleter);

    @NotNull
    CommandUnitBuilder commandExecutor(@Nullable CommandExecutor commandExecutor);

    @NotNull
    CommandUnitBuilder addChild(@NotNull String childUnitName);

    @NotNull
    CommandUnitBuilder addChild(@NotNull String childUnitName, @NotNull String childUnitDescription);

    @NotNull
    CommandUnitBuilder addChild(@NotNull Consumer<CommandUnitBuilder> childUnitBuilderConsumer);

    @NotNull
    CommandUnitBuilder removeChild(@NotNull String childUnitName);
}
