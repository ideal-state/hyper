package team.idealstate.hyper.gradle.module.java.task

import org.apache.commons.lang3.time.DateFormatUtils
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.work.DisableCachingByDefault
import team.idealstate.hyper.gradle.HyperExtension
import team.idealstate.hyper.gradle.HyperPlugin
import java.util.*

/**
 * <p>AbstractHyperJar</p>
 *
 * <p>创建于 2024/3/19 22:44</p>
 * @author ketikai
 * @since 1.0.0
 * @version 1.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
abstract class AbstractHyperJar : Jar() {

    init {
        group = HyperPlugin.GROUP
    }

    override fun copy() {
        val developers = project.extensions.getByType(HyperExtension::class.java)
            .build.developers.joinToString(", ", "'", "'") {
                "(${it.id}) ${it.name} <${it.email}>"
            }
        val attributes = manifest.attributes
        val attrs = LinkedHashMap<String, Any>(5 + attributes.size)
        attrs["Hyper-Group"] = project.group
        attrs["Hyper-Name"] = project.name
        attrs["Hyper-Version"] = project.version
        attrs["Hyper-Developers"] = developers
        attrs["Hyper-Built-By"] = DateFormatUtils.format(Date(), "yyyy-MM-dd HH:mm:ssZ")
        attrs["Hyper-Java-Version"] = project.extensions.getByType(JavaPluginExtension::class.java)
            .targetCompatibility.majorVersion
        attrs += attributes
        attributes.clear()
        manifest.attributes(attrs)
        super.copy()
    }

    companion object {

        @JvmStatic
        protected val MAIN_SOURCE_SET_ID = "main"

        @JvmStatic
        protected val TEST_SOURCE_SET_ID = "test"

        @JvmStatic
        protected val PROCESS_RESOURCES_ID = "processResources"

        @JvmStatic
        protected val CLASSES_ID = "classes"

        @JvmStatic
        protected val DOC_ID = "javadoc"
    }
}