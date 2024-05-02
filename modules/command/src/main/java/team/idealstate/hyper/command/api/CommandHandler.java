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

package team.idealstate.hyper.command.api;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.command.api.detail.CommandDetailResolver;
import team.idealstate.hyper.command.api.exception.CommandException;
import team.idealstate.hyper.command.api.factory.CommandContextFactory;

import java.util.List;
import java.util.function.Consumer;

/**
 * <p>CommandHandler</p>
 *
 * <p>创建于 2024/3/27 7:10</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CommandHandler {
    void registerCommandLine(@NotNull CommandLine commandLine);

    void unregisterCommandLine(@NotNull String commandLineName);

    boolean execute(@NotNull String command) throws CommandException;

    boolean execute(@NotNull CommandDetail commandDetail) throws CommandException;

    boolean execute(@NotNull String command, @Nullable Consumer<CommandContext> prepContext) throws CommandException;

    boolean execute(@NotNull CommandDetail commandDetail, @Nullable Consumer<CommandContext> prepContext) throws CommandException;

    @NotNull
    List<String> complete(@NotNull String command) throws CommandException;

    @NotNull
    List<String> complete(@NotNull CommandDetail commandDetail) throws CommandException;

    @NotNull
    List<String> complete(@NotNull String command, @Nullable Consumer<CommandContext> prepContext) throws CommandException;

    @NotNull
    List<String> complete(@NotNull CommandDetail commandDetail, @Nullable Consumer<CommandContext> prepContext) throws CommandException;

    @NotNull
    CommandDetailResolver getCommandDetailResolver();

    void setCommandDetailResolver(@NotNull CommandDetailResolver commandDetailResolver);

    @NotNull
    CommandContextFactory getCommandContextFactory();

    void setCommandContextFactory(@NotNull CommandContextFactory commandContextFactory);
}
