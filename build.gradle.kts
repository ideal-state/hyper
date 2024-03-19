import java.util.*

group = "team.idealstate.hyper"

val repositoriesProperties = Properties()
file("${rootProject.projectDir}/repositories.properties").inputStream().use {
    repositoriesProperties.load(it)
}

subprojects {
    group = rootProject.group

    val buildScript = project.file("build.gradle.kts")
    if (!buildScript.exists() || buildScript.isDirectory) {
        return@subprojects
    }

    apply {
        plugin("java")
        plugin("java-library")
        plugin("maven-publish")
        plugin("signing")
        plugin("hyper-plugin")
    }

    repositories {
        mavenLocal()
        repositoriesProperties.forEach {
            maven {
                name = it.key as String
                url = uri(it.value as String)
            }
        }
        mavenCentral()
    }
}