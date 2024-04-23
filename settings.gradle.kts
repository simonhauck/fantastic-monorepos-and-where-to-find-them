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

buildCache { local { removeUnusedEntriesAfterDays = 10 } }

includeBuild("build-logic")

include("server", "server-api", "web-nextjs")

rootProject.name = "fantastic-monorepos-and-where-to-find-them"
