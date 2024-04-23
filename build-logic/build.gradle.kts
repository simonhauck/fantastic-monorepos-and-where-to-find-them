// This is taken from: https://github.com/gradle/gradle/blob/master/build-logic/gradle.properties
description = "Provides plugins that are used by Gradle subprojects"

tasks.register("assemble") { dependsOn(subprojects.map { "${it.name}:assemble" }) }

tasks.register("check") { dependsOn(subprojects.map { "${it.name}:check" }) }

val clean by
    tasks.registering {
        val buildSrcPropertiesFile = layout.projectDirectory.file("gradle.properties")
        val rootPropertiesFile = layout.projectDirectory.file("../gradle.properties")
        doLast {
            val buildSrcProperties = readProperties(buildSrcPropertiesFile.asFile)
            val rootProperties = readProperties(rootPropertiesFile.asFile)
            val jvmArgs =
                listOf(buildSrcProperties, rootProperties)
                    .map { it.getProperty("org.gradle.jvmargs") }
                    .toSet()
            if (jvmArgs.size > 1) {
                throw GradleException(
                    "gradle.properties and build-logic/gradle.properties have different org.gradle.jvmargs " +
                        "which may cause two daemons to be spawned on CI and in IDEA. " +
                        "Use the same org.gradle.jvmargs for both builds."
                )
            }
        }
    }

fun readProperties(propertiesFile: File) =
    java.util.Properties().apply { propertiesFile.inputStream().use { fis -> load(fis) } }
