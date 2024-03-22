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

import org.gradle.api.plugins.ObjectConfigurationAction
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.java.JavaPlugin
import team.idealstate.hyper.gradle.module.kotlin_jvm.KotlinJvmPlugin
import team.idealstate.hyper.gradle.module.maven_publish.MavenPublishPlugin
import team.idealstate.hyper.gradle.module.repository.RepositoryPlugin

/**
 * <p>HyperDependencyExtension</p>
 *
 * <p>创建于 2024/3/20 22:50</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
val PluginDependenciesSpec.hyper_repository: PluginDependencySpec
    get() = id(HyperPlugin.module(RepositoryPlugin.ID))

val PluginDependenciesSpec.hyper_java: PluginDependencySpec
    get() = id(HyperPlugin.module(JavaPlugin.ID))

val PluginDependenciesSpec.hyper_kotlin_jvm: PluginDependencySpec
    get() = id(HyperPlugin.module(KotlinJvmPlugin.ID))

val PluginDependenciesSpec.hyper_maven_publish: PluginDependencySpec
    get() = id(HyperPlugin.module(MavenPublishPlugin.ID))

val ObjectConfigurationAction.hyper_repository: ObjectConfigurationAction
    get() {
        plugin(RepositoryPlugin::class.java)
        return this
    }

val ObjectConfigurationAction.hyper_java: ObjectConfigurationAction
    get() {
        plugin(JavaPlugin::class.java)
        return this
    }

val ObjectConfigurationAction.hyper_kotlin_jvm: ObjectConfigurationAction
    get() {
        plugin(KotlinJvmPlugin::class.java)
        return this
    }

val ObjectConfigurationAction.hyper_maven_publish: ObjectConfigurationAction
    get() {
        plugin(MavenPublishPlugin::class.java)
        return this
    }
