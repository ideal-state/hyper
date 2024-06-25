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
import team.idealstate.hyper.common.annotation.lang.Nullable;
import team.idealstate.hyper.director.api.component.*;

import java.util.List;

/**
 * <p>DirectionUnit</p>
 *
 * <p>创建于 2024/3/24 1:13</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DirectionUnit {

    @NotNull
    String getDescription();

    void setDescription(@NotNull String description);

    @Nullable
    DirectionUnit getParent();

    void setParent(@Nullable DirectionUnit directionUnit);

    boolean isRoot();

    @NotNull
    List<? extends DirectionUnit> getChildren();

    int getDepth();

    @Nullable
    ActionInterceptor getActionInterceptor();

    void setActionInterceptor(@Nullable ActionInterceptor actionInterceptor);

    @Nullable
    ArgumentAcceptor getArgumentAcceptor();

    void setArgumentAcceptor(@Nullable ArgumentAcceptor argumentAcceptor);

    @Nullable
    DirectionUnitCompleter getDirectionCompleter();

    void setDirectionCompleter(@Nullable DirectionUnitCompleter directionUnitCompleter);

    @Nullable
    DirectionUnitExecutor getDirectionExecutor();

    void setDirectionExecutor(@Nullable DirectionUnitExecutor directionUnitExecutor);

    @Nullable
    ExampleListProvider getExampleListProvider();

    void setExampleListProvider(@Nullable ExampleListProvider exampleListProvider);

    @NotNull
    String getName();

    void setName(@NotNull String name);

    void addChild(@NotNull DirectionUnit directionUnit);

    void removeChild(@NotNull String directionUnitName);
}
