/*
 *    hyper-library-loader
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

package team.idealstate.hyper.library.loader.api.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>Dependency</p>
 *
 * <p>创建于 2024/3/28 10:50</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class Dependency implements Serializable {

    private static final long serialVersionUID = 8458503745477327098L;
    private String group;
    private String name;
    private String version;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }
        final Dependency that = (Dependency) o;

        if (!Objects.equals(group, that.group)) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "group='" + group + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
