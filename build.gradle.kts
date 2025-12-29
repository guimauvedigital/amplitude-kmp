import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.cocoapods) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenPublish) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    group = project.findProperty("GROUP") ?: ""
    version = project.findProperty("VERSION_NAME") ?: "0.0.1-SNAPSHOT"

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        }
    }
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
