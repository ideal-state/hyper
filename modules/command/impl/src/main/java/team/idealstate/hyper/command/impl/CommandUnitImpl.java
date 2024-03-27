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

package team.idealstate.hyper.command.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.command.api.CommandUnit;
import team.idealstate.hyper.command.api.unit.*;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.template.ListUtils;
import team.idealstate.hyper.core.common.template.MapUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>CommandUnitImpl</p>
 *
 * <p>创建于 2024/3/23 21:46</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandUnitImpl implements CommandUnit {

    private final Map<String, CommandUnit> children = MapUtils.linkedMapOf();
    private String name;
    private String description;
    private CommandUnit parent;
    private ActionInterceptor actionInterceptor;
    private ArgumentAcceptor argumentAcceptor;
    private CommandCompleter commandCompleter;
    private CommandExecutor commandExecutor;
    private ExampleListProvider exampleListProvider;

    public CommandUnitImpl(@NotNull String name) {
        this(name, "");
    }

    public CommandUnitImpl(@NotNull String name, @NotNull String description) {
        AssertUtils.notBlank(name, "无效的命令单元名称");
        AssertUtils.notNull(description, "无效的命令单元描述");
        this.name = name;
        this.description = description;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的命令单元名称");
        this.name = name;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        AssertUtils.notNull(description, "无效的命令单元描述");
        this.description = description;
    }

    @Override
    @Nullable
    public CommandUnit getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable CommandUnit commandUnit) {
        if (commandUnit == null || isRoot()) {
            this.parent = commandUnit;
            return;
        }
        throw new IllegalStateException("该命令单元已经拥有父命令单元了");
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    @NotNull
    public List<? extends CommandUnit> getChildren() {
        if (MapUtils.isEmpty(children)) {
            return ListUtils.emptyList();
        }
        return ListUtils.listOf(children.values());
    }

    @Override
    public int getDepth() {
        int depth = 0;
        CommandUnit parent = getParent();
        while (parent != null) {
            if (parent.isRoot()) {
                break;
            }
            depth++;
            parent = parent.getParent();
        }
        return depth;
    }

    @Override
    public void addChild(@NotNull CommandUnit commandUnit) {
        AssertUtils.notNull(commandUnit, "无效的命令单元");
        commandUnit.setParent(this);
        children.put(commandUnit.getName(), commandUnit);
    }

    @Override
    public void removeChild(@NotNull String commandUnitName) {
        AssertUtils.notNull(commandUnitName, "无效的命令单元名称");
        CommandUnit commandUnit = children.remove(commandUnitName);
        if (commandUnit != null) {
            commandUnit.setParent(null);
        }
    }

    @Override
    @Nullable
    public ActionInterceptor getActionInterceptor() {
        return actionInterceptor;
    }

    @Override
    public void setActionInterceptor(@Nullable ActionInterceptor actionInterceptor) {
        this.actionInterceptor = actionInterceptor;
    }

    @Override
    @Nullable
    public ArgumentAcceptor getArgumentAcceptor() {
        return argumentAcceptor;
    }

    @Override
    public void setArgumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor) {
        this.argumentAcceptor = argumentAcceptor;
    }

    @Override
    @Nullable
    public CommandCompleter getCommandCompleter() {
        return commandCompleter;
    }

    @Override
    public void setCommandCompleter(@Nullable CommandCompleter commandCompleter) {
        this.commandCompleter = commandCompleter;
    }

    @Override
    @Nullable
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    @Override
    public void setCommandExecutor(@Nullable CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    @Nullable
    public ExampleListProvider getExampleListProvider() {
        return exampleListProvider;
    }

    @Override
    public void setExampleListProvider(@Nullable ExampleListProvider exampleListProvider) {
        this.exampleListProvider = exampleListProvider;
    }
}
