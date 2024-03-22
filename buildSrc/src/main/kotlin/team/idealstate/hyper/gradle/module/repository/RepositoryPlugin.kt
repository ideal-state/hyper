/*
 *    hyper-gradle-plugin
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

package team.idealstate.hyper.gradle.module.repository

import team.idealstate.hyper.gradle.ConfigurablePlugin
import team.idealstate.hyper.gradle.ConfigurationSupport
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.repository.entity.Repositories
import team.idealstate.hyper.gradle.module.repository.entity.Repository

/**
 * <p>RepositoryPlugin</p>
 *
 * <p>创建于 2024/3/20 16:49</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class RepositoryPlugin : ConfigurablePlugin<Repositories>(
    ConfigurationSupport.TOML,
    ASSET_MODULE_REPOSITORY_HYPER_TOML,
    Repositories::class.java
) {

    override fun apply() {
        val repositories = config
        project.repositories.also { handler ->
            handler.mavenLocal()
            for (repository in repositories.repositories) {
                when (repository.type) {
                    Repository.Type.MAVEN -> handler.maven {
                        it.name = repository.name
                        it.url = repository.url
                        if (repository.username != null && repository.password != null) {
                            it.credentials { credentials ->
                                credentials.username = repository.username
                                credentials.password = repository.password
                            }
                        }
                    }

                    Repository.Type.IVY -> handler.ivy {
                        it.name = repository.name
                        it.url = repository.url
                        if (repository.username != null && repository.password != null) {
                            it.credentials { credentials ->
                                credentials.username = repository.username
                                credentials.password = repository.password
                            }
                        }
                    }
                }
            }
            handler.mavenCentral()
        }
    }

    companion object {

        @JvmStatic
        val ID = "repository"

        @JvmStatic
        private val ASSET_MODULE_REPOSITORY_HYPER_TOML =
            "${HyperPlugin.NAME}/module/repository.hyper.toml"
    }
}