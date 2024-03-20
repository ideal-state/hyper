package team.idealstate.hyper.gradle.module.java.task

import org.gradle.api.tasks.SourceSetContainer
import org.gradle.work.DisableCachingByDefault

/**
 * <p>HyperSourcesJar</p>
 *
 * <p>创建于 2024/3/19 18:19</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
open class HyperSourcesJar : AbstractHyperJar() {

    override fun copy() {
        archiveClassifier.set("sources")
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        from(sourceSets.named(MAIN_SOURCE_SET_ID).get().allSource)
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperSourcesJar"
    }
}