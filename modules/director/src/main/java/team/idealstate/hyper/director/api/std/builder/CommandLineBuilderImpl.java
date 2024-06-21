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

package team.idealstate.hyper.director.api.std.builder;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.director.api.CommandLine;
import team.idealstate.hyper.director.api.builder.CommandLineBuilder;
import team.idealstate.hyper.director.api.builder.CommandUnitBuilder;
import team.idealstate.hyper.director.api.factory.CommandLineFactory;
import team.idealstate.hyper.director.api.factory.CommandUnitFactory;
import team.idealstate.hyper.core.common.AssertUtils;
import team.idealstate.hyper.core.common.object.ObjectUtils;
import team.idealstate.hyper.core.common.template.CollectionUtils;
import team.idealstate.hyper.core.common.template.ListUtils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>CommandLineBuilderImpl</p>
 *
 * <p>创建于 2024/3/27 8:41</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
final class CommandLineBuilderImpl implements CommandLineBuilder {

    private final CommandLineFactory commandLineFactory;
    private final CommandUnitFactory commandUnitFactory;
    private final List<CommandUnitBuilderImpl> unitBuilders = ListUtils.linkedListOf();
    private CommandLine commandLine;

    CommandLineBuilderImpl(@NotNull CommandLineFactory commandLineFactory, @NotNull CommandUnitFactory commandUnitFactory) {
        AssertUtils.notNull(commandLineFactory, "无效的命令行工厂");
        AssertUtils.notNull(commandUnitFactory, "无效的命令单元工厂");
        this.commandLineFactory = commandLineFactory;
        this.commandUnitFactory = commandUnitFactory;
    }

    private void throwInstanceIfNull() {
        AssertUtils.notNull(commandLine, "命令行未创建");
    }

    @Override
    @NotNull
    public CommandLineBuilder name(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的命令行名称");
        if (ObjectUtils.isNull(commandLine)) {
            this.commandLine = commandLineFactory.createCommandLine(name);
        } else {
            commandLine.setName(name);
        }
        return this;
    }

    @Override
    @NotNull
    public CommandLineBuilder description(@NotNull String description) {
        throwInstanceIfNull();
        AssertUtils.notNull(description, "无效的命令行描述");
        commandLine.setDescription(description);
        return this;
    }

    @Override
    @NotNull
    public CommandUnitBuilder addCommandUnit(@NotNull String unitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的命令单元名称");
        CommandUnitBuilderImpl unitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        return unitBuilder.name(unitName);
    }

    @Override
    @NotNull
    public CommandUnitBuilder addCommandUnit(@NotNull String unitName, @NotNull String unitDescription) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的命令单元名称");
        AssertUtils.notNull(unitDescription, "无效的命令单元描述");
        CommandUnitBuilderImpl unitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        return unitBuilder.name(unitName).description(unitDescription);
    }

    @Override
    @NotNull
    public CommandLineBuilder addCommandUnit(@NotNull Consumer<CommandUnitBuilder> unitBuilderConsumer) {
        throwInstanceIfNull();
        AssertUtils.notNull(unitBuilderConsumer, "无效的命令单元建造器消费者");
        CommandUnitBuilderImpl unitBuilder = new CommandUnitBuilderImpl(commandUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        unitBuilderConsumer.accept(unitBuilder);
        return this;
    }

    @Override
    @NotNull
    public CommandLineBuilder removeCommandUnit(@NotNull String unitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的命令单元名称");
        unitBuilders.removeIf(unitBuilder -> unitName.equals(unitBuilder.commandUnit.getName()));
        return this;
    }

    @Override
    @NotNull
    public CommandLine build() {
        throwInstanceIfNull();
        if (CollectionUtils.isNotEmpty(unitBuilders)) {
            Iterator<CommandUnitBuilderImpl> iterator = unitBuilders.iterator();
            while (iterator.hasNext()) {
                CommandUnitBuilderImpl unitBuilder = iterator.next();
                commandLine.addCommandUnit(unitBuilder.build());
                iterator.remove();
            }
        }
        return commandLine;
    }
}
