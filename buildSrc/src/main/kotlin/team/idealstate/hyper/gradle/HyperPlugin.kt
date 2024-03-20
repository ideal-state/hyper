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