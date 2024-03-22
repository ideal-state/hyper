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

package team.idealstate.hyper.gradle.module.maven_publish.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import team.idealstate.hyper.gradle.HyperPlugin

/**
 * <p>HyperPublish</p>
 *
 * <p>创建于 2024/3/21 5:36</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class HyperPublish : DefaultTask() {

    init {
        group = HyperPlugin.GROUP
    }

    @TaskAction
    protected open fun publish() {
    }

    companion object {

        @JvmStatic
        val NAME = "hyperPublish"
    }
}