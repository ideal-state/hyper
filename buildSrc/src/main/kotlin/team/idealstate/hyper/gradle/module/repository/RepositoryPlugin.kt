package team.idealstate.hyper.gradle.module.repository

import team.idealstate.hyper.gradle.ConfigurablePlugin
import team.idealstate.hyper.gradle.ConfigurationSupport
import team.idealstate.hyper.gradle.HyperPlugin
import team.idealstate.hyper.gradle.module.repository.entity.Repositories
import team.idealstate.hyper.gradle.module.repository.entity.Repository

/**
 * <p>RepositoryPlugin</p>
 *
 * <p>创建于 2024/3/20 16:49</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
open class RepositoryPlugin : ConfigurablePlugin<Repositories>(
    ConfigurationSupport.TOML,
    ASSET_MODULE_REPOSITORY_HYPER_TOML,
    Repositories::class.java
) {

    override fun apply() {
        val repositories = config
        project.repositories.also { handler ->
            handler.mavenLocal()
            for (repository in repositories.repositories) {
                when (repository.type) {
                    Repository.Type.MAVEN -> handler.maven {
                        it.name = repository.name
                        it.url = project.uri(repository.url)
                        if (repository.username != null && repository.password != null) {
                            it.credentials { credentials ->
                                credentials.username = repository.username
                                credentials.password = repository.password
                            }
                        }
                    }

                    Repository.Type.IVY -> handler.ivy {
                        it.name = repository.name
                        it.url = project.uri(repository.url)
                        if (repository.username != null && repository.password != null) {
                            it.credentials { credentials ->
                                credentials.username = repository.username
                                credentials.password = repository.password
                            }
                        }
                    }
                }
            }
            handler.mavenCentral()
        }
    }

    companion object {

        @JvmStatic
        val ID = "repository"

        @JvmStatic
        private val ASSET_MODULE_REPOSITORY_HYPER_TOML =
            "${HyperPlugin.NAME}/module/repository.hyper.toml"
    }
}