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
import team.idealstate.hyper.director.api.Direction;

import java.util.function.Consumer;

/**
 * <p>DirectionBuilder</p>
 *
 * <p>创建于 2024/3/27 7:53</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DirectionBuilder extends Builder<Direction> {

    @NotNull
    DirectionBuilder name(@NotNull String name);

    @NotNull
    DirectionBuilder description(@NotNull String description);

    @NotNull
    DirectionUnitBuilder addDirectionUnit(@NotNull String unitName);

    @NotNull
    DirectionUnitBuilder addDirectionUnit(@NotNull String unitName, @NotNull String unitDescription);

    @NotNull
    DirectionBuilder addDirectionUnit(@NotNull Consumer<DirectionUnitBuilder> unitBuilderConsumer);

    @NotNull
    DirectionBuilder removeDirectionUnit(@NotNull String unitName);
}
