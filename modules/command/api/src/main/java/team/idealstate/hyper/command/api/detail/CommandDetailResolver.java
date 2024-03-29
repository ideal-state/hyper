/*
 *    hyper-command-api
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

package team.idealstate.hyper.command.api.detail;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.command.api.CommandDetail;
import team.idealstate.hyper.command.api.exception.InvalidCommandException;

/**
 * <p>CommandDetailResolver</p>
 *
 * <p>创建于 2024/3/23 19:16</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public interface CommandDetailResolver {

    @NotNull
    CommandDetail resolve(@NotNull String command) throws InvalidCommandException;
}
