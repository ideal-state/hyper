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

package team.idealstate.hyper.command.impl;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.annotation.lang.Nullable;
import team.idealstate.hyper.command.api.*;
import team.idealstate.hyper.command.api.detail.CommandDetailResolver;
import team.idealstate.hyper.command.api.exception.CommandException;
import team.idealstate.hyper.command.api.exception.InterceptedCommandException;
import team.idealstate.hyper.command.api.exception.InvalidCommandException;
import team.idealstate.hyper.command.api.factory.CommandContextFactory;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.string.StringJoiner;
import team.idealstate.hyper.core.common.template.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>CommandHandler</p>
 *
 * <p>创建于 2024/3/27 4:43</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandHandlerImpl implements CommandHandler {

    private final Map<String, CommandLine> commandLineMap = MapUtils.linkedMapOf();
    private CommandDetailResolver commandDetailResolver;
    private CommandContextFactory commandContextFactory;

    public CommandHandlerImpl(@NotNull CommandDetailResolver commandDetailResolver, @NotNull CommandContextFactory commandContextFactory) {
        AssertUtils.notNull(commandDetailResolver, "无效的命令细节解析器");
        AssertUtils.notNull(commandContextFactory, "无效的命令上下文工厂");
        this.commandDetailResolver = commandDetailResolver;
        this.commandContextFactory = commandContextFactory;
    }

    private static void processResult(CommandDetail commandDetail, CommandResult.Code code, int acceptedDepth) {
        switch (code) {
            case SUCCESS:
                break;
            case INTERRUPTED:
                throw new InterceptedCommandException();
            case FAILURE:
            case INVALID_COMMAND:
            default:
                throw new InvalidCommandException(
                        buildInvalidCommandExceptionMessage(commandDetail, acceptedDepth)
                );
        }
    }

    private static String buildInvalidCommandExceptionMessage(CommandDetail commandDetail, int acceptedDepth) {
        if (acceptedDepth < 0) {
            return commandDetail.getName() + " <";
        }
        String[] arguments = commandDetail.getArguments();
        int end = Math.min(arguments.length, acceptedDepth);
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 0; i < end; i++) {
            joiner.append(arguments[i]);
        }
        return commandDetail.getName() + " " + joiner + " <";
    }

    @Override
    public void registerCommandLine(@NotNull CommandLine commandLine) {
        AssertUtils.notNull(commandLine, "无效的命令行");
        commandLineMap.put(commandLine.getName(), commandLine);
    }

    @Override
    public void unregisterCommandLine(@NotNull String commandLineName) {
        AssertUtils.notBlank(commandLineName, "无效的命令行名称");
        commandLineMap.remove(commandLineName);
    }

    @Override
    public boolean execute(@NotNull String command) throws CommandException {
        return execute(getCommandDetailResolver().resolve(command), null);
    }

    @Override
    public boolean execute(@NotNull CommandDetail commandDetail) throws CommandException {
        return execute(commandDetail, null);
    }

    @Override
    public boolean execute(@NotNull String command, @Nullable Consumer<CommandContext> prepContext) throws CommandException {
        return execute(getCommandDetailResolver().resolve(command), prepContext);
    }

    @Override
    public boolean execute(@NotNull CommandDetail commandDetail, @Nullable Consumer<CommandContext> prepContext) throws CommandException {
        AssertUtils.notNull(commandDetail, "无效的命令细节");
        String name = commandDetail.getName();
        CommandLine commandLine = commandLineMap.get(name);
        if (ObjectUtils.isNull(commandLine)) {
            throw new InvalidCommandException(
                    buildInvalidCommandExceptionMessage(commandDetail, -1)
            );
        }
        CommandContext commandContext = commandContextFactory.createCommandContext();
        AssertUtils.notNull(commandContext, "无效的命令上下文");
        if (ObjectUtils.isNotNull(prepContext)) {
            prepContext.accept(commandContext);
        }
        CommandResult<Boolean> executed = commandLine.execute(commandContext, commandDetail.getArguments());
        processResult(commandDetail, executed.getCode(), executed.getAcceptedDepth());
        Boolean data = executed.getData();
        if (ObjectUtils.isNull(data)) {
            throw new InvalidCommandException(
                    buildInvalidCommandExceptionMessage(commandDetail, executed.getAcceptedDepth())
            );
        }
        return data;
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull String command) throws CommandException {
        return complete(getCommandDetailResolver().resolve(command), null);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull CommandDetail commandDetail) throws CommandException {
        return complete(commandDetail, null);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull String command, @Nullable Consumer<CommandContext> prepContext) throws CommandException {
        return complete(getCommandDetailResolver().resolve(command), prepContext);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull CommandDetail commandDetail, @Nullable Consumer<CommandContext> prepContext) throws CommandException {
        AssertUtils.notNull(commandDetail, "无效的命令细节");
        String name = commandDetail.getName();
        CommandLine commandLine = commandLineMap.get(name);
        if (ObjectUtils.isNull(commandLine)) {
            throw new InvalidCommandException(
                    buildInvalidCommandExceptionMessage(commandDetail, -1)
            );
        }
        CommandContext commandContext = commandContextFactory.createCommandContext();
        AssertUtils.notNull(commandContext, "无效的命令上下文");
        if (ObjectUtils.isNotNull(prepContext)) {
            prepContext.accept(commandContext);
        }
        CommandResult<List<String>> completed = commandLine.complete(commandContext, commandDetail.getArguments());
        processResult(commandDetail, completed.getCode(), completed.getAcceptedDepth());
        List<String> data = completed.getData();
        if (ObjectUtils.isNull(data)) {
            throw new InvalidCommandException(
                    buildInvalidCommandExceptionMessage(commandDetail, completed.getAcceptedDepth())
            );
        }
        return data;
    }

    @Override
    @NotNull
    public CommandDetailResolver getCommandDetailResolver() {
        return commandDetailResolver;
    }

    @Override
    public void setCommandDetailResolver(@NotNull CommandDetailResolver commandDetailResolver) {
        AssertUtils.notNull(commandDetailResolver, "无效的命令细节解析器");
        this.commandDetailResolver = commandDetailResolver;
    }

    @Override
    @NotNull
    public CommandContextFactory getCommandContextFactory() {
        return commandContextFactory;
    }

    @Override
    public void setCommandContextFactory(@NotNull CommandContextFactory commandContextFactory) {
        AssertUtils.notNull(commandContextFactory, "无效的命令上下文工厂");
        this.commandContextFactory = commandContextFactory;
    }
}
