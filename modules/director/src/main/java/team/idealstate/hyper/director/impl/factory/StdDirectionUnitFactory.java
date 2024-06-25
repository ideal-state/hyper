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

package team.idealstate.hyper.director.impl.factory;

import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.director.api.DirectionUnit;
import team.idealstate.hyper.director.api.factory.DirectionUnitFactory;
import team.idealstate.hyper.director.impl.StdDirectionUnit;

/**
 * <p>StdDirectionUnitFactory</p>
 *
 * <p>创建于 2024/3/27 12:23</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdDirectionUnitFactory implements DirectionUnitFactory {
    @Override
    public @NotNull DirectionUnit createDirectionUnit(@NotNull String name) {
        return new StdDirectionUnit(name);
    }

    @Override
    public @NotNull DirectionUnit createDirectionUnit(@NotNull String name, @NotNull String description) {
        return new StdDirectionUnit(name, description);
    }
}
