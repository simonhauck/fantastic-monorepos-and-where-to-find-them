import com.github.psxpaul.task.ExecJoin
import com.github.psxpaul.task.JavaExecFork
import org.springdoc.openapi.gradle.plugin.OpenApiGeneratorTask

plugins {
    id("build.common.kotlin-conventions")
    id("build.common.spring-conventions")

    // Generate open api doc
    alias(libs.plugins.openApiDoc)
}

configurations.compileOnly { extendsFrom(configurations.annotationProcessor.get()) }

// TODO Simon.Hauck 2024-04-23 - Add frontend to

dependencies {
    annotationProcessor(libs.springAnnotationProcessor)

    implementation(libs.bundles.springStarterWeb)
    implementation(libs.springDocOpenApi)

    testImplementation(libs.bundles.springTestCore)

}

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


// Whitelist tasks that are not compatible with configuration cache
// If not whitelisted they will fail the build
tasks.withType<JavaExecFork> {
    notCompatibleWithConfigurationCache("JavaExecFork is not yet supported")
}

tasks.withType<ExecJoin> { notCompatibleWithConfigurationCache("ExecJoin is not yet supported") }
