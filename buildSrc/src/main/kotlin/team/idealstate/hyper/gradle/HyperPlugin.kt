package team.idealstate.hyper.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import team.idealstate.hyper.gradle.hyper.lang.Language

/**
 * <p>HyperPlugin</p>
 *
 * <p>创建于 2024/3/17 20:16</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
abstract class HyperPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val language = Language.of(project)
        project.plugins.apply(language.plugin)
    }

    companion object {
        @JvmStatic
        val NAME = "hyper"
    }
}