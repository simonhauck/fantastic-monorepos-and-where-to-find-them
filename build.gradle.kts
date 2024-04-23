import java.util.*

plugins { id("build.common.kotlin-conventions") apply false }

allprojects {
    group = "fantastic-monorepos-and-where-to-find-them"
    version = readVersionFromFile(file("${rootProject.projectDir.resolve("version.properties")}"))
}

private fun readVersionFromFile(file: File): String = readProperties(file).getProperty("version")

private fun readProperties(propertiesFile: File) =
    Properties().apply { propertiesFile.inputStream().use { load(it) } }
