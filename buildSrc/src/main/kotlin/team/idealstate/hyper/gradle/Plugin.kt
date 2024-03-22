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

import org.gradle.api.Project
import java.io.File

/**
 * <p>Plugin</p>
 *
 * <p>创建于 2024/3/19 17:45</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
abstract class Plugin : org.gradle.api.Plugin<Project> {

    private var applied: Project? = null
    protected val project: Project
        get() = applied!!
    private val depends: MutableSet<String> = linkedSetOf()

    final override fun apply(target: Project) {
        applied = target
        if (this !is HyperPlugin) {
            depends.add(HyperPlugin.ID)
        }
        applyDepends()
        apply()
    }

    protected abstract fun apply()

    protected fun dependsOn(vararg pluginId: String) {
        depends.addAll(pluginId)
    }

    private fun applyDepends() {
        val plugins = project.plugins
        for (depend in depends) {
            if (plugins.hasPlugin(depend)) {
                continue
            }
            plugins.apply(depend)
        }
    }

    protected fun asset(path: String, onlyFile: Boolean = false): File {
        val file = project.file(path.run {
            if (onlyFile) {
                replace('\\', '/').substringAfterLast('/')
            } else {
                this
            }
        })
        if (!file.exists()) {
            val parentFile = file.parentFile
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            val asset = "/$ASSETS_ROOT_DIR$path"
            val resource = Companion::class.java.getResourceAsStream(asset)
                ?: throw IllegalStateException("不存在默认资源 '$asset'")
            file.outputStream().use { output ->
                resource.use { input ->
                    val buffer = ByteArray(1024)
                    while (true) {
                        val read = input.read(buffer)
                        if (read == -1) break
                        output.write(buffer, 0, read)
                    }
                }
                output.flush()
            }
        }
        return file
    }

    companion object {
        @JvmStatic
        protected val ASSETS_ROOT_DIR = "assets/"
    }
}