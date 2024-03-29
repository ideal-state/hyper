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

package team.idealstate.hyper.command.api;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.command.api.exception.CommandException;

import java.util.List;

/**
 * <p>CommandLine</p>
 *
 * <p>创建于 2024/3/27 4:03</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CommandLine {
    @NotNull
    CommandResult<List<String>> complete(@NotNull CommandContext context, @NotNull String[] arguments) throws CommandException;

    @NotNull
    CommandResult<Boolean> execute(@NotNull CommandContext context, @NotNull String[] arguments) throws CommandException;

    @NotNull
    String getName();

    void setName(@NotNull String name);

    @NotNull
    String getDescription();

    void setDescription(@NotNull String description);

    @NotNull
    List<CommandUnit> getCommandUnits();

    void addCommandUnit(@NotNull CommandUnit commandUnit);

    void removeCommandUnit(@NotNull String commandUnitName);
}
