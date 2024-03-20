package team.idealstate.hyper.gradle.module.java.task

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
        super.dependsOn(project.tasks.named(PROCESS_RESOURCES_ID), project.tasks.named(CLASSES_ID))
    }

    override fun copy() {
        archiveClassifier.set("")
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        from(
            sourceSets.named(MAIN_SOURCE_SET_ID).get().output.classesDirs,
            project.tasks.named(PROCESS_RESOURCES_ID)
        )
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperJar"
    }
}