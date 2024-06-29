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

package team.idealstate.hyper.gradle.module.java

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.language.jvm.tasks.ProcessResources
import team.idealstate.hyper.gradle.ConfigurablePlugin
import team.idealstate.hyper.gradle.ConfigurationSupport
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.java.entity.Java
import team.idealstate.hyper.gradle.module.java.task.HyperDocJar
import team.idealstate.hyper.gradle.module.java.task.HyperJar
import team.idealstate.hyper.gradle.module.java.task.HyperSourcesJar
import team.idealstate.hyper.gradle.module.repository.RepositoryPlugin
import java.io.File

/**
 * <p>JavaPlugin</p>
 *
 * <p>创建于 2024/3/19 15:56</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class JavaPlugin : ConfigurablePlugin<Java>(
    ConfigurationSupport.TOML,
    ASSET_MODULE_JAVA_HYPER_TOML,
    Java::class.java,
) {

    init {
        super.dependsOn(HyperPlugin.module(RepositoryPlugin.ID), JAVA_PLUGIN_ID, JAVA_LIBRARY_PLUGIN_ID)
    }

    override fun apply() {
        createHyperConfigurations()
        configureJavaPluginExtension()

        val java = config
        val encoding = java.file.encoding
        configureJavaCompileTask(encoding)
        configureProcessResourcesTask(encoding)
        configureJavadocTask(encoding)

        val tasks = project.tasks
        tasks.create(HyperSourcesJar.NAME, HyperSourcesJar::class.java)
        tasks.create(HyperDocJar.NAME, HyperDocJar::class.java)
        tasks.create(HyperJar.NAME, HyperJar::class.java)
    }

    private fun createHyperConfigurations() {
        val hyperCompileClasspath = createHyperConfiguration(COMPILE_CLASSPATH)
        val hyperRuntimeClasspath = createHyperConfiguration(RUNTIME_CLASSPATH)
        createHyperConfiguration(COMPILE_ONLY, hyperCompileClasspath)
        createHyperConfiguration(IMPLEMENTATION, hyperRuntimeClasspath)
        createHyperConfiguration(RUNTIME_ONLY, hyperRuntimeClasspath)
        createHyperConfiguration(COMPILE_ONLY_API, hyperCompileClasspath)
        createHyperConfiguration(API, hyperRuntimeClasspath)
    }

    private fun createHyperConfiguration(originalName: String, vararg subConfigs: Configuration): Configuration {
        val configurations = project.configurations
        val configuration = configurations.getByName(originalName)
        return configurations.create(getHyperConfigurationName(originalName)) {
            configuration.extendsFrom(it)
            for (subConfig in subConfigs) {
                subConfig.extendsFrom(it)
            }
        }
    }

    private fun getHyperConfiguration(originalName: String): Configuration {
        return project.configurations.getByName(getHyperConfigurationName(originalName))
    }

    private fun configureJavaPluginExtension() {
        val java = config
        val javaLanguageVersion = java.language.version
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
            it.options.also { compileOptions ->
                compileOptions.isFork = true
                compileOptions.encoding = encoding
                compileOptions.compilerArgs.also { compileArgs ->
                    compileArgs.add("-deprecation")
                    compileArgs.add("-parameters")
                    compileArgs.add("-XDignore.symbol.file")
                }
                compileOptions.forkOptions.also { forkOptions ->
                    forkOptions.jvmArgs!!.add("-J-Dfile.encoding=$encoding")
                    forkOptions.executable =
                        it.javaCompiler.get().executablePath.asFile.absolutePath
                }
            }
        }
    }

    private fun configureProcessResourcesTask(charset: String) {
        project.extensions.getByType(SourceSetContainer::class.java).forEach {
            val file = project.file("src/${it.name}/resources/assets/")
            if (file.parentFile.exists() && !file.exists()) {
                file.mkdirs()
            }
        }
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
                val dependencies = getDependencies(getHyperConfiguration(RUNTIME_CLASSPATH))
                if (dependencies.isEmpty()) {
                    return@doLast
                }
                val repositories = project.repositories.filter { repository ->
                    if (repository is MavenArtifactRepository) {
                        if (repository.url.scheme != "file") {
                            val credentials = repository.credentials
                            if (credentials.username == null && credentials.password == null) {
                                if (repository.authentication.isEmpty()) {
                                    return@filter true
                                }
                            }
                        }
                    }
                    return@filter false
                }.map { repository ->
                    repository as MavenArtifactRepository
                    return@map linkedMapOf<String, String>(
                        "name" to repository.name,
                        "url" to repository.url.toURL().toString(),
                    )
                }
                val file = File(it.destinationDir, "$assetsDir/$DEPENDENCIES_HYPER_JSON")
                if (!file.exists()) {
                    val parentFile = file.parentFile
                    if (!parentFile.exists()) {
                        parentFile.mkdirs()
                    }
                    file.createNewFile()
                }
                ConfigurationSupport.JSON.build().writerWithDefaultPrettyPrinter().writeValue(
                    file, linkedMapOf(
                        "repositories" to repositories,
                        "dependencies" to dependencies
                    )
                )

                val assetsDirFile = File(it.destinationDir, assetsDir)
                copyFile(project.file(LICENSES_DIR), assetsDirFile)
                copyFile(project.file(LICENSE_TXT), assetsDirFile)
                copyFile(project.file(NOTICE_TXT), assetsDirFile)
            }
        }
    }

    protected fun copyFile(source: File, destinationDir: File) {
        if (!source.exists()) {
            return
        }
        if (!destinationDir.exists()) {
            destinationDir.mkdirs()
        }
        require(destinationDir.isDirectory) { "目标路径 '${destinationDir}' 不是一个有效的文件夹" }
        if (source.isFile) {
            source.copyTo(File(destinationDir, source.name))
            return
        }
        val destDir = File(destinationDir, source.name)
        source.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                copyFile(file, destDir)
            } else {
                file.copyTo(File(destDir, file.name))
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

        @JvmStatic
        val ID = "java"

        @JvmStatic
        private val ASSET_MODULE_JAVA_HYPER_TOML =
            "${HyperPlugin.NAME}/module/java.hyper.toml"

        @JvmStatic
        private val DEPENDENCIES_HYPER_JSON = "dependencies.hyper.json"

        @JvmStatic
        val JAVA_PLUGIN_ID = "java"

        @JvmStatic
        val JAVA_LIBRARY_PLUGIN_ID = "java-library"

        @JvmStatic
        val COMPILE_CLASSPATH = "compileClasspath"

        @JvmStatic
        val RUNTIME_CLASSPATH = "runtimeClasspath"

        @JvmStatic
        val COMPILE_ONLY = "compileOnly"

        @JvmStatic
        val IMPLEMENTATION = "implementation"

        @JvmStatic
        val RUNTIME_ONLY = "runtimeOnly"

        @JvmStatic
        val COMPILE_ONLY_API = "compileOnlyApi"

        @JvmStatic
        val API = "api"

        @JvmStatic
        val LICENSES_DIR = "LICENSES"

        @JvmStatic
        val LICENSE_TXT = "LICENSE.txt"

        @JvmStatic
        val NOTICE_TXT = "NOTICE.txt"

        @JvmStatic
        fun getHyperConfigurationName(originalName: String): String {
            var first = originalName[0]
            var configurationName = originalName
            if (Character.isLowerCase(first)) {
                first = first.uppercaseChar()
                configurationName = first.toString() + configurationName.substring(1)
            }
            return "hyper$configurationName"
        }

        @JvmStatic
        fun getDependencies(configuration: Configuration): LinkedHashSet<Map<String, String>> {
            val dependencies = configuration.resolvedConfiguration.firstLevelModuleDependencies
            val ids = LinkedHashSet<Map<String, String>>(dependencies.size)
            for (dependency in dependencies) {
                ids.add(
                    linkedMapOf(
                        "group" to dependency.moduleGroup,
                        "name" to dependency.moduleName,
                        "version" to dependency.moduleVersion,
                    )
                )
            }
            return ids
        }
    }
}