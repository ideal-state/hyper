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

package team.idealstate.hyper.command.api;

import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.core.common.AssertUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>CommandDetail</p>
 *
 * <p>创建于 2024/3/27 4:27</p>
 *
 * @author ketikai
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommandDetail {

    private String name;

    private String[] arguments;

    public CommandDetail(@NotNull String name, @NotNull String[] arguments) {
        AssertUtils.notBlank(name, "无效的命令名称");
        AssertUtils.notNull(arguments, "无效的命令参数数组");
        this.name = name;
        this.arguments = arguments;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        AssertUtils.notBlank(name, "无效的命令名称");
        this.name = name;
    }

    @NotNull
    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(@NotNull String[] arguments) {
        AssertUtils.notNull(arguments, "无效的命令参数数组");
        this.arguments = arguments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandDetail)) {
            return false;
        }
        final CommandDetail that = (CommandDetail) o;

        if (!Objects.equals(name, that.name)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return "CommandDetail{" +
                "name='" + name + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
