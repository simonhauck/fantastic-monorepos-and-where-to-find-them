pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // Specify toolchains: https://github.com/gradle/foojay-toolchains
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

includeBuild("build-logic")

include("server", "server-api", "web-angular", "example-duplicate-build-code")

rootProject.name = "fantastic-monorepos-and-where-to-find-them"

include("reverse-string-library")
