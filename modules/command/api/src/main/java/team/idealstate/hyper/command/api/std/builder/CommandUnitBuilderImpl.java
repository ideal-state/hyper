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

package team.idealstate.hyper.command.api.std.builder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.idealstate.hyper.command.api.CommandUnit;
import team.idealstate.hyper.command.api.builder.CommandLineBuilder;
import team.idealstate.hyper.command.api.builder.CommandUnitBuilder;
import team.idealstate.hyper.command.api.builder.exception.NoSuchParentException;
import team.idealstate.hyper.command.api.factory.CommandUnitFactory;
import team.idealstate.hyper.command.api.unit.*;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>CommandUnitBuilderImpl</p>
 *
 * <p>创建于 2024/3/27 8:40</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
final class CommandUnitBuilderImpl implements CommandUnitBuilder {

    private final CommandUnitFactory commandUnitFactory;
    private final CommandLineBuilder root;
    private final CommandUnitBuilder parent;
    private final List<CommandUnitBuilderImpl> children = ListUtils.linkedListOf();
    CommandUnit commandUnit;

    CommandUnitBuilderImpl(@NotNull CommandUnitFactory commandUnitFactory, @NotNull CommandLineBuilder root, @Nullable CommandUnitBuilder parent) {
        AssertUtils.notNull(commandUnitFactory, "无效的命令单元工厂");
        AssertUtils.notNull(root, "无效的根命令行建造器");
        AssertUtils.notNull(parent, "无效的父命令单元建造器");
        this.commandUnitFactory = commandUnitFactory;
        this.root = root;
        this.parent = parent;
    }

    private void throwInstanceIfNull() {
        AssertUtils.notNull(commandUnit, "命令单元未创建");
    }

    @Override
    public @NotNull CommandLineBuilder root() {
        return root;
    }

    @Override
    public @NotNull CommandUnitBuilder parent() throws NoSuchParentException {
        if (ObjectUtils.isNull(parent)) {
            throw new NoSuchParentException();
        }
        return parent;
    }

    @Override
    public @NotNull CommandUnitBuilder and() throws NoSuchParentException {
        return parent();
    }

    @Override
    public @NotNull CommandUnitBuilder name(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的命令单元名称");
        if (ObjectUtils.isNull(commandUnit)) {
            this.commandUnit = commandUnitFactory.createCommandUnit(name);
        } else {
            commandUnit.setName(name);
        }
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder description(@NotNull String description) {
        throwInstanceIfNull();
        AssertUtils.notNull(description, "无效的命令单元描述");
        commandUnit.setDescription(description);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder actionInterceptor(@Nullable ActionInterceptor actionInterceptor) {
        throwInstanceIfNull();
        commandUnit.setActionInterceptor(actionInterceptor);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder argumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor) {
        throwInstanceIfNull();
        commandUnit.setArgumentAcceptor(argumentAcceptor);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder exampleListProvider(@Nullable ExampleListProvider exampleListProvider) {
        throwInstanceIfNull();
        commandUnit.setExampleListProvider(exampleListProvider);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder commandCompleter(@Nullable CommandCompleter commandCompleter) {
        throwInstanceIfNull();
        commandUnit.setCommandCompleter(commandCompleter);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder commandExecutor(@Nullable CommandExecutor commandExecutor) {
        throwInstanceIfNull();
        commandUnit.setCommandExecutor(commandExecutor);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder addChild(@NotNull String childUnitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子命令单元名称");
        CommandUnitBuilderImpl childUnitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, root, this);
        children.add(childUnitBuilder);
        return childUnitBuilder.name(childUnitName);
    }

    @Override
    public @NotNull CommandUnitBuilder addChild(@NotNull String childUnitName, @NotNull String childUnitDescription) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子命令单元名称");
        AssertUtils.notNull(childUnitDescription, "无效的子命令单元描述");
        CommandUnitBuilderImpl childUnitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, root, this);
        children.add(childUnitBuilder);
        return childUnitBuilder.name(childUnitName).description(childUnitDescription);
    }

    @Override
    public @NotNull CommandUnitBuilder addChild(@NotNull Consumer<CommandUnitBuilder> childUnitBuilderConsumer) {
        throwInstanceIfNull();
        AssertUtils.notNull(childUnitBuilderConsumer, "无效的子命令单元建造器消费者");
        CommandUnitBuilderImpl childUnitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, root, this);
        children.add(childUnitBuilder);
        childUnitBuilderConsumer.accept(childUnitBuilder);
        return this;
    }

    @Override
    public @NotNull CommandUnitBuilder removeChild(@NotNull String childUnitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子命令单元名称");
        children.removeIf(childUnitBuilder -> childUnitName.equals(childUnitBuilder.commandUnit.getName()));
        return this;
    }

    @Override
    public @NotNull CommandUnit build() {
        throwInstanceIfNull();
        if (CollectionUtils.isNotEmpty(children)) {
            Iterator<CommandUnitBuilderImpl> iterator = children.iterator();
            while (iterator.hasNext()) {
                CommandUnitBuilderImpl childUnitBuilder = iterator.next();
                commandUnit.addChild(childUnitBuilder.build());
                iterator.remove();
            }
        }
        return commandUnit;
    }
}