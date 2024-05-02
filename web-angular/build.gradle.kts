import com.github.gradle.node.npm.task.NpmTask

plugins { id("build.common.node-conventions") }

val backendApiBindingConfig: Configuration by configurations.creating {}

dependencies { backendApiBindingConfig(project(":server-api", "zip")) }

val angularBuildDir = layout.buildDirectory.dir("dist/web-angular/browser")
val angularApiBindingCode = layout.buildDirectory.dir("generated/angular-binding")

val copyBeApiBindingTask =
    tasks.register<Sync>("copyBeApiBinding") {
        dependsOn(backendApiBindingConfig)
        from(zipTree(backendApiBindingConfig.singleFile))
        into(angularApiBindingCode)
    }

tasks.prepareEnv {
    dependsOn(tasks.npmInstall)
    dependsOn(copyBeApiBindingTask)
}

// ---------------------------------------------------------------------------------------------------------------------
// Build
// ---------------------------------------------------------------------------------------------------------------------

val angularConfigFiles =
    listOf(
        ".eslintrc.js",
        ".prettierignore",
        ".prettierrc.json",
        "angular.json",
        "package.json",
        "package-lock.json",
        "tsconfig.json",
        "tsconfig.app.json",
        "tsconfig.spec.json",
    )

val buildAngularTask =
    tasks.register<NpmTask>("buildAngular") {
        dependsOn(tasks.prepareEnv)
        npmCommand.set(listOf("run", "build"))
        inputs.files(angularConfigFiles)
        inputs.dir("src")
        inputs.dir(angularApiBindingCode)
        outputs.dir(angularBuildDir)
    }

val buildAngularZip =
    tasks.register<Zip>("assemble") {
        dependsOn(buildAngularTask)
        from(angularBuildDir)
        destinationDirectory.set(layout.buildDirectory.dir("zip"))
        archiveBaseName.set("${rootProject.name}-${project.name}")
    }

configurations.create("zip").let { artifacts.add(it.name, buildAngularZip.get()) }

// ---------------------------------------------------------------------------------------------------------------------
// Test
// ---------------------------------------------------------------------------------------------------------------------

val testTask =
    tasks.register<NpmTask>("test") {
        dependsOn(tasks.prepareEnv)
        inputs.files(angularConfigFiles)
        inputs.dir("src")
        inputs.dir(angularApiBindingCode)
        outputs.upToDateWhen { true }

        npmCommand.set(listOf("run", "test:ci"))
    }

tasks.checkFormat {
    inputs.dir("src")
    inputs.files(angularConfigFiles)
}

tasks.register("check") { dependsOn(testTask, tasks.checkFormat) }
