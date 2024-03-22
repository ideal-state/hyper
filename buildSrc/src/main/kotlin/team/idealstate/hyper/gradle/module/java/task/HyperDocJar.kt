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

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.work.DisableCachingByDefault

/**
 * <p>HyperDocJar</p>
 *
 * <p>创建于 2024/3/19 18:19</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
open class HyperDocJar : AbstractHyperJar() {

    private val docSource: Property<Any> = objectFactory.property(Any::class.java)

    init {
        super.dependsOn(project.tasks.named(DOC_ID))
        archiveClassifier.set(DOC_ID)
    }

    fun docSource(action: Action<Property<Any>>) {
        action.execute(docSource)
        val source = docSource.orNull
        if (source is Task) {
            val javadoc = project.tasks.named(DOC_ID).get()
            javadoc.enabled = source == javadoc
            dependsOn(source)
        } else if (source == null) {
            project.tasks.named(DOC_ID).get().enabled = true
        }
    }

    override fun copy() {
        val source = docSource.orNull
        if (source == null) {
            from(project.tasks.named(DOC_ID))
        } else {
            from(source)
        }
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperDocJar"
    }
}