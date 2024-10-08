// This is an example of a "traditional" gradle file.
// It uses a lot of sections that can be extracted to convention plugins to reduce code duplications
// like plugins, repositories, kotlin configuration, testing configuration, etc.

plugins {
    kotlin("jvm")
    id("com.adarshr.test-logger")
    id("com.ncorti.ktfmt.gradle")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
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
    compilerOptions { freeCompilerArgs = listOf("-Xjsr305=strict") }
}

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

tasks.withType<Test> { useJUnitPlatform() }

// ---------------------------------------------------------------------------------------------------------------------
// Spring config
// ---------------------------------------------------------------------------------------------------------------------

springBoot {
    // If the time always changes, the task can not be cached
    buildInfo { excludes.set(listOf("time")) }
}

// ---------------------------------------------------------------------------------------------------------------------
// Dependencies
// ---------------------------------------------------------------------------------------------------------------------

// Dependencies can be extracted to versions catalogs like libs.versions.toml.
// Dependencies that must be used together can be bundled so that resolving them is even easier.
dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    implementation(project(":reverse-string-library"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.2")
}
