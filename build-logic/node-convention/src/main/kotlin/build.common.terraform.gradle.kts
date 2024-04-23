plugins { id("com.github.node-gradle.node") }

node {
    version.set(NodeConstants.NODE_VERSION)
    download.set(true)
    workDir.set(layout.getNodeDirectory())
    fastNpmInstall.set(true)
}

val npmInstallTask = configureNpmInstall()

val prepareEnvTask = tasks.register("prepareEnv") { dependsOn(npmInstallTask) }
