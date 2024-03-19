package team.idealstate.hyper.gradle.hyper.lang.plugin

import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * <p>KotlinJvmPlugin</p>
 *
 * <p>创建于 2024/3/19 15:57</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
internal class KotlinJvmPlugin : JavaPlugin() {
    override fun apply() {
        super.apply()
        val javaLanguageVersion = javaLang.language.version
        project.extensions.getByType(KotlinJvmProjectExtension::class.java).also {
            it.jvmToolchain { toolchain ->
                toolchain.languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion))
                toolchain.vendor.set(JvmVendorSpec.AZUL)
            }
            it.compilerOptions.javaParameters.set(true)
        }
    }

    companion object {

        @JvmStatic
        private val KOTLIN_JVM_PLUGIN_ID = "org.jetbrains.kotlin.jvm"
    }
}