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

import team.idealstate.hyper.common.AssertUtils;
import team.idealstate.hyper.common.annotation.lang.NotNull;
import team.idealstate.hyper.common.annotation.lang.Nullable;

import java.util.Objects;

/**
 * <p>DirectionResult</p>
 *
 * <p>创建于 2024/3/27 5:20</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class DirectionResult<T> {

    private Code code;
    private int acceptedDepth;
    private T data;

    public DirectionResult(@NotNull Code code) {
        this(code, -1, null);
    }

    private DirectionResult(@NotNull Code code, int acceptedDepth, T data) {
        AssertUtils.notNull(code, "无效的结果代码");
        AssertUtils.isTrue(acceptedDepth >= -1, "无效的已接受深度");
        this.code = code;
        this.acceptedDepth = acceptedDepth;
        this.data = data;
    }

    @NotNull
    public Code getCode() {
        return code;
    }

    public void setCode(@NotNull Code code) {
        AssertUtils.notNull(code, "无效的结果代码");
        this.code = code;
    }

    public int getAcceptedDepth() {
        return acceptedDepth;
    }

    public void setAcceptedDepth(int acceptedDepth) {
        AssertUtils.isTrue(acceptedDepth >= 0, "无效的已接受深度");
        if (acceptedDepth > this.acceptedDepth) {
            this.acceptedDepth = acceptedDepth;
        }
    }

    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectionResult)) {
            return false;
        }
        @SuppressWarnings("rawtypes") final DirectionResult that = (DirectionResult) o;

        if (acceptedDepth != that.acceptedDepth) {
            return false;
        }
        if (code != that.code) {
            return false;
        }
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + acceptedDepth;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectionResult{" +
                "code=" + code +
                ", acceptedDepth=" + acceptedDepth +
                ", data=" + data +
                '}';
    }

    public enum Code {
        SUCCESS,
        FAILURE,
        INTERRUPTED,
        INVALID_COMMAND;
    }
}
