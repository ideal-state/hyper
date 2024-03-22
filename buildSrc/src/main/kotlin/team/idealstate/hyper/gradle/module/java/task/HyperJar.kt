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

package team.idealstate.hyper.gradle.module.java.task

import org.gradle.api.tasks.SourceSetContainer
import org.gradle.work.DisableCachingByDefault

/**
 * <p>HyperJar</p>
 *
 * <p>创建于 2024/3/18 21:28</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
open class HyperJar : AbstractHyperJar() {

    init {
        super.dependsOn(project.tasks.named(PROCESS_RESOURCES_ID), project.tasks.named(CLASSES_ID))
        archiveClassifier.set("")
    }

    override fun copy() {
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        from(
            sourceSets.named(MAIN_SOURCE_SET_ID).get().output.classesDirs,
            project.tasks.named(PROCESS_RESOURCES_ID)
        )
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperJar"
    }
}