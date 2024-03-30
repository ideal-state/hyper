plugins {
    hyper_java
    hyper_maven_publish
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")

    val jacksonVersion = "2.17.0"
    hyperImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-toml:$jacksonVersion")
    hyperApi("org.apache.maven.resolver:maven-resolver-supplier:1.9.18")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}