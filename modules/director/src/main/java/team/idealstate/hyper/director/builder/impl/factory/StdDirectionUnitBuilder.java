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

package team.idealstate.hyper.director.builder.impl.factory;

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.director.api.DirectionUnit;
import team.idealstate.hyper.director.api.component.*;
import team.idealstate.hyper.director.api.factory.DirectionUnitFactory;
import team.idealstate.hyper.director.builder.api.DirectionBuilder;
import team.idealstate.hyper.director.builder.api.DirectionUnitBuilder;
import team.idealstate.hyper.director.builder.api.exception.NoSuchParentException;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>DirectionUnitBuilderImpl</p>
 *
 * <p>创建于 2024/3/27 8:40</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class StdDirectionUnitBuilder implements DirectionUnitBuilder {

    private final DirectionUnitFactory directionUnitFactory;
    private final DirectionBuilder root;
    private final DirectionUnitBuilder parent;
    private final List<StdDirectionUnitBuilder> children = ListUtils.linkedListOf();
    DirectionUnit directionUnit;

    StdDirectionUnitBuilder(@NotNull DirectionUnitFactory directionUnitFactory, @NotNull DirectionBuilder root, @Nullable DirectionUnitBuilder parent) {
        AssertUtils.notNull(directionUnitFactory, "无效的指令单元工厂");
        AssertUtils.notNull(root, "无效的根指令建造器");
        AssertUtils.notNull(parent, "无效的父指令单元建造器");
        this.directionUnitFactory = directionUnitFactory;
        this.root = root;
        this.parent = parent;
    }

    private void throwInstanceIfNull() {
        AssertUtils.notNull(directionUnit, "指令单元未创建");
    }

    @Override
    @NotNull
    public DirectionBuilder root() {
        return root;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder parent() throws NoSuchParentException {
        if (ObjectUtils.isNull(parent)) {
            throw new NoSuchParentException();
        }
        return parent;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder and() throws NoSuchParentException {
        return parent();
    }

    @Override
    @NotNull
    public DirectionUnitBuilder name(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的指令单元名称");
        if (ObjectUtils.isNull(directionUnit)) {
            this.directionUnit = directionUnitFactory.createDirectionUnit(name);
        } else {
            directionUnit.setName(name);
        }
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder description(@NotNull String description) {
        throwInstanceIfNull();
        AssertUtils.notNull(description, "无效的指令单元描述");
        directionUnit.setDescription(description);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder actionInterceptor(@Nullable ActionInterceptor actionInterceptor) {
        throwInstanceIfNull();
        directionUnit.setActionInterceptor(actionInterceptor);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder argumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor) {
        throwInstanceIfNull();
        directionUnit.setArgumentAcceptor(argumentAcceptor);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder exampleListProvider(@Nullable ExampleListProvider exampleListProvider) {
        throwInstanceIfNull();
        directionUnit.setExampleListProvider(exampleListProvider);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder directionCompleter(@Nullable DirectionUnitCompleter directionUnitCompleter) {
        throwInstanceIfNull();
        directionUnit.setDirectionCompleter(directionUnitCompleter);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder directionExecutor(@Nullable DirectionUnitExecutor directionUnitExecutor) {
        throwInstanceIfNull();
        directionUnit.setDirectionExecutor(directionUnitExecutor);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder addChild(@NotNull String childUnitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子指令单元名称");
        StdDirectionUnitBuilder childUnitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, root, this);
        children.add(childUnitBuilder);
        return childUnitBuilder.name(childUnitName);
    }

    @Override
    @NotNull
    public DirectionUnitBuilder addChild(@NotNull String childUnitName, @NotNull String childUnitDescription) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子指令单元名称");
        AssertUtils.notNull(childUnitDescription, "无效的子指令单元描述");
        StdDirectionUnitBuilder childUnitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, root, this);
        children.add(childUnitBuilder);
        return childUnitBuilder.name(childUnitName).description(childUnitDescription);
    }

    @Override
    @NotNull
    public DirectionUnitBuilder addChild(@NotNull Consumer<DirectionUnitBuilder> childUnitBuilderConsumer) {
        throwInstanceIfNull();
        AssertUtils.notNull(childUnitBuilderConsumer, "无效的子指令单元建造器消费者");
        StdDirectionUnitBuilder childUnitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, root, this);
        children.add(childUnitBuilder);
        childUnitBuilderConsumer.accept(childUnitBuilder);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder removeChild(@NotNull String childUnitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(childUnitName, "无效的子指令单元名称");
        children.removeIf(childUnitBuilder -> childUnitName.equals(childUnitBuilder.directionUnit.getName()));
        return this;
    }

    @Override
    @NotNull
    public DirectionUnit build() {
        throwInstanceIfNull();
        if (CollectionUtils.isNotEmpty(children)) {
            Iterator<StdDirectionUnitBuilder> iterator = children.iterator();
            while (iterator.hasNext()) {
                StdDirectionUnitBuilder childUnitBuilder = iterator.next();
                directionUnit.addChild(childUnitBuilder.build());
                iterator.remove();
            }
        }
        return directionUnit;
    }
}
