import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.internal.os.OperatingSystem

private const val basePathNode = ".cache/nodejs"

fun ProjectLayout.getNodeDirectory() = buildDirectory.dir(basePathNode)

fun Project.configureNpmInstall(): Task {
    return tasks.getByName("npmInstall") {
        inputs.file("package.json")
        outputs.files("package-lock.json", "node_modules/.package-lock.json")
    }
}

object NodeConstants {
    const val NODE_VERSION = "20.11.0"

    fun getNodeExecutable(layout: ProjectLayout): Provider<RegularFile> {
        val currentOs = OperatingSystem.current()
        if (currentOs.isLinux)
            return layout.buildDirectory.file("$basePathNode/node-v$NODE_VERSION-linux-x64/bin")

        TODO("Not yet implemented")
    }
}

// This is how to configure a plugin in  plugin class
// project.pluginManager.apply(NodePlugin::class.java)
// project.extensions.configure(NodeExtension::class.java) {
//    version.set(NODE_VERSION)
//    download.set(true)
//    workDir.set(project.layout.buildDirectory.dir(NODE_DOWNLOAD_DIR))
//    fastNpmInstall.set(true)
// }
