import Build_common_liquibase_gradle.GenerateNewLiquibaseChangeLog
import com.github.psxpaul.task.ExecJoin
import com.github.psxpaul.task.JavaExecFork
import org.springdoc.openapi.gradle.plugin.OpenApiGeneratorTask

plugins {
    id("build.common.artifactory")
    id("build.common.kotlin-conventions")
    id("build.common.spring-conventions")
    id("build.common.docker")
    id("build.common.liquibase")

    // Generate open api doc
    alias(libs.plugins.openApiDoc)
    `jvm-test-suite`
}

configurations.compileOnly { extendsFrom(configurations.annotationProcessor.get()) }

// TODO Simon.Hauck 2024-04-23 - Add frontend to

dependencies {
    implementation(libs.bundles.springStarterWeb)
    implementation(libs.springActuator)
    implementation(libs.springDataJdbc)
    implementation(libs.kotlinLogging)

    implementation(libs.liquibase)
    runtimeOnly(libs.postgresDriver)
    runtimeOnly(libs.h2Driver)

    developmentOnly(libs.springDevTools)
    developmentOnly(libs.springDockerCompose)
    annotationProcessor(libs.springAnnotationProcessor)
    annotationProcessor(libs.springAnnotationProcessor)

    implementation(libs.springDocOpenApi)
}

tasks.bootRun { workingDir = rootProject.projectDir }

// ---------------------------------------------------------------------------------------------------------------------
// Testing
// ---------------------------------------------------------------------------------------------------------------------

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by
            getting(JvmTestSuite::class) {
                useJUnitJupiter()
                dependencies {
                    // Alternatively check this out:
                    // https://stackoverflow.com/questions/70448998/gradle-integration-test-suite-depending-on-testimplementation-dependencies
                    implementation.bundle(libs.bundles.springTestCore)
                }
            }

        val integrationTest by
            register<JvmTestSuite>("integrationTest") {
                dependencies {
                    implementation.bundle(libs.bundles.springTestCore)
                    implementation(project())
                }
            }
    }
}

@Suppress("UnstableApiUsage") tasks.check { dependsOn(testing.suites.named("integrationTest")) }

// ---------------------------------------------------------------------------------------------------------------------
// Generate database changelog
// ---------------------------------------------------------------------------------------------------------------------

tasks.register<GenerateNewLiquibaseChangeLog>("newChangeLog") {
    liquibaseRoot = layout.projectDirectory.file("src/main/resources/db/changelog")
}

// ---------------------------------------------------------------------------------------------------------------------
// Docker Build
// ---------------------------------------------------------------------------------------------------------------------

jib {
    to { image = "cryptojuenger/example" }
    container { ports = listOf("8080") }
}

// ---------------------------------------------------------------------------------------------------------------------
// OpenAPI Swagger
// ---------------------------------------------------------------------------------------------------------------------

// User another port to have it not clashing with running instances
openApi {
    val apiGeneratedPort = 59186
    apiDocsUrl.set("http://localhost:$apiGeneratedPort/v3/api-docs/openapi.json")
    outputDir.set(file("${project(":server-api").projectDir}/src/main/resources"))

    customBootRun {
        args.set(
            listOf(
                "--server.port=$apiGeneratedPort",
                "--spring.profiles.active=h2",
                "--spring.docker.compose.enabled=false"
            )
        )
    }
}

// Run open api generate always when requested
tasks.withType<OpenApiGeneratorTask> { outputs.upToDateWhen { false } }

tasks.withType<JavaExecFork> {
    notCompatibleWithConfigurationCache("JavaExecFork is not yet supported")
}

tasks.withType<ExecJoin> { notCompatibleWithConfigurationCache("ExecJoin is not yet supported") }
