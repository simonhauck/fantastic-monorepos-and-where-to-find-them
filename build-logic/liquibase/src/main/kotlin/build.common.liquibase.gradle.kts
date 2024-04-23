import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.time.LocalDate
import kotlin.io.path.createFile
import kotlin.io.path.writeText

@DisableCachingByDefault(because = "A new changelog should always be generated")
abstract class GenerateNewLiquibaseChangeLog() : DefaultTask() {
    @get:InputDirectory abstract val liquibaseRoot: RegularFileProperty

    @TaskAction
    fun generate() {
        val date = LocalDate.now()
        val monthString = date.monthValue.toString().padStart(2, '0')

        val basePath = liquibaseRoot.get().asFile.resolve("${date.year}/$monthString").absolutePath
        val fileName = findAvailableChangeLogName(basePath, date)
        val userName = System.getenv("USERNAME") ?: "Default User"
        val timeStamp = System.currentTimeMillis()

        val template =
            this.javaClass.classLoader
                .getResource("liquibase-template.xml")
                ?.readText(StandardCharsets.UTF_8)
                ?: throw GradleException("Could not read template")

        val finalContent =
            template.replace("\$timeStamp", "$timeStamp").replace("\$userName", userName)

        Path.of(fileName).apply { parent.toFile().mkdirs() }.createFile().writeText(finalContent)
    }

    private fun findAvailableChangeLogName(basePath: String, date: LocalDate): String {
        val dayValue = date.dayOfMonth.toString().padStart(2, '0')
        return (1..99)
            .map { it.toString().padStart(2, '0') }
            .map { "$basePath/$dayValue-$it-changelog.xml" }
            .first { !File(it).exists() }
    }
}
