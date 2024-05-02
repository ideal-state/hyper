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

package team.idealstate.hyper.command.api.exception;

/**
 * <p>InterceptedCommandException</p>
 *
 * <p>创建于 2024/3/27 5:12</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public class InterceptedCommandException extends CommandException {
    private static final long serialVersionUID = -2867301262964807977L;

    public InterceptedCommandException() {
    }

    public InterceptedCommandException(String message) {
        super(message);
    }

    public InterceptedCommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterceptedCommandException(Throwable cause) {
        super(cause);
    }
}
