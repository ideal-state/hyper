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

package team.idealstate.hyper.director.builder.api;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.director.api.DirectionUnit;
import team.idealstate.hyper.director.api.component.*;
import team.idealstate.hyper.director.builder.api.exception.NoSuchParentException;

import java.util.function.Consumer;

/**
 * <p>DirectionUnitBuilder</p>
 *
 * <p>创建于 2024/3/27 8:07</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public interface DirectionUnitBuilder extends Builder<DirectionUnit> {

    @NotNull
    DirectionBuilder root();

    @NotNull
    DirectionUnitBuilder parent() throws NoSuchParentException;

    @NotNull
    DirectionUnitBuilder and() throws NoSuchParentException;

    @NotNull
    DirectionUnitBuilder name(@NotNull String name);

    @NotNull
    DirectionUnitBuilder description(@NotNull String description);

    @NotNull
    DirectionUnitBuilder actionInterceptor(@Nullable ActionInterceptor actionInterceptor);

    @NotNull
    DirectionUnitBuilder argumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor);

    @NotNull
    DirectionUnitBuilder exampleListProvider(@Nullable ExampleListProvider exampleListProvider);

    @NotNull
    DirectionUnitBuilder directionCompleter(@Nullable DirectionUnitCompleter directionUnitCompleter);

    @NotNull
    DirectionUnitBuilder directionExecutor(@Nullable DirectionUnitExecutor directionUnitExecutor);

    @NotNull
    DirectionUnitBuilder addChild(@NotNull String childUnitName);

    @NotNull
    DirectionUnitBuilder addChild(@NotNull String childUnitName, @NotNull String childUnitDescription);

    @NotNull
    DirectionUnitBuilder addChild(@NotNull Consumer<DirectionUnitBuilder> childUnitBuilderConsumer);

    @NotNull
    DirectionUnitBuilder removeChild(@NotNull String childUnitName);
}
