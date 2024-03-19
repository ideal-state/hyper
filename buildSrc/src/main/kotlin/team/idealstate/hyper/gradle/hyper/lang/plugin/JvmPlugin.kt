package team.idealstate.hyper.gradle.hyper.lang.plugin

import team.idealstate.hyper.gradle.hyper.lang.Plugin

/**
 * <p>JvmPlugin</p>
 *
 * <p>创建于 2024/3/19 15:33</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
internal abstract class JvmPlugin : Plugin() {
    companion object {
        @JvmStatic
        protected val JAVA_PLUGIN_ID = "java"

        @JvmStatic
        protected val JAVA_LIBRARY_PLUGIN_ID = "java-library"

        @JvmStatic
        protected val ASSET_JAVA_LANG_HYPER_TOML = "hyper/lang/java.lang.hyper.toml"
    }
}