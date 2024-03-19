package team.idealstate.hyper.gradle.hyper.task

import org.gradle.work.DisableCachingByDefault

/**
 * <p>HyperDocJar</p>
 *
 * <p>创建于 2024/3/19 18:19</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
open class HyperDocJar : AbstractHyperJar() {

    init {
        super.dependsOn(project.tasks.named(DOC_ID))
    }

    override fun copy() {
        archiveClassifier.set(DOC_ID)
        from(project.tasks.named(DOC_ID))
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperDocJar"

        @JvmStatic
        protected val DOC_ID = "javadoc"
    }
}