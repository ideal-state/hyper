/*
 *    hyper-command-impl
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

package team.idealstate.hyper.command.impl.factory;

import org.jetbrains.annotations.NotNull;
import team.idealstate.hyper.command.api.CommandUnit;
import team.idealstate.hyper.command.api.factory.CommandUnitFactory;
import team.idealstate.hyper.command.impl.CommandUnitImpl;

/**
 * <p>CommandUnitFactoryImpl</p>
 *
 * <p>创建于 2024/3/27 12:23</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class CommandUnitFactoryImpl implements CommandUnitFactory {
    @Override
    public @NotNull CommandUnit createCommandUnit(@NotNull String name) {
        return new CommandUnitImpl(name);
    }

    @Override
    public @NotNull CommandUnit createCommandUnit(@NotNull String name, @NotNull String description) {
        return new CommandUnitImpl(name, description);
    }
}