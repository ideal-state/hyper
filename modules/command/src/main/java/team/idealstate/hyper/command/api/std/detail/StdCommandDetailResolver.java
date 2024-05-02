/*
 *    hyper-command
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

package team.idealstate.hyper.command.api.std.detail;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.command.api.CommandDetail;
import team.idealstate.hyper.command.api.detail.CommandDetailResolver;
import team.idealstate.hyper.command.api.exception.InvalidCommandException;
import team.idealstate.hyper.core.common.array.ArrayUtils;

/**
 * <p>StdCommandDetailResolver</p>
 *
 * <p>创建于 2024/3/27 11:49</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public final class StdCommandDetailResolver implements CommandDetailResolver {

    private static final String PREFIX = "/";
    private static final String SEPARATOR = " ";
    private static final String[] EMPTY_ARGS = new String[0];

    @Override
    @NotNull
    public CommandDetail resolve(@NotNull String command) throws InvalidCommandException {
        if (!command.startsWith(PREFIX)) {
            throw new InvalidCommandException("命令必须以 \"" + PREFIX + "\" 开头。");
        }
        command = command.substring(PREFIX.length());
        String[] arguments = command.split(SEPARATOR);
        command = arguments[0];
        if (arguments.length == 1) {
            arguments = EMPTY_ARGS;
        } else {
            arguments = ArrayUtils.copyOfRange(arguments, 1);
        }
        return new CommandDetail(command, arguments);
    }
}
