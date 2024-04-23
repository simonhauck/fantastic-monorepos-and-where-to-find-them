import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("build.common.artifactory")
    id("build.common.kotlin-conventions")
    id("build.common.spring-conventions")
    id("build.common.terraform")
    application
}

dependencies {
    implementation(libs.springStarter)
    implementation(libs.cdktf)
}

application {
    mainClass.set(
        "com.github.cryptojuenger.template.infrastructure.hetzner.InfrastructureApplicationKt"
    )
}

val cdktfGetTask =
    tasks.create<NpmTask>("cdktfGet") {
        group = "build"
        dependsOn(tasks.npmInstall)
        inputs.files("package.json", "package-lock.json", "cdktf.json")
        outputs.dir(layout.projectDirectory.dir("src/main/java"))
        outputs.dir(layout.projectDirectory.dir("terraform.hcloudprovider"))
        outputs.dir(layout.projectDirectory.dir("src/main/resources/terraform"))
        npmCommand.set(listOf("run", "generate-tf-cdk"))
    }

tasks.prepareEnv { dependsOn(cdktfGetTask) }

tasks.ktfmtCheckMain { dependsOn(tasks.prepareEnv) }

tasks.processResources { dependsOn(tasks.prepareEnv) }

tasks.compileKotlin { dependsOn(tasks.prepareEnv) }

tasks.bootRun {
    val nodeEnvVariable = "PATH" to NodeConstants.getNodeExecutable(layout).get().asFile
    environment = mapOf(nodeEnvVariable)
}
