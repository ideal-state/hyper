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
import team.idealstate.hyper.common.array.ArrayUtils;
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.template.CollectionUtils;
import team.idealstate.hyper.common.template.ListUtils;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.director.api.Direction;
import team.idealstate.hyper.director.api.DirectionContext;
import team.idealstate.hyper.director.api.DirectionResult;
import team.idealstate.hyper.director.api.DirectionUnit;
import team.idealstate.hyper.director.api.component.*;
import team.idealstate.hyper.director.api.exception.DirectionException;

import java.util.List;
import java.util.Map;

/**
 * <p>StdDirection</p>
 *
 * <p>创建于 2024/3/24 1:08</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class StdDirection implements Direction {

    private static final int COMPLETE_DIFF = 2;
    private static final int EXECUTE_DIFF = 1;
    private final Map<String, DirectionUnit> directionUnits = MapUtils.linkedMapOf();
    private String name;
    private String description;

    public StdDirection(@NotNull String name) {
        this(name, "");
    }

    public StdDirection(@NotNull String name, @NotNull String description) {
        AssertUtils.notBlank(name, "无效的指令名称");
        AssertUtils.notNull(description, "无效的指令描述");
        this.name = name;
        this.description = description;
    }

    private static void doComplete(int diff, DirectionResult<List<String>> result, List<? extends DirectionUnit> directionUnits, DirectionContext context, String[] arguments) throws DirectionException {
        for (DirectionUnit directionUnit : directionUnits) {
            final int depth = directionUnit.getDepth();
            if (depth > arguments.length - diff) {
                continue;
            }
            ActionInterceptor actionInterceptor = ObjectUtils.defaultIfNull(
                    directionUnit.getActionInterceptor(), ActionInterceptor.DEFAULT
            );
            if (actionInterceptor.onAccept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                result.setCode(DirectionResult.Code.INTERRUPTED);
                continue;
            }
            ArgumentAcceptor argumentAcceptor = ObjectUtils.defaultIfNull(
                    directionUnit.getArgumentAcceptor(), ArgumentAcceptor.DEFAULT
            );
            if (!argumentAcceptor.accept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                continue;
            }
            result.setAcceptedDepth(depth);
            List<? extends DirectionUnit> children = directionUnit.getChildren();
            final boolean childrenIsEmpty = CollectionUtils.isEmpty(children);
            if (arguments.length - depth == diff) {
                if (childrenIsEmpty) {
                    result.setData(ListUtils.emptyList());
                } else {
                    if (actionInterceptor.onComplete(
                            context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
                    )) {
                        result.setCode(DirectionResult.Code.INTERRUPTED);
                        continue;
                    }
                    List<String> exampleList = ListUtils.linkedListOf();
                    final int childDepth = depth + 1;
                    for (DirectionUnit child : children) {
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
                        DirectionUnitCompleter directionUnitCompleter = ObjectUtils.defaultIfNull(
                                directionUnit.getDirectionCompleter(), DirectionUnitCompleter.DEFAULT
                        );
                        result.setData(directionUnitCompleter.complete(
                                context, exampleList, ArrayUtils.copyOf(arguments),
                                depth, arguments[depth], arguments[childDepth]
                        ));
                    }
                }
                result.setCode(DirectionResult.Code.SUCCESS);
                return;
            } else if (childrenIsEmpty) {
                continue;
            }
            doComplete(diff, result, children, context, arguments);
            if (DirectionResult.Code.SUCCESS.equals(result.getCode())) {
                return;
            }
        }
    }

    private static void doExecute(int diff, DirectionResult<Boolean> result, List<? extends DirectionUnit> directionUnits, DirectionContext context, String[] arguments) throws DirectionException {
        for (DirectionUnit directionUnit : directionUnits) {
            final int depth = directionUnit.getDepth();
            if (depth > arguments.length - diff) {
                continue;
            }
            ActionInterceptor actionInterceptor = ObjectUtils.defaultIfNull(
                    directionUnit.getActionInterceptor(), ActionInterceptor.DEFAULT
            );
            if (actionInterceptor.onAccept(
                    context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
            )) {
                result.setCode(DirectionResult.Code.INTERRUPTED);
                continue;
            }
            ArgumentAcceptor argumentAcceptor = ObjectUtils.defaultIfNull(
                    directionUnit.getArgumentAcceptor(), ArgumentAcceptor.DEFAULT
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
                    result.setCode(DirectionResult.Code.INTERRUPTED);
                    continue;
                }
                DirectionUnitExecutor directionUnitExecutor = directionUnit.getDirectionExecutor();
                if (ObjectUtils.isNull(directionUnitExecutor)) {
                    continue;
                }
                result.setData(directionUnitExecutor.execute(
                        context, ArrayUtils.copyOf(arguments), depth, arguments[depth]
                ));
                result.setCode(DirectionResult.Code.SUCCESS);
                return;
            }
            List<? extends DirectionUnit> children = directionUnit.getChildren();
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            doExecute(diff, result, children, context, arguments);
            if (DirectionResult.Code.SUCCESS.equals(result.getCode())) {
                return;
            }
        }
    }

    @Override
    @NotNull
    public DirectionResult<List<String>> complete(@NotNull DirectionContext context, @NotNull String[] arguments) throws DirectionException {
        AssertUtils.notNull(context, "无效的指令上下文");
        AssertUtils.notNull(arguments, "无效的指令参数");
        DirectionResult<List<String>> result = new DirectionResult<>(DirectionResult.Code.INVALID_COMMAND);
        if (ArrayUtils.isEmpty(arguments)) {
            return result;
        }
        doComplete(COMPLETE_DIFF, result, getDirectionUnits(), context, arguments);
        return result;
    }

    @Override
    @NotNull
    public DirectionResult<Boolean> execute(@NotNull DirectionContext context, @NotNull String[] arguments) throws DirectionException {
        AssertUtils.notNull(context, "无效的指令上下文");
        AssertUtils.notNull(arguments, "无效的指令参数");
        DirectionResult<Boolean> result = new DirectionResult<>(DirectionResult.Code.INVALID_COMMAND);
        if (ArrayUtils.isEmpty(arguments)) {
            return result;
        }
        doExecute(EXECUTE_DIFF, result, getDirectionUnits(), context, arguments);
        return result;
    }

    @Override
    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的指令名称");
        this.name = name;
    }

    @Override
    @NotNull
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(@NotNull String description) {
        AssertUtils.notNull(description, "无效的指令描述");
        this.description = description;
    }

    @Override
    @NotNull
    public List<DirectionUnit> getDirectionUnits() {
        if (MapUtils.isEmpty(directionUnits)) {
            return ListUtils.emptyList();
        }
        return ListUtils.listOf(directionUnits.values());
    }

    @Override
    public void addDirectionUnit(@NotNull DirectionUnit directionUnit) {
        AssertUtils.notNull(directionUnit, "无效的指令单元");
        directionUnits.put(directionUnit.getName(), directionUnit);
    }

    @Override
    public void removeDirectionUnit(@NotNull String directionUnitName) {
        AssertUtils.notNull(directionUnitName, "无效的指令单元名称");
        directionUnits.remove(directionUnitName);
    }
}
