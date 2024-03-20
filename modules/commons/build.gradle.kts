plugins {
    hyper_maven_publish
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")

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