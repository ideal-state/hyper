plugins {
    hyper_java
    hyper_maven_publish
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}