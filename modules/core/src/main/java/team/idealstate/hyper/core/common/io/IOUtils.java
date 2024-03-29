/*
 *    hyper-core
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

package team.idealstate.hyper.core.common.io;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;

import java.io.*;

/**
 * <p>IOUtils</p>
 *
 * <p>创建于 2024/3/26 20:36</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class IOUtils {

    public static final int DEFAULT_BUF_SIZE = 1024;

    @NotNull
    public static ByteArrayOutputStream byteArrayOutputStream() {
        return byteArrayOutputStream(DEFAULT_BUF_SIZE);
    }

    @NotNull
    public static ByteArrayOutputStream byteArrayOutputStream(int size) {
        return new ByteArrayOutputStream(size);
    }

    @NotNull
    public static ByteArrayInputStream byteArrayInputStream(byte @NotNull [] byteArray) {
        AssertUtils.notNull(byteArray, "无效的字节数组");
        return new ByteArrayInputStream(byteArray);
    }

    @NotNull
    public static ObjectOutputStream objectOutputStream(@NotNull OutputStream outputStream) throws IORuntimeException {
        AssertUtils.notNull(outputStream, "无效的输出流");
        try {
            return new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    @NotNull
    public static ObjectInputStream objectInputStream(@NotNull InputStream inputStream) throws IORuntimeException {
        AssertUtils.notNull(inputStream, "无效的输入流");
        try {
            return new ObjectInputStream(inputStream);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
