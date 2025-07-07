import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(libs.liquibase.core)
    }
}

plugins {
    id("idea")
    id("application")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.liquibase)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.ekorn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.cloud.dependencies.get().toString())
    }
}

dependencies {
    implementation(libs.bundles.runtime)
    liquibaseRuntime(libs.bundles.liquibase)
    testImplementation(libs.bundles.test)
}

application {
    mainClass = "com.ekorn.MainKt"
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}

liquibase {
    activities.register("main") {
        arguments = mapOf(
            "changelogFile" to "src/main/resources/db/changelog.xml",
            "url" to "jdbc:postgresql://127.0.0.2:15432/price-aggregator",
            "schema" to "public",
            "driver" to "org.postgresql.Driver",
            "username" to "postgres",
            "password" to "postgres",
        )
    }
}
