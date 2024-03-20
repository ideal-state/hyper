package team.idealstate.hyper.gradle.entity

/**
 * <p>Build</p>
 *
 * <p>创建于 2024/3/17 22:27</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Build(
    val version: Version,
    val developers: List<Developer>,
)
