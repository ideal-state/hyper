includeBuild("buildSrc") {
    name = "build-src"
}

rootProject.name = "hyper"

pluginManagement {
    repositories {
        mavenLocal()
        maven {
            name = "sonatype-public"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

buildscript {
    repositories {
        mavenLocal()
        maven {
            name = "sonatype-public"
            url = uri("https://oss.sonatype.org/content/groups/public/")
        }
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

fun Settings.includeModules(modules: String = "modules") {
    fun findBuildScripts(modulesDirectory: File): List<File> {
        val moduleIds = mutableListOf<File>()
        for (file in modulesDirectory.listFiles()!!) {
            if (file.isDirectory) {
                moduleIds.addAll(findBuildScripts(file))
            } else if (file.name == "build.gradle.kts") {
                moduleIds.add(file)
            }
        }
        return moduleIds
    }

    val modulesDirectory = file("${rootProject.projectDir}/${modules}/")
    if (!modulesDirectory.exists()) {
        throw IllegalStateException("模块目录不存在")
    }
    if (!modulesDirectory.isDirectory) {
        throw IllegalStateException("模块目录必须是文件夹类型")
    }
    val modulesPath = modulesDirectory.absolutePath
    println()
    println("> Modules: $modules")
    val buildScripts = findBuildScripts(modulesDirectory)
    val prefixLength = modulesPath.length - modules.length - 1
    buildScripts.forEach {
        val moduleId = it.parentFile.absolutePath
            .substring(prefixLength).replace('\\', ':').replace('/', ':')
        if (moduleId.isBlank() || moduleId == ":") {
            return
        }
        include(moduleId)
        val project = project(moduleId)
        project.name = "${rootProject.name}${moduleId.substring(modules.length + 1)}"
            .replace(':', '-')
        println(">> included $moduleId (${project.name})")
    }
    println()
}

includeModules()
