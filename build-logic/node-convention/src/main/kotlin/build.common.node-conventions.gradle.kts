import com.github.gradle.node.npm.task.NpmTask

plugins { id("com.github.node-gradle.node") }

node {
    version.set(NodeConstants.NODE_VERSION)
    download.set(true)
    workDir.set(layout.getNodeDirectory())
    fastNpmInstall.set(true)
}

val npmInstallTask = configureNpmInstall()

val prepareEnvTask = tasks.register("prepareEnv") { dependsOn(npmInstallTask) }

// ---------------------------------------------------------------------------------------------------------------------
// Formatting
// ---------------------------------------------------------------------------------------------------------------------

val formatTask =
    tasks.register<NpmTask>("format") {
        group = "formatting"
        dependsOn(prepareEnvTask)

        npmCommand.set(listOf("run", "lint:fix"))
    }

val checkFormatTask =
    tasks.register<NpmTask>("checkFormat") {
        group = "verification"
        dependsOn(prepareEnvTask)
        inputs.dir("src")
        outputs.upToDateWhen { true }

        npmCommand.set(listOf("run", "lint"))
    }
