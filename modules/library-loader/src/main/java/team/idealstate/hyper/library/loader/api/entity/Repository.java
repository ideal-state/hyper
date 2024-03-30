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
import java.net.URI;
import java.util.Objects;

/**
 * <p>Repository</p>
 *
 * <p>创建于 2024/3/28 11:03</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class Repository implements Serializable {
    private static final long serialVersionUID = 3390425019236889369L;

    private String name;
    private URI url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repository)) {
            return false;
        }
        final Repository that = (Repository) o;

        if (!Objects.equals(name, that.name)) {
            return false;
        }
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }
}
