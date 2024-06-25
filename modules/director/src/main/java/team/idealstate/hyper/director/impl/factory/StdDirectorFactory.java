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
import team.idealstate.hyper.director.api.DirectionDetailResolver;
import team.idealstate.hyper.director.api.Director;
import team.idealstate.hyper.director.api.factory.DirectionContextFactory;
import team.idealstate.hyper.director.api.factory.DirectorFactory;
import team.idealstate.hyper.director.impl.StdDirector;

/**
 * <p>StdDirectorFactory</p>
 *
 * <p>创建于 2024/3/27 12:25</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdDirectorFactory implements DirectorFactory {
    @Override
    public @NotNull Director createDirector(@NotNull DirectionDetailResolver directionDetailResolver, @NotNull DirectionContextFactory directionContextFactory) {
        return new StdDirector(directionDetailResolver, directionContextFactory);
    }
}
