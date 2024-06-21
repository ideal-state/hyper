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

package team.idealstate.hyper.director.impl;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.director.api.CommandContext;
import team.idealstate.hyper.director.api.CommandLine;
import team.idealstate.hyper.director.api.CommandResult;
import team.idealstate.hyper.director.api.CommandUnit;
import team.idealstate.hyper.director.api.exception.CommandException;
import team.idealstate.hyper.director.api.unit.*;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.array.ArrayUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>CommandLine</p>
 *
 * <p>创建于 2024/3/24 1:08</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandLineImpl implements CommandLine {

    private static final int COMPLETE_DIFF = 2;
    private static final int EXECUTE_DIFF = 1;
    private final Map<String, CommandUnit> commandUnits = MapUtils.linkedMapOf();
    private String name;
    private String description;

    public CommandLineImpl(@NotNull String name) {
        this(name, "");
    }

    public CommandLineImpl(@NotNull String name, @NotNull String description) {
        AssertUtils.notBlank(name, "无效的命令行名称");
        AssertUtils.notNull(description, "无效的命令行描述");
        this.name = name;
        this.description = description;
    }

    private static void doComplete(int diff, CommandResult<List<String>> result, List<? extends CommandUnit> commandUnits, CommandContext context, String[] arguments) throws CommandException {
        for (CommandUnit commandUnit : commandUnits) {
            final int depth = commandUnit.getDepth();
            if (depth > arguments.length - diff) {
                continue;
            }
            ActionInterceptor actionInterceptor = ObjectUtils.defaultIfNull(
                    commandUnit.getActionInterceptor(), ActionInterceptor.DEFAULT
            );
            if (actionInterceptor.onAccept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                result.setCode(CommandResult.Code.INTERRUPTED);
                continue;
            }
            ArgumentAcceptor argumentAcceptor = ObjectUtils.defaultIfNull(
                    commandUnit.getArgumentAcceptor(), ArgumentAcceptor.DEFAULT
            );
            if (!argumentAcceptor.accept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                continue;
            }
            result.setAcceptedDepth(depth);
            List<? extends CommandUnit> children = commandUnit.getChildren();
            final boolean childrenIsEmpty = CollectionUtils.isEmpty(children);
            if (arguments.length - depth == diff) {
                if (childrenIsEmpty) {
                    result.setData(ListUtils.emptyList());
                } else {
                    if (actionInterceptor.onComplete(
                            context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
                    )) {
                        result.setCode(CommandResult.Code.INTERRUPTED);
                        continue;
                    }
                    List<String> exampleList = ListUtils.linkedListOf();
                    final int childDepth = depth + 1;
                    for (CommandUnit child : children) {
                        if (child.getDepth() != childDepth) {
                            continue;
                        }
                        ExampleListProvider exampleListProvider = ObjectUtils.defaultIfNull(
                                child.getExampleListProvider(), ExampleListProvider.DEFAULT
                        );
                        CollectionUtils.addAll(exampleList, exampleListProvider.provide(
                                context, ArrayUtils.copyOf(arguments),
                                depth, arguments[depth], arguments[childDepth]
                        ));
                    }
                    if (CollectionUtils.isEmpty(exampleList)) {
                        result.setData(ListUtils.emptyList());
                    } else {
                        CommandCompleter commandCompleter = ObjectUtils.defaultIfNull(
                                commandUnit.getCommandCompleter(), CommandCompleter.DEFAULT
                        );
                        result.setData(commandCompleter.complete(
                                context, exampleList, ArrayUtils.copyOf(arguments),
                                depth, arguments[depth], arguments[childDepth]
                        ));
                    }
                }
                result.setCode(CommandResult.Code.SUCCESS);
                return;
            } else if (childrenIsEmpty) {
                continue;
            }
            doComplete(diff, result, children, context, arguments);
            if (CommandResult.Code.SUCCESS.equals(result.getCode())) {
                return;
            }
        }
    }

    private static void doExecute(int diff, CommandResult<Boolean> result, List<? extends CommandUnit> commandUnits, CommandContext context, String[] arguments) throws CommandException {
        for (CommandUnit commandUnit : commandUnits) {
            final int depth = commandUnit.getDepth();
            if (depth > arguments.length - diff) {
                continue;
            }
            ActionInterceptor actionInterceptor = ObjectUtils.defaultIfNull(
                    commandUnit.getActionInterceptor(), ActionInterceptor.DEFAULT
            );
            if (actionInterceptor.onAccept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                result.setCode(CommandResult.Code.INTERRUPTED);
                continue;
            }
            ArgumentAcceptor argumentAcceptor = ObjectUtils.defaultIfNull(
                    commandUnit.getArgumentAcceptor(), ArgumentAcceptor.DEFAULT
            );
            if (!argumentAcceptor.accept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                continue;
            }
            result.setAcceptedDepth(depth);
            if (arguments.length - depth == diff) {
                if (actionInterceptor.onExecute(
                        context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
                )) {
                    result.setCode(CommandResult.Code.INTERRUPTED);
                    continue;
                }
                CommandExecutor commandExecutor = commandUnit.getCommandExecutor();
                if (ObjectUtils.isNull(commandExecutor)) {
                    continue;
                }
                result.setData(commandExecutor.execute(
                        context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
                ));
                result.setCode(CommandResult.Code.SUCCESS);
                return;
            }
            List<? extends CommandUnit> children = commandUnit.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            doExecute(diff, result, children, context, arguments);
            if (CommandResult.Code.SUCCESS.equals(result.getCode())) {
                return;
            }
        }
    }

    @Override
    @NotNull
    public CommandResult<List<String>> complete(@NotNull CommandContext context, @NotNull String[] arguments) throws CommandException {
        AssertUtils.notNull(context, "无效的命令上下文");
        AssertUtils.notNull(arguments, "无效的命令行参数");
        CommandResult<List<String>> result = new CommandResult<>(CommandResult.Code.INVALID_COMMAND);
        if (ArrayUtils.isEmpty(arguments)) {
            return result;
        }
        doComplete(COMPLETE_DIFF, result, getCommandUnits(), context, arguments);
        return result;
    }

    @Override
    @NotNull
    public CommandResult<Boolean> execute(@NotNull CommandContext context, @NotNull String[] arguments) throws CommandException {
        AssertUtils.notNull(context, "无效的命令上下文");
        AssertUtils.notNull(arguments, "无效的命令行参数");
        CommandResult<Boolean> result = new CommandResult<>(CommandResult.Code.INVALID_COMMAND);
        if (ArrayUtils.isEmpty(arguments)) {
            return result;
        }
        doExecute(EXECUTE_DIFF, result, getCommandUnits(), context, arguments);
        return result;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的命令行名称");
        this.name = name;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        AssertUtils.notNull(description, "无效的命令行描述");
        this.description = description;
    }

    @Override
    @NotNull
    public List<CommandUnit> getCommandUnits() {
        if (MapUtils.isEmpty(commandUnits)) {
            return ListUtils.emptyList();
        }
        return ListUtils.listOf(commandUnits.values());
    }

    @Override
    public void addCommandUnit(@NotNull CommandUnit commandUnit) {
        AssertUtils.notNull(commandUnit, "无效的命令单元");
        commandUnits.put(commandUnit.getName(), commandUnit);
    }

    @Override
    public void removeCommandUnit(@NotNull String commandUnitName) {
        AssertUtils.notNull(commandUnitName, "无效的命令单元名称");
        commandUnits.remove(commandUnitName);
    }
}
