package team.idealstate.hyper.gradle.hyper.task

import org.gradle.api.tasks.SourceSetContainer
import org.gradle.work.DisableCachingByDefault

/**
 * <p>HyperJar</p>
 *
 * <p>创建于 2024/3/18 21:28</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
open class HyperJar : AbstractHyperJar() {

    init {
        super.dependsOn(project.tasks.named(CLASSES_ID))
    }

    override fun copy() {
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        from(sourceSets.named("main").get().output.classesDirs)
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperJar"

        @JvmStatic
        protected val CLASSES_ID = "classes"
    }
}