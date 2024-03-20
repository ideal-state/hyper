package team.idealstate.hyper.gradle.module.java.task

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.provider.Property
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

    private val docSource: Property<Any> = objectFactory.property(Any::class.java)

    init {
        super.dependsOn(project.tasks.named(DOC_ID))
    }

    fun docSource(action: Action<Property<Any>>) {
        action.execute(docSource)
        val source = docSource.orNull
        if (source is Task) {
            val javadoc = project.tasks.named(DOC_ID).get()
            javadoc.enabled = source == javadoc
            dependsOn(source)
        } else if (source == null) {
            project.tasks.named(DOC_ID).get().enabled = true
        }
    }

    override fun copy() {
        archiveClassifier.set(DOC_ID)
        val source = docSource.orNull
        if (source == null) {
            from(project.tasks.named(DOC_ID))
        } else {
            from(source)
        }
        super.copy()
    }

    companion object {
        @JvmStatic
        val NAME = "hyperDocJar"
    }
}