group = "team.idealstate.hyper"

subprojects {
    group = rootProject.group

    val buildScript = project.file("build.gradle.kts")
    if (!buildScript.exists() || buildScript.isDirectory) {
        return@subprojects
    }

    apply {
//        plugin("team.idealstate.hyper.gradle.plugin")
    }
}