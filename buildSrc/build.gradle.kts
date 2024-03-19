import java.util.*

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.9.23"
}

group = "team.idealstate.hyper"
version = "1.0.0"

repositories {
    mavenLocal()
    val repositoriesProperties = Properties()
    file("${projectDir}/repositories.properties").inputStream().use {
        repositoriesProperties.load(it)
    }
    repositoriesProperties.forEach {
        maven {
            name = it.key as String
            url = uri(it.value as String)
        }
    }
    mavenCentral()
}

dependencies {
    val jacksonVersion = "2.17.0"
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}

kotlin {
    val javaVersion = 8
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
        vendor.set(JvmVendorSpec.AZUL)
    }
    this.compilerOptions.javaParameters.set(true)
}

gradlePlugin {
    plugins {
        create("HyperPlugin") {
            id = "hyper-plugin"
            implementationClass = "team.idealstate.hyper.gradle.HyperPlugin"
        }
    }
}