package team.idealstate.hyper.gradle.hyper.entity

import team.idealstate.hyper.gradle.hyper.entity.build.Developer
import team.idealstate.hyper.gradle.hyper.entity.build.Publication
import team.idealstate.hyper.gradle.hyper.entity.build.Version

/**
 * <p>Build</p>
 *
 * <p>创建于 2024/3/17 22:27</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Build(
    val description: String = "",
    val version: Version,
    val publication: Publication?,
    val developers: Set<Developer>,
)
