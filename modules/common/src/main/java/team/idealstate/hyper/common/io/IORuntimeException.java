/*
 *    hyper-common
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

package team.idealstate.hyper.common.io;

import java.io.IOException;

/**
 * <p>IORuntimeException</p>
 *
 * <p>创建于 2024/3/26 20:38</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class IORuntimeException extends RuntimeException {

    private static final long serialVersionUID = -5332878934581137170L;

    public IORuntimeException() {
    }

    public IORuntimeException(String message) {
        super(message);
    }

    public IORuntimeException(String message, IOException cause) {
        super(message, cause);
    }

    public IORuntimeException(IOException cause) {
        super(cause);
    }
}
