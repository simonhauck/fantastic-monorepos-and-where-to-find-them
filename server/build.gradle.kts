import com.github.psxpaul.task.ExecJoin
import com.github.psxpaul.task.JavaExecFork
import org.springdoc.openapi.gradle.plugin.OpenApiGeneratorTask

plugins {
    id("build.common.kotlin-conventions")
    id("build.common.spring-conventions")

    // Generate open api doc
    alias(libs.plugins.openApiDoc)
    `jvm-test-suite`
}

configurations.compileOnly { extendsFrom(configurations.annotationProcessor.get()) }

// TODO Simon.Hauck 2024-04-23 - Add frontend to

dependencies {
    implementation(libs.bundles.springStarterWeb)
    annotationProcessor(libs.springAnnotationProcessor)

    implementation(libs.springDocOpenApi)
}

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
// OpenAPI Swagger
// ---------------------------------------------------------------------------------------------------------------------

// User another port to have it not clashing with running instances
openApi {
    val apiGeneratedPort = 59186
    apiDocsUrl.set("http://localhost:$apiGeneratedPort/v3/api-docs/openapi.json")
    outputDir.set(file("${project(":server-api").projectDir}/src/main/resources"))

    customBootRun { args.set(listOf("--server.port=$apiGeneratedPort")) }
}

// Run open api generate always when requested
tasks.withType<OpenApiGeneratorTask> { outputs.upToDateWhen { false } }

tasks.withType<JavaExecFork> {
    notCompatibleWithConfigurationCache("JavaExecFork is not yet supported")
}

tasks.withType<ExecJoin> { notCompatibleWithConfigurationCache("ExecJoin is not yet supported") }
