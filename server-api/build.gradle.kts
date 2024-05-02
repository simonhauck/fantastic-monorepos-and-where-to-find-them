import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins { alias(libs.plugins.openApiGenerator) }

val generateApiBaseDir = "generated/typescript-fetch"

tasks.register<Delete>("clean") { delete { layout.buildDirectory } }

val angularBindingName = "angular-binding"
val angularBindingPath = layout.buildDirectory.dir("generated/$angularBindingName")
val openApiJsonFile = "$projectDir/src/main/resources/openapi.json"

// ---------------------------------------------------------------------------------------------------------------------
// Angular client
// ---------------------------------------------------------------------------------------------------------------------

openApiGenerate {
    generatorName = "typescript-angular"
    inputSpec = openApiJsonFile
    outputDir = angularBindingPath.map { it.asFile.path }
    cleanupOutput = true

    configOptions =
        mapOf(
            "npmName" to "@monorepo-api",
            "snapshot" to "false",
            "npmVersion" to "1.0.0",
            "ngVersion" to "17.0.0",
            "serviceSuffix" to "",
            "serviceFileSuffix" to ".controller"
        )
}

val generateApiBindingTask = tasks.withType<GenerateTask> {}

val zipAngularBindingTask =
    tasks.register<Zip>("assemble") {
        dependsOn(generateApiBindingTask)
        from(angularBindingPath)
        destinationDirectory.set(layout.buildDirectory.dir("zip"))
        archiveBaseName.set(angularBindingName)
    }

configurations.create("zip").let { artifacts.add(it.name, zipAngularBindingTask.get()) }
