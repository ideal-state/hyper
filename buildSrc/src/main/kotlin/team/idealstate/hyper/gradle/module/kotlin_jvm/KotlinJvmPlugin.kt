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

package team.idealstate.hyper.gradle.module.kotlin_jvm

import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import team.idealstate.hyper.gradle.ConfigurablePlugin
import team.idealstate.hyper.gradle.ConfigurationSupport
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.java.JavaPlugin
import team.idealstate.hyper.gradle.module.java.task.AbstractHyperJar
import team.idealstate.hyper.gradle.module.java.task.HyperDocJar
import team.idealstate.hyper.gradle.module.kotlin_jvm.entity.KotlinJvm

/**
 * <p>KotlinJvmPlugin</p>
 *
 * <p>创建于 2024/3/19 15:57</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class KotlinJvmPlugin : ConfigurablePlugin<KotlinJvm>(
    ConfigurationSupport.TOML,
    ASSET_MODULE_KOTLIN_JVM_HYPER_TOML,
    KotlinJvm::class.java
) {

    init {
        super.dependsOn(HyperPlugin.module(JavaPlugin.ID), KOTLIN_JVM_PLUGIN_ID, KOTLIN_DOKKA_PLUGIN_ID)
    }

    override fun apply() {
        val kotlinJvm = config
        val kotlinJvmVersion = kotlinJvm.language.version
        project.extensions.getByType(KotlinJvmProjectExtension::class.java).also {
            it.coreLibrariesVersion = kotlinJvmVersion
            it.compilerOptions.javaParameters.set(true)
        }
        val tasks = project.tasks
        tasks.withType(AbstractHyperJar::class.java) {
            it.manifest.attributes(
                mapOf(
                    "Hyper-Kotlin-Version" to kotlinJvmVersion,
                )
            )
        }
        tasks.withType(HyperDocJar::class.java) { docJar ->
            docJar.docSource {
                it.set(tasks.named(KOTLIN_DOKKA_HTML_TASK_ID))
            }
        }
    }

    companion object {

        @JvmStatic
        val ID = "kotlin_jvm"

        @JvmStatic
        protected val ASSET_MODULE_KOTLIN_JVM_HYPER_TOML =
            "${HyperPlugin.NAME}/module/kotlin_jvm.hyper.toml"

        @JvmStatic
        protected val KOTLIN_JVM_PLUGIN_ID = "org.jetbrains.kotlin.jvm"

        @JvmStatic
        protected val KOTLIN_DOKKA_PLUGIN_ID = "org.jetbrains.dokka"

        @JvmStatic
        protected val KOTLIN_DOKKA_HTML_TASK_ID = "dokkaHtml"
    }
}