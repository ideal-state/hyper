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
import team.idealstate.hyper.common.object.ObjectUtils;
import team.idealstate.hyper.common.string.StringJoiner;
import team.idealstate.hyper.common.template.MapUtils;
import team.idealstate.hyper.director.api.*;
import team.idealstate.hyper.director.api.exception.DirectionException;
import team.idealstate.hyper.director.api.exception.InterceptedDirectionException;
import team.idealstate.hyper.director.api.exception.InvalidDirectionException;
import team.idealstate.hyper.director.api.factory.DirectionContextFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>StdDirector</p>
 *
 * <p>创建于 2024/3/27 4:43</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class StdDirector implements Director {

    private final Map<String, Direction> directionMap = MapUtils.linkedMapOf();
    private DirectionDetailResolver directionDetailResolver;
    private DirectionContextFactory directionContextFactory;

    public StdDirector(@NotNull DirectionDetailResolver directionDetailResolver, @NotNull DirectionContextFactory directionContextFactory) {
        AssertUtils.notNull(directionDetailResolver, "无效的指令细节解析器");
        AssertUtils.notNull(directionContextFactory, "无效的指令上下文工厂");
        this.directionDetailResolver = directionDetailResolver;
        this.directionContextFactory = directionContextFactory;
    }

    private static void processResult(DirectionDetail directionDetail, DirectionResult.Code code, int acceptedDepth) {
        switch (code) {
            case SUCCESS:
                break;
            case INTERRUPTED:
                throw new InterceptedDirectionException();
            case FAILURE:
            case INVALID_COMMAND:
            default:
                throw new InvalidDirectionException(
                        buildInvalidDirectionExceptionMessage(directionDetail, acceptedDepth)
                );
        }
    }

    private static String buildInvalidDirectionExceptionMessage(DirectionDetail directionDetail, int acceptedDepth) {
        if (acceptedDepth < 0) {
            return directionDetail.getName() + " <";
        }
        String[] arguments = directionDetail.getArguments();
        int end = Math.min(arguments.length, acceptedDepth);
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 0; i < end; i++) {
            joiner.append(arguments[i]);
        }
        return directionDetail.getName() + " " + joiner + " <";
    }

    @Override
    public void registerDirection(@NotNull Direction direction) {
        AssertUtils.notNull(direction, "无效的指令");
        directionMap.put(direction.getName(), direction);
    }

    @Override
    public void unregisterDirection(@NotNull String directionName) {
        AssertUtils.notBlank(directionName, "无效的指令名称");
        directionMap.remove(directionName);
    }

    @Override
    public boolean execute(@NotNull String direction) throws DirectionException {
        return execute(getDirectionDetailResolver().resolve(direction), null);
    }

    @Override
    public boolean execute(@NotNull DirectionDetail directionDetail) throws DirectionException {
        return execute(directionDetail, null);
    }

    @Override
    public boolean execute(@NotNull String direction, @Nullable Consumer<DirectionContext> prepContext) throws DirectionException {
        return execute(getDirectionDetailResolver().resolve(direction), prepContext);
    }

    @Override
    public boolean execute(@NotNull DirectionDetail directionDetail, @Nullable Consumer<DirectionContext> prepContext) throws DirectionException {
        AssertUtils.notNull(directionDetail, "无效的指令细节");
        String name = directionDetail.getName();
        Direction direction = directionMap.get(name);
        if (ObjectUtils.isNull(direction)) {
            throw new InvalidDirectionException(
                    buildInvalidDirectionExceptionMessage(directionDetail, -1)
            );
        }
        DirectionContext directionContext = directionContextFactory.createDirectionContext();
        AssertUtils.notNull(directionContext, "无效的指令上下文");
        if (ObjectUtils.isNotNull(prepContext)) {
            prepContext.accept(directionContext);
        }
        DirectionResult<Boolean> executed = direction.execute(directionContext, directionDetail.getArguments());
        processResult(directionDetail, executed.getCode(), executed.getAcceptedDepth());
        Boolean data = executed.getData();
        if (ObjectUtils.isNull(data)) {
            throw new InvalidDirectionException(
                    buildInvalidDirectionExceptionMessage(directionDetail, executed.getAcceptedDepth())
            );
        }
        return data;
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull String direction) throws DirectionException {
        return complete(getDirectionDetailResolver().resolve(direction), null);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull DirectionDetail directionDetail) throws DirectionException {
        return complete(directionDetail, null);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull String direction, @Nullable Consumer<DirectionContext> prepContext) throws DirectionException {
        return complete(getDirectionDetailResolver().resolve(direction), prepContext);
    }

    @Override
    @NotNull
    public List<String> complete(@NotNull DirectionDetail directionDetail, @Nullable Consumer<DirectionContext> prepContext) throws DirectionException {
        AssertUtils.notNull(directionDetail, "无效的指令细节");
        String name = directionDetail.getName();
        Direction direction = directionMap.get(name);
        if (ObjectUtils.isNull(direction)) {
            throw new InvalidDirectionException(
                    buildInvalidDirectionExceptionMessage(directionDetail, -1)
            );
        }
        DirectionContext directionContext = directionContextFactory.createDirectionContext();
        AssertUtils.notNull(directionContext, "无效的指令上下文");
        if (ObjectUtils.isNotNull(prepContext)) {
            prepContext.accept(directionContext);
        }
        DirectionResult<List<String>> completed = direction.complete(directionContext, directionDetail.getArguments());
        processResult(directionDetail, completed.getCode(), completed.getAcceptedDepth());
        List<String> data = completed.getData();
        if (ObjectUtils.isNull(data)) {
            throw new InvalidDirectionException(
                    buildInvalidDirectionExceptionMessage(directionDetail, completed.getAcceptedDepth())
            );
        }
        return data;
    }

    @Override
    @NotNull
    public DirectionDetailResolver getDirectionDetailResolver() {
        return directionDetailResolver;
    }

    @Override
    public void setDirectionDetailResolver(@NotNull DirectionDetailResolver directionDetailResolver) {
        AssertUtils.notNull(directionDetailResolver, "无效的指令细节解析器");
        this.directionDetailResolver = directionDetailResolver;
    }

    @Override
    @NotNull
    public DirectionContextFactory getDirectionContextFactory() {
        return directionContextFactory;
    }

    @Override
    public void setDirectionContextFactory(@NotNull DirectionContextFactory directionContextFactory) {
        AssertUtils.notNull(directionContextFactory, "无效的指令上下文工厂");
        this.directionContextFactory = directionContextFactory;
    }
}
