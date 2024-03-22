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

package team.idealstate.hyper.gradle

import team.idealstate.hyper.gradle.entity.Build

/**
 * <p>HyperPlugin</p>
 *
 * <p>创建于 2024/3/17 20:16</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class HyperPlugin : ConfigurablePlugin<Build>(
    ConfigurationSupport.TOML,
    ASSET_BUILD_HYPER_TOML,
    Build::class.java
) {

    override fun apply() {
        val build = config
        val (major, minor, revision) = build.version
        project.version = "$major.$minor.$revision"
        project.extensions.add("hyper", HyperExtension(build))
    }

    companion object {

        @JvmStatic
        val GROUP = "hyper"

        @JvmStatic
        val ID = "team.idealstate.hyper.gradle.plugin"

        @JvmStatic
        val NAME = "hyper-gradle-plugin"

        @JvmStatic
        private val ASSET_BUILD_HYPER_TOML = "${NAME}/build.hyper.toml"

        @JvmStatic
        fun module(moduleId: String): String {
            return "$ID.module.$moduleId"
        }
    }
}