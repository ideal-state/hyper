package team.idealstate.hyper.gradle.hyper.lang.plugin

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.language.jvm.tasks.ProcessResources
import team.idealstate.hyper.gradle.hyper.HyperExtension
import team.idealstate.hyper.gradle.hyper.entity.Build
import team.idealstate.hyper.gradle.hyper.lang.entity.JavaLang
import team.idealstate.hyper.gradle.hyper.task.HyperDocJar
import team.idealstate.hyper.gradle.hyper.task.HyperJar
import team.idealstate.hyper.gradle.hyper.task.HyperSourcesJar
import java.io.File

/**
 * <p>JavaPlugin</p>
 *
 * <p>创建于 2024/3/19 15:56</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
internal open class JavaPlugin : JvmPlugin() {

    private var _javaLang: JavaLang? = null
    protected val javaLang: JavaLang
        get() = _javaLang ?: throw IllegalStateException("尚未加载语言配置文件 '$ASSET_JAVA_LANG_HYPER_TOML'")

    init {
        super.dependsOn(JAVA_LIBRARY_PLUGIN_ID)
    }

    override fun apply() {
        val build = toml.readValue(asset(ASSET_BUILD_HYPER_TOML), Build::class.java)
        val (major, minor, revision) = build.version
        project.version = "$major.$minor.$revision"

        _javaLang = toml.readValue(asset(ASSET_JAVA_LANG_HYPER_TOML), JavaLang::class.java)

        project.extensions.add("hyper", HyperExtension(build))

        createHyperConfigurations()
        configureJavaPluginExtension()

        val encoding = javaLang.file.encoding
        configureJavaCompileTask(encoding)
        configureProcessResourcesTask(encoding)
        configureJavadocTask(encoding)

        val tasks = project.tasks
        tasks.create(HyperSourcesJar.NAME, HyperSourcesJar::class.java)
        tasks.create(HyperDocJar.NAME, HyperDocJar::class.java)
        tasks.create(HyperJar.NAME, HyperJar::class.java)
    }

    private fun getHyperConfigurationName(original: String): String {
        var first = original[0]
        var configurationName = original
        if (Character.isLowerCase(first)) {
            first = first.uppercaseChar()
            configurationName = first.toString() + configurationName.substring(1)
        }
        return "hyper$configurationName"
    }

    private fun createHyperConfigurations() {
        val hyperCompileClasspath = createHyperConfiguration("compileClasspath")
        val hyperRuntimeClasspath = createHyperConfiguration("runtimeClasspath")
        createHyperConfiguration("compileOnly", hyperCompileClasspath)
        createHyperConfiguration("implementation", hyperRuntimeClasspath)
        createHyperConfiguration("runtimeOnly", hyperRuntimeClasspath)
        createHyperConfiguration("compileOnlyApi", hyperCompileClasspath)
        createHyperConfiguration("api", hyperRuntimeClasspath)
    }

    private fun createHyperConfiguration(originalName: String, vararg superConfigs: Configuration): Configuration {
        val configurations = project.configurations
        val configuration = configurations.getByName(originalName)
        return configurations.create(getHyperConfigurationName(originalName)) {
            configuration.extendsFrom(it)
            for (superConfig in superConfigs) {
                superConfig.extendsFrom(it)
            }
        }
    }

    private fun getHyperConfiguration(originalName: String): Configuration {
        return project.configurations.getByName(getHyperConfigurationName(originalName))
    }

    private fun configureJavaPluginExtension() {
        val javaLanguageVersion = javaLang.language.version
        project.extensions.getByType(JavaPluginExtension::class.java).also {
            it.sourceCompatibility = JavaVersion.toVersion(javaLanguageVersion)
            it.targetCompatibility = it.sourceCompatibility
            it.toolchain { toolchain ->
                toolchain.languageVersion.set(JavaLanguageVersion.of(javaLanguageVersion))
                toolchain.vendor.set(JvmVendorSpec.AZUL)
            }
        }
    }

    private fun configureJavaCompileTask(encoding: String) {
        project.tasks.withType(JavaCompile::class.java) {
            it.options.encoding = encoding
            it.options.compilerArgs.apply {
                add("-XDignore.symbol.file")
                add("-parameters")
            }
            it.options.isFork = true
            it.options.forkOptions.executable =
                it.javaCompiler.get().executablePath.asFile.absolutePath
        }
    }

    private fun getDependencies(originalName: String): LinkedHashSet<Map<String, String>> {
        val dependencies = getHyperConfiguration(originalName)
            .resolvedConfiguration.firstLevelModuleDependencies
        val ids = LinkedHashSet<Map<String, String>>(dependencies.size)
        for (dependency in dependencies) {
            ids.add(
                linkedMapOf(
                    "group" to dependency.moduleGroup,
                    "name" to dependency.moduleName,
                    "version" to dependency.moduleVersion
                )
            )
        }
        return ids
    }

    private fun configureProcessResourcesTask(charset: String) {
        project.tasks.withType(ProcessResources::class.java) {
            it.filteringCharset = charset
            it.includeEmptyDirs = false
            val rootDir = ASSETS_ROOT_DIR
            val assetsDir = "$rootDir${project.name}"
            val prefix = rootDir.length - 1
            it.eachFile { details ->
                if (details.path.startsWith(rootDir)) {
                    print("> Asset [${details.path} >> ")
                    details.path = assetsDir + details.path.substring(prefix)
                    println(details.path + "]")
                }
            }
            it.doLast { _ ->
                val dependencyIds = getDependencies("runtimeClasspath")
                if (dependencyIds.isEmpty()) {
                    return@doLast
                }
                val file = File(it.destinationDir, "$assetsDir/$DEPENDENCIES_HYPER_JSON")
                if (!file.exists()) {
                    val parentFile = file.parentFile
                    if (!parentFile.exists()) {
                        parentFile.mkdirs()
                    }
                    file.createNewFile()
                }
                json.writerWithDefaultPrettyPrinter().writeValue(file, dependencyIds)
            }
        }
    }

    private fun configureJavadocTask(charset: String) {
        project.tasks.withType(Javadoc::class.java) {
            it.options { options ->
                options as StandardJavadocDocletOptions
                options.charSet(charset)
                options.encoding(charset)
                options.docEncoding(charset)
                options.locale("zh_CN")
                options.windowTitle("${project.name}-${project.version} API")
                options.docTitle(options.windowTitle)
                options.author(true)
                options.version(true)
                options.jFlags("-D'file.encoding'=${charset}")
            }
        }
    }

    companion object {
        private const val DEPENDENCIES_HYPER_JSON = "dependencies.hyper.json"
    }
}