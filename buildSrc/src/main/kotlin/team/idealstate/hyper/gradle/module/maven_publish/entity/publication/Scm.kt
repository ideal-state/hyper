package team.idealstate.hyper.gradle.module.maven_publish.entity.publication

/**
 * <p>Scm</p>
 *
 * <p>创建于 2024/3/18 17:48</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
data class Scm(
    val url: String,
    val connection: String,
    val developerConnection: String,
)
