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
import team.idealstate.hyper.director.api.factory.DirectionFactory;
import team.idealstate.hyper.director.api.factory.DirectionUnitFactory;
import team.idealstate.hyper.director.builder.api.DirectionBuilder;
import team.idealstate.hyper.director.builder.api.factory.DirectionBuilderFactory;

/**
 * <p>StdDirectionBuilderFactory</p>
 *
 * <p>创建于 2024/3/27 11:34</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public final class StdDirectionBuilderFactory implements DirectionBuilderFactory {
    
    @Override
    @NotNull
    public DirectionBuilder builder(
            @NotNull DirectionFactory directionFactory,
            @NotNull DirectionUnitFactory directionUnitFactory
    ) {
        AssertUtils.notNull(directionFactory, "无效的指令工厂");
        AssertUtils.notNull(directionUnitFactory, "无效的指令单元工厂");
        return new StdDirectionBuilder(directionFactory, directionUnitFactory);
    }
}
