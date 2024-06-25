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

package team.idealstate.hyper.director.api;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.director.api.exception.DirectionException;

import java.util.List;

/**
 * <p>Direction</p>
 *
 * <p>创建于 2024/3/27 4:03</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Direction {
    @NotNull
    DirectionResult<List<String>> complete(@NotNull DirectionContext context, @NotNull String[] arguments) throws DirectionException;

    @NotNull
    DirectionResult<Boolean> execute(@NotNull DirectionContext context, @NotNull String[] arguments) throws DirectionException;

    @NotNull
    String getName();

    void setName(@NotNull String name);

    @NotNull
    String getDescription();

    void setDescription(@NotNull String description);

    @NotNull
    List<DirectionUnit> getDirectionUnits();

    void addDirectionUnit(@NotNull DirectionUnit directionUnit);

    void removeDirectionUnit(@NotNull String directionUnitName);
}
