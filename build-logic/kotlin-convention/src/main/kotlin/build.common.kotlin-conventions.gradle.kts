import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.adarshr.test-logger")
    id("com.ncorti.ktfmt.gradle")
}

repositories { mavenCentral() }

// ---------------------------------------------------------------------------------------------------------------------
// Kotlin config
// ---------------------------------------------------------------------------------------------------------------------

kotlin {
    jvmToolchain {
        this.languageVersion.set(JavaLanguageVersion.of(21))
        this.vendor.set(JvmVendorSpec.ADOPTIUM)
    }
}

tasks.withType<KotlinCompile> { kotlinOptions { freeCompilerArgs = listOf("-Xjsr305=strict") } }

// ---------------------------------------------------------------------------------------------------------------------
// Kotlin formatter
// ---------------------------------------------------------------------------------------------------------------------

ktfmt {
    // KotlinLang style - 4 space indentation - From kotlinlang.org/docs/coding-conventions.html
    kotlinLangStyle()

    blockIndent.set(4)
    continuationIndent.set(4)

    removeUnusedImports.set(true)
}

tasks.register("checkFormat") {
    group = "verification"
    dependsOn(tasks.ktfmtCheck)
    outputs.upToDateWhen { true }
}

tasks.register("format") {
    group = "formatting"
    dependsOn(tasks.ktfmtFormat)
}

// ---------------------------------------------------------------------------------------------------------------------
// Testing config
// ---------------------------------------------------------------------------------------------------------------------

tasks.withType<Test> {
    useJUnitPlatform()
}

