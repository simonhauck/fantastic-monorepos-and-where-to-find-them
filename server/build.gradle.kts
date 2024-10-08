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

val frontendApp: Configuration by configurations.creating {}

dependencies {
    annotationProcessor(libs.springAnnotationProcessor)

    implementation(libs.bundles.springStarterWeb)
    implementation(libs.springDocOpenApi)

    implementation(project(":reverse-string-library"))

    // This is an example of a "traditional" dependency. The dependency and version must be
    // explicitly set. This can lead to inconsistencies if used in multiple projects.
    // This can be replaced with values in libs.version.toml
    
    // testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation(libs.bundles.springTestCore)

    if (isProd()) {
        frontendApp(project(":web-angular", "zip"))
    } else {
        frontendApp(files("./mockFrontend/dummy-frontend.zip"))
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Add resources
// ---------------------------------------------------------------------------------------------------------------------

// To perform a file operation in a doFirst or doLast you have to use these injected operations to
// be compatible with the configuration cache.
// Full list:
// https://docs.gradle.org/current/userguide/configuration_cache.html#config_cache:requirements
interface Injected {
    @get:Inject val archiveOperations: ArchiveOperations
}

val frontendDirectory = "staticResources/frontend"
val tempFrontendDirectory = "tmp/angular-frontend"

val frontendZipName = "web-resources.zip"
val copyFrontendTask =
    tasks.register<Sync>("copyFrontend") {
        val buildFrontendDirectory = layout.buildDirectory.dir(tempFrontendDirectory)
        from(frontendApp)
        rename(".*", frontendZipName)
        into(buildFrontendDirectory)
    }

val unzipFrontendTask =
    tasks.register<Sync>("unzipFrontend") {
        dependsOn(copyFrontendTask)

        val injected = project.objects.newInstance<Injected>()
        val zip =
            injected.archiveOperations.zipTree(
                layout.buildDirectory.file("$tempFrontendDirectory/$frontendZipName")
            )
        val buildDirUnzipped = layout.buildDirectory.dir(frontendDirectory)
        from(zip)
        into(buildDirUnzipped)
    }

fun Copy.addFrontendToJar() {
    dependsOn(unzipFrontendTask)
    from(layout.buildDirectory.dir(frontendDirectory)) { into("static") }
}

tasks.processResources { addFrontendToJar() }

// ---------------------------------------------------------------------------------------------------------------------
// OpenAPI Swagger
// ---------------------------------------------------------------------------------------------------------------------

// To make our life easier, we used a code-first approach here.
// We write the Controller / Api in Kotlin, and then run the generateOpenApiDocs task.
// The task writes the generated openApiJson into the server-api module where it will be used to
// generate a new API client.
// The compatibility of the specified API and the actually implemented API is ensured with a Test.
//
// Alternatively you can generate a spring-kotlin client in the server-api module.

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

// ---------------------------------------------------------------------------------------------------------------------
// Utility
// ---------------------------------------------------------------------------------------------------------------------

private fun isProd(): Boolean {
    return project.properties.containsKey("prod")
}
