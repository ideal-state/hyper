plugins {
    hyper_kotlin_jvm
    id("java-gradle-plugin")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")

//    hyperImplementation("org.apache.maven.resolver:maven-resolver-supplier:1.9.18")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testCompileOnly("org.jetbrains:annotations:24.0.0")
}

tasks.test {
    useJUnitPlatform()
}

val manifestAttributes: MutableMap<String, *> = linkedMapOf(
    "Multi-Release" to true,
)

tasks.hyperJar {
    manifest.attributes(manifestAttributes)
}