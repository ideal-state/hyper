import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version ("8.1.1")
    id("java-gradle-plugin")
    hyper_kotlin_jvm
    hyper_maven_publish
}

val includedBuild = gradle.includedBuild("build-src")
val taskSourcesJar: TaskReference = includedBuild.task(":sourcesJar")
val prefix = "build/libs/${project.name}-1.0.0"
val fileSourcesJar = File(includedBuild.projectDir, "$prefix-sources.jar")
val taskDocJar: TaskReference = includedBuild.task(":docJar")
val fileDocJar = File(includedBuild.projectDir, "$prefix-javadoc.jar")
val taskJar: TaskReference = includedBuild.task(":jar")
val fileJar = File(includedBuild.projectDir, "$prefix.jar")
val manifestAttributes: Attributes = tasks.hyperJar.get().manifest.attributes

tasks.create<ShadowJar>("copySourcesJar") {
    dependsOn(taskSourcesJar)
    archiveClassifier.set("copySourcesJar")
    from(fileSourcesJar)
    manifest.attributes(manifestAttributes)
}

tasks.hyperSourcesJar {
    dependsOn("copySourcesJar")
    doLast {
        val jar = tasks.hyperSourcesJar.get().archiveFile.get().asFile
        jar.delete()
        val shadowJar = tasks.named<ShadowJar>("copySourcesJar").get().archiveFile.get().asFile
        shadowJar.renameTo(jar)
    }
}

tasks.dokkaHtml {
    enabled = false
}

tasks.create<ShadowJar>("copyDocJar") {
    dependsOn(taskDocJar)
    archiveClassifier.set("copyDocJar")
    from(fileDocJar)
    manifest.attributes(manifestAttributes)
}

tasks.hyperDocJar {
    dependsOn("copyDocJar")
    doLast {
        val jar = tasks.hyperDocJar.get().archiveFile.get().asFile
        jar.delete()
        val shadowJar = tasks.named<ShadowJar>("copyDocJar").get().archiveFile.get().asFile
        shadowJar.renameTo(jar)
    }
}

tasks.create<ShadowJar>("copyJar") {
    dependsOn(taskJar)
    archiveClassifier.set("copyJar")
    from(fileJar)
    manifest.attributes(manifestAttributes)
}

tasks.hyperJar {
    dependsOn("copyJar")
    doLast {
        val jar = tasks.hyperJar.get().archiveFile.get().asFile
        jar.delete()
        val shadowJar = tasks.named<ShadowJar>("copyJar").get().archiveFile.get().asFile
        shadowJar.renameTo(jar)
    }
}