plugins {
    hyper_java
    hyper_maven_publish
}

dependencies {
    hyperApi(project(":modules:command:hyper-command-api"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}