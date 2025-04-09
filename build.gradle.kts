plugins {
    alias(libs.plugins.elhub.gradle.plugin)
    alias(libs.plugins.google.cloud.tools.jib)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.liquibase.plugin)
    alias(libs.plugins.gradle.docker)
}

buildscript {
    dependencies {
        classpath(libs.database.liquibase.core)
    }
}

dependencies {
    // Ktor
    implementation(libs.bundles.ktor)
    // Koin
    implementation(libs.bundles.dependency.injection)
    ksp(libs.di.koin.ksp.compiler)
    // Serialization
    implementation(libs.bundles.serialization)
    // Database
    implementation(libs.bundles.database)
    // Liquibase
    liquibaseRuntime(libs.database.liquibase.core)
    liquibaseRuntime(libs.cli.picocli)
    liquibaseRuntime(libs.serialization.yaml.snakeyaml)
    liquibaseRuntime(libs.database.postgresql)
    // Documentation
    implementation(libs.bundles.documentation)
    // Observability
    implementation(libs.bundles.logging)
    implementation(libs.bundles.monitoring)
    // Unit Testing
    testImplementation(libs.database.postgresql)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.ktor.server.test.host)
    testImplementation(libs.test.kotest.runner.junit5)
    testImplementation(libs.test.kotest.assertions.core)
    testImplementation(libs.test.testcontainers)
    testImplementation(libs.test.testcontainers.postgres)
}

ksp {
    arg("KOIN_CONFIG_CHECK", "true")
    arg("KOIN_DEFAULT_MODULE", "true")
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

liquibase {
    jvmArgs = arrayOf(
        "-Dliquibase.command.url=jdbc:postgresql://localhost:5432/postgres",
        "-Dliquibase.command.username=postgres",
        "-Dliquibase.command.password=postgres",
        "-Dliquibase.command.driver=org.postgresql.Driver",
        "-Dliquibase.command.changeLogFile=db/db-changelog.yaml"
    )
    activities.register("main")
}

dockerCompose {
    createNested("database").apply {
        useComposeFiles.set(listOf("db/db-compose.yaml"))
    }
}

tasks.named("run").configure {
    dependsOn(tasks.named("databaseComposeUp"))
    dependsOn(tasks.named("liquibaseUpdate"))
}
