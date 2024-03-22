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

package team.idealstate.hyper.gradle.module.maven_publish

import groovy.util.Node
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.plugins.signing.SigningExtension
import team.idealstate.hyper.gradle.ConfigurablePlugin
import team.idealstate.hyper.gradle.ConfigurationSupport
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.java.JavaPlugin
import team.idealstate.hyper.gradle.module.java.task.HyperDocJar
import team.idealstate.hyper.gradle.module.java.task.HyperJar
import team.idealstate.hyper.gradle.module.java.task.HyperSourcesJar
import team.idealstate.hyper.gradle.module.maven_publish.entity.Publication
import team.idealstate.hyper.gradle.module.maven_publish.task.HyperPublish

/**
 * <p>MavenPublishPlugin</p>
 *
 * <p>创建于 2024/3/20 23:23</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class MavenPublishPlugin : ConfigurablePlugin<Publication>(
    ConfigurationSupport.TOML,
    ASSET_MODULE_MAVEN_PUBLISH_HYPER_TOML,
    Publication::class.java
) {

    init {
        dependsOn(HyperPlugin.module(JavaPlugin.ID), MAVEN_PUBLISH_PLUGIN_ID)
    }

    override fun apply() {
        val publication = config
        val build = project.plugins.getPlugin(HyperPlugin::class.java).config
        val publishingExtension = project.extensions
            .getByType(PublishingExtension::class.java)
        publishingExtension.repositories { handler ->
            handler.maven {
                it.name = "ProjectBuild"
                it.url = project.uri("file://${project.projectDir}/build/repository")
            }
        }
        val tasks = project.tasks
        val publications = publishingExtension.publications
        publications.create(
            HYPER_PUBLICATION_NAME,
            MavenPublication::class.java
        ) { mavenPublication ->
            mavenPublication.groupId = project.group.toString()
            mavenPublication.artifactId = project.name
            mavenPublication.version = project.version.toString()

            mavenPublication.pom { pom ->
                pom.name.set(project.name)
                publication.description?.let {
                    pom.description.set(it)
                }
                pom.packaging = publication.packaging
                publication.url?.let {
                    pom.url.set(it.toString())
                }
                pom.inceptionYear.set(publication.inceptionYear)

                publication.organization?.let {
                    pom.organization { organization ->
                        organization.name.set(it.name)
                        organization.url.set(it.url.toString())
                    }
                }

                build.developers.forEach {
                    pom.developers { developers ->
                        developers.developer { developer ->
                            developer.id.set(it.id)
                            developer.name.set(it.name)
                            developer.email.set(it.email)
                        }
                    }
                }

                publication.license?.let {
                    pom.licenses { pomLicense ->
                        pomLicense.license { license ->
                            license.name.set(it.name)
                            license.url.set(it.url.toString())
                        }
                    }
                }

                pom.scm { pomScm ->
                    val it = publication.scm
                    pomScm.url.set(it.url.toString())
                    pomScm.connection.set(it.connection.toString())
                    pomScm.developerConnection.set(it.developerConnection.toString())
                }

                pom.withXml { xml ->
                    var dependenciesNode: Node? = null
                    val compileDependencyIds = mutableSetOf<String>()
                    var scope = "compile"
                    for (dependency in
                    JavaPlugin.getDependencies(project.configurations.getByName(JavaPlugin.COMPILE_CLASSPATH))
                    ) {
                        val group = dependency["group"]!!
                        val name = dependency["name"]!!
                        val version = dependency["version"]!!
                        val id = "${group}:${name}:${version}"
                        if (dependenciesNode == null) {
                            dependenciesNode = xml.asNode().appendNode("dependencies")
                        }
                        val dependencyNode = dependenciesNode!!.appendNode("dependency")
                        dependencyNode.appendNode("groupId", group)
                        dependencyNode.appendNode("artifactId", name)
                        dependencyNode.appendNode("version", version)
                        dependencyNode.appendNode("scope", scope)
                        compileDependencyIds.add(id)
                    }
                    scope = "runtime"
                    for (dependency in
                    JavaPlugin.getDependencies(project.configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH))
                    ) {
                        val group = dependency["group"]!!
                        val name = dependency["name"]!!
                        val version = dependency["version"]!!
                        val id = "${group}:${name}:${version}"
                        if (!compileDependencyIds.contains(id)) {
                            if (dependenciesNode == null) {
                                dependenciesNode = xml.asNode().appendNode("dependencies")
                            }
                            val dependencyNode = dependenciesNode!!.appendNode("dependency")
                            dependencyNode.appendNode("groupId", group)
                            dependencyNode.appendNode("artifactId", name)
                            dependencyNode.appendNode("version", version)
                            dependencyNode.appendNode("scope", scope)
                            compileDependencyIds.add(id)
                        }
                    }
                }
            }

            if (publication.packaging == "jar") {
                mavenPublication.artifact(tasks.named(HyperSourcesJar.NAME))
                mavenPublication.artifact(tasks.named(HyperDocJar.NAME))
                mavenPublication.artifact(tasks.named(HyperJar.NAME))
            } else {
                throw IllegalArgumentException("不支持的打包类型 '$publication.packaging'")
            }
        }
        tryConfigureSigning(*publications.toTypedArray())
        tasks.create(HyperPublish.NAME, HyperPublish::class.java) {
            it.dependsOn(REAL_PUBLISH_TASK_ID)
        }
    }

    private fun tryConfigureSigning(vararg publications: org.gradle.api.publish.Publication) {
        val executable = project.property("signing.gnupg.executable") as String?
        val keyName = project.property("signing.gnupg.keyName") as String?
        val passphrase = project.property("signing.gnupg.passphrase") as String?
        if (executable != null && keyName != null && passphrase != null) {
            project.plugins.apply(SIGNING_PLUGIN_ID)
            project.extensions.getByType(SigningExtension::class.java).also {
                it.useGpgCmd()
                it.sign(*publications)
            }
        }
    }

    companion object {

        @JvmStatic
        val ID = "maven_publish"

        @JvmStatic
        private val ASSET_MODULE_MAVEN_PUBLISH_HYPER_TOML =
            "${HyperPlugin.NAME}/module/maven_publish.hyper.toml"

        @JvmStatic
        val MAVEN_PUBLISH_PLUGIN_ID = "maven-publish"

        @JvmStatic
        val SIGNING_PLUGIN_ID = "signing"

        @JvmStatic
        val HYPER_PUBLICATION_NAME = "Hyper"

        @JvmStatic
        val PROJECT_BUILD_LOCAL_REPOSITORY_NAME = "ProjectBuild"

        @JvmStatic
        private val REAL_PUBLISH_TASK_ID =
            "publish${HYPER_PUBLICATION_NAME}PublicationTo${PROJECT_BUILD_LOCAL_REPOSITORY_NAME}Repository"
    }
}