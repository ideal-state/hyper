package team.idealstate.hyper.gradle.hyper.entity.build

import team.idealstate.hyper.gradle.hyper.entity.build.publication.License
import team.idealstate.hyper.gradle.hyper.entity.build.publication.Organization
import team.idealstate.hyper.gradle.hyper.entity.build.publication.Scm

/**
 * <p>Publication</p>
 *
 * <p>创建于 2024/3/18 17:56</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Publication(
    val organization: Organization?,
    val scm: Scm,
    val license: License,
)
