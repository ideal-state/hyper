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

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.director.api.DirectionUnit;
import team.idealstate.hyper.director.api.component.*;

import java.util.List;
import java.util.Map;

/**
 * <p>StdDirectionUnit</p>
 *
 * <p>创建于 2024/3/23 21:46</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdDirectionUnit implements DirectionUnit {

    private final Map<String, DirectionUnit> children = MapUtils.linkedMapOf();
    private String name;
    private String description;
    private DirectionUnit parent;
    private ActionInterceptor actionInterceptor;
    private ArgumentAcceptor argumentAcceptor;
    private DirectionUnitCompleter directionUnitCompleter;
    private DirectionUnitExecutor directionUnitExecutor;
    private ExampleListProvider exampleListProvider;

    public StdDirectionUnit(@NotNull String name) {
        this(name, "");
    }

    public StdDirectionUnit(@NotNull String name, @NotNull String description) {
        AssertUtils.notBlank(name, "无效的指令单元名称");
        AssertUtils.notNull(description, "无效的指令单元描述");
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
        AssertUtils.notBlank(name, "无效的指令单元名称");
        this.name = name;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        AssertUtils.notNull(description, "无效的指令单元描述");
        this.description = description;
    }

    @Override
    @Nullable
    public DirectionUnit getParent() {
        return parent;
    }

    @Override
    public void setParent(@Nullable DirectionUnit directionUnit) {
        if (directionUnit == null || isRoot()) {
            this.parent = directionUnit;
            return;
        }
        throw new IllegalStateException("该指令单元已经拥有父指令单元了");
    }

    @Override
    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    @NotNull
    public List<? extends DirectionUnit> getChildren() {
        if (MapUtils.isEmpty(children)) {
            return ListUtils.emptyList();
        }
        return ListUtils.listOf(children.values());
    }

    @Override
    public int getDepth() {
        int depth = 0;
        DirectionUnit parent = getParent();
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
    public void addChild(@NotNull DirectionUnit directionUnit) {
        AssertUtils.notNull(directionUnit, "无效的指令单元");
        directionUnit.setParent(this);
        children.put(directionUnit.getName(), directionUnit);
    }

    @Override
    public void removeChild(@NotNull String directionUnitName) {
        AssertUtils.notNull(directionUnitName, "无效的指令单元名称");
        DirectionUnit directionUnit = children.remove(directionUnitName);
        if (directionUnit != null) {
            directionUnit.setParent(null);
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
    public DirectionUnitCompleter getDirectionCompleter() {
        return directionUnitCompleter;
    }

    @Override
    public void setDirectionCompleter(@Nullable DirectionUnitCompleter directionUnitCompleter) {
        this.directionUnitCompleter = directionUnitCompleter;
    }

    @Override
    @Nullable
    public DirectionUnitExecutor getDirectionExecutor() {
        return directionUnitExecutor;
    }

    @Override
    public void setDirectionExecutor(@Nullable DirectionUnitExecutor directionUnitExecutor) {
        this.directionUnitExecutor = directionUnitExecutor;
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
