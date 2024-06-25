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
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.director.api.Direction;
import team.idealstate.hyper.director.api.factory.DirectionFactory;
import team.idealstate.hyper.director.api.factory.DirectionUnitFactory;
import team.idealstate.hyper.director.builder.api.DirectionBuilder;
import team.idealstate.hyper.director.builder.api.DirectionUnitBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * <p>DirectionBuilderImpl</p>
 *
 * <p>创建于 2024/3/27 8:41</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
final class StdDirectionBuilder implements DirectionBuilder {

    private final DirectionFactory directionFactory;
    private final DirectionUnitFactory directionUnitFactory;
    private final List<StdDirectionUnitBuilder> unitBuilders = ListUtils.linkedListOf();
    private Direction direction;

    StdDirectionBuilder(@NotNull DirectionFactory directionFactory, @NotNull DirectionUnitFactory directionUnitFactory) {
        AssertUtils.notNull(directionFactory, "无效的指令工厂");
        AssertUtils.notNull(directionUnitFactory, "无效的指令单元工厂");
        this.directionFactory = directionFactory;
        this.directionUnitFactory = directionUnitFactory;
    }

    private void throwInstanceIfNull() {
        AssertUtils.notNull(direction, "指令未创建");
    }

    @Override
    @NotNull
    public DirectionBuilder name(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的指令名称");
        if (ObjectUtils.isNull(direction)) {
            this.direction = directionFactory.createDirection(name);
        } else {
            direction.setName(name);
        }
        return this;
    }

    @Override
    @NotNull
    public DirectionBuilder description(@NotNull String description) {
        throwInstanceIfNull();
        AssertUtils.notNull(description, "无效的指令描述");
        direction.setDescription(description);
        return this;
    }

    @Override
    @NotNull
    public DirectionUnitBuilder addDirectionUnit(@NotNull String unitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的指令单元名称");
        StdDirectionUnitBuilder unitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        return unitBuilder.name(unitName);
    }

    @Override
    @NotNull
    public DirectionUnitBuilder addDirectionUnit(@NotNull String unitName, @NotNull String unitDescription) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的指令单元名称");
        AssertUtils.notNull(unitDescription, "无效的指令单元描述");
        StdDirectionUnitBuilder unitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        return unitBuilder.name(unitName).description(unitDescription);
    }

    @Override
    @NotNull
    public DirectionBuilder addDirectionUnit(@NotNull Consumer<DirectionUnitBuilder> unitBuilderConsumer) {
        throwInstanceIfNull();
        AssertUtils.notNull(unitBuilderConsumer, "无效的指令单元建造器消费者");
        StdDirectionUnitBuilder unitBuilder = new StdDirectionUnitBuilder(directionUnitFactory, this, null);
        unitBuilders.add(unitBuilder);
        unitBuilderConsumer.accept(unitBuilder);
        return this;
    }

    @Override
    @NotNull
    public DirectionBuilder removeDirectionUnit(@NotNull String unitName) {
        throwInstanceIfNull();
        AssertUtils.notBlank(unitName, "无效的指令单元名称");
        unitBuilders.removeIf(unitBuilder -> unitName.equals(unitBuilder.directionUnit.getName()));
        return this;
    }

    @Override
    @NotNull
    public Direction build() {
        throwInstanceIfNull();
        if (CollectionUtils.isNotEmpty(unitBuilders)) {
            Iterator<StdDirectionUnitBuilder> iterator = unitBuilders.iterator();
            while (iterator.hasNext()) {
                StdDirectionUnitBuilder unitBuilder = iterator.next();
                direction.addDirectionUnit(unitBuilder.build());
                iterator.remove();
            }
        }
        return direction;
    }
}
