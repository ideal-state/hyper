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