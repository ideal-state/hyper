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

package team.idealstate.hyper.library.loader.impl;

import org.apache.commons.lang3.Validate;
import team.idealstate.hyper.annotation.lang.NotNull;
import team.idealstate.hyper.library.loader.api.DependencyDownloader;
import team.idealstate.hyper.library.loader.api.entity.Dependency;
import team.idealstate.hyper.library.loader.api.entity.Repository;

import java.io.File;
import java.util.List;

/**
 * <p>MavenDependencyDownloader</p>
 *
 * <p>创建于 2024/3/28 12:19</p>
 *
 * @author ketikai
 * @version 1.0.0
 * @since 1.0.0
 */
public class MavenDependencyDownloader implements DependencyDownloader {
    @Override
    @NotNull
    public List<File> download(@NotNull Dependency dependency) {
        Validate.notNull(dependency, "");
        return null;
    }

    @Override
    @NotNull
    public List<Repository> getRepositories() {
        return null;
    }

    @Override
    @NotNull
    public File getDestinationDirectory() {
        return null;
    }
}
