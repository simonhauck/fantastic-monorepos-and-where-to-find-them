import com.github.gradle.node.npm.task.NpmTask

plugins { id("com.github.node-gradle.node") }

node {
    version.set("20.11.0")
    download.set(true)
    workDir.set(layout.buildDirectory.dir(".cache/nodejs"))
    fastNpmInstall.set(true)
}

val npmInstallTask =
    tasks.getByName("npmInstall") {
        inputs.file("package.json")
        outputs.files("package-lock.json", "node_modules/.package-lock.json")
    }

val prepareEnvTask = tasks.register("prepareEnv") { dependsOn(npmInstallTask) }

// ---------------------------------------------------------------------------------------------------------------------
// Formatting
// ---------------------------------------------------------------------------------------------------------------------

tasks.register<NpmTask>("format") {
    group = "formatting"
    dependsOn(prepareEnvTask)

    npmCommand.set(listOf("run", "lint:fix"))
}

tasks.register<NpmTask>("checkFormat") {
    group = "verification"
    dependsOn(prepareEnvTask)
    inputs.dir("src")
    outputs.upToDateWhen { true }

    npmCommand.set(listOf("run", "lint"))
}
