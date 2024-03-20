import java.util.*

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.9.20"
}

group = "team.idealstate.hyper"
version = "1.0.0"

val kotlinVersion = "1.9.20"
kotlin {
    coreLibrariesVersion = kotlinVersion
    val javaVersion = 8
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
        vendor.set(JvmVendorSpec.AZUL)
    }
    compilerOptions.javaParameters.set(true)
}

repositories {
    mavenLocal()
    val repositoriesProperties = Properties()
    file("${projectDir}/../repositories.properties").inputStream().use {
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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$kotlinVersion")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    val jacksonVersion = "2.17.0"
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}