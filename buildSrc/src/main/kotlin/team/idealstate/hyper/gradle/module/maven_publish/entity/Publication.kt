package team.idealstate.hyper.gradle.module.maven_publish.entity

import team.idealstate.hyper.gradle.module.maven_publish.entity.publication.License
import team.idealstate.hyper.gradle.module.maven_publish.entity.publication.Organization
import team.idealstate.hyper.gradle.module.maven_publish.entity.publication.Scm

/**
 * <p>Publication</p>
 *
 * <p>创建于 2024/3/18 17:56</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Publication(
    val description: String?,
    val url: String?,
    val packaging: String,
    val inceptionYear: String,
    val organization: Organization?,
    val scm: Scm,
    val license: License?,
)
