dependencies {
    compileOnly("org.jetbrains:annotations:24.0.0")

    hyperImplementation("org.apache.maven.resolver:maven-resolver-supplier:1.9.18")

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

//publishing {
//    publications {
//        create<MavenPublication>("maven") {
//            groupId = project.group.toString()
//            artifactId = project.name
//            version = project.version.toString()
//
//            pom {
//                name.set(project.name)
//                description.set("提供一些基础的公共工具类")
//                packaging = "jar"
//                url.set("https://github.com/ideal-state/hyper-commons")
//                inceptionYear.set("2024")
//
//                organization {
//                    name.set("ideal-state")
//                    url.set("https://github.com/ideal-state")
//                }
//
//                developers {
//                    developer {
//                        id.set("ketikai")
//                        name.set("ketikai")
//                        email.set("ketikai@idealstate.team")
//                    }
//                }
//
//                licenses {
//                    license {
//                        name.set("Apache License 2.0")
//                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
//                    }
//                }
//
//                scm {
//                    url.set("https://github.com/ideal-state/hyper-commons")
//                    tag.set(version)
//                    connection.set("scm:git:git@github.com:ideal-state/hyper-commons.git")
//                    developerConnection.set("scm:git:git@github.com:ideal-state/hyper-commons.git")
//                }
//
//                withXml {
//                    var dependenciesNode: Node? = null
//                    val compileDependencyIds = mutableSetOf<String>()
//                    configurations.compileClasspath.get()
//                            .resolvedConfiguration.firstLevelModuleDependencies.forEach { dependency ->
//                                if (dependenciesNode == null) {
//                                    dependenciesNode = asNode().appendNode("dependencies")
//                                }
//                                val dependencyNode = dependenciesNode!!.appendNode("dependency")
//                                dependencyNode.appendNode("groupId", dependency.moduleGroup)
//                                dependencyNode.appendNode("artifactId", dependency.moduleName)
//                                dependencyNode.appendNode("version", dependency.moduleVersion)
//                                dependencyNode.appendNode("scope", "compile")
//                                compileDependencyIds.add("${dependency.moduleGroup}:${dependency.moduleName}:${dependency.moduleVersion}")
//                            }
//                    configurations.runtimeClasspath.get()
//                            .resolvedConfiguration.firstLevelModuleDependencies.forEach { dependency ->
//                                if (!compileDependencyIds.contains("${dependency.moduleGroup}:${dependency.moduleName}:${dependency.moduleVersion}")) {
//                                    if (dependenciesNode == null) {
//                                        dependenciesNode = asNode().appendNode("dependencies")
//                                    }
//                                    val dependencyNode = dependenciesNode!!.appendNode("dependency")
//                                    dependencyNode.appendNode("groupId", dependency.moduleGroup)
//                                    dependencyNode.appendNode("artifactId", dependency.moduleName)
//                                    dependencyNode.appendNode("version", dependency.moduleVersion)
//                                    dependencyNode.appendNode("scope", "runtime")
//                                }
//                            }
//                }
//            }
//
//            artifact(sourcesJar)
//            artifact(javadocJar)
//            artifact(jar)
//        }
//    }
//    repositories {
//        maven {
//            name = "local"
//            url = uri("file://${projectDir}/build/repository")
//        }
//    }
//}
//
//signing {
//    useGpgCmd()
//    sign(publishing.publications)
//}