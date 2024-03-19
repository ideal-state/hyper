package team.idealstate.hyper.gradle.hyper.lang

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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

    protected val toml: TomlMapper = TomlMapper.builder()
        .addModule(
            KotlinModule.Builder().build()
        )
        .build()

    protected val json: ObjectMapper = ObjectMapper().apply {
        registerModule(
            KotlinModule.Builder().build()
        )
    }

    final override fun apply(target: Project) {
        applied = target
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

        @JvmStatic
        protected val ASSET_BUILD_HYPER_TOML = "hyper/build.hyper.toml"
    }
}