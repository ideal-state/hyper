package team.idealstate.hyper.gradle.hyper.lang

import org.gradle.api.Project
import team.idealstate.hyper.gradle.hyper.lang.plugin.JavaPlugin
import team.idealstate.hyper.gradle.hyper.lang.plugin.KotlinJvmPlugin

/**
 * <p>Language</p>
 *
 * <p>创建于 2024/3/19 0:15</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
enum class Language(private val pluginType: Class<out Plugin>?) {

    UNKNOWN(null),
    JAVA(JavaPlugin::class.java),
    KOTLIN_JVM(KotlinJvmPlugin::class.java);

    val plugin: Class<out Plugin>
        get() = pluginType ?: throw IllegalStateException("Unknown language.")

    companion object {
        @JvmStatic
        fun of(project: Project): Language {
            val plugins = project.plugins
            return when {
                plugins.hasPlugin("org.jetbrains.kotlin.jvm") -> KOTLIN_JVM
                plugins.hasPlugin("java") -> JAVA
                else -> throw IllegalStateException("Unknown language.")
            }
        }
    }
}