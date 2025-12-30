import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.mavenPublish)
}

kotlin {
    // Explicit API mode (like Android SDK)
    explicitApi()

    // Android target
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        }
        publishLibraryVariants("release")
    }

    // iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "AmplitudeKMP"
            isStatic = true
        }
    }

    // CocoaPods configuration for local Amplitude-Swift SDK
    cocoapods {
        version = "1.0.1"
        summary = "Amplitude Analytics KMP SDK"
        homepage = "https://github.com/amplitude/amplitude-kmp"
        ios.deploymentTarget = libs.versions.iosDeploymentTarget.get()

        framework {
            baseName = "AmplitudeKMP"
            isStatic = true
        }

        // Use local source-based AmplitudeCore instead of binary XCFramework
        pod("AmplitudeCore") {
            source = path(project.rootProject.file("../AmplitudeCore-Swift"))
        }

        // Use official AmplitudeSwift and AnalyticsConnector
        pod("AnalyticsConnector") {
            version = "~> 1.3.0"
        }
        pod("AmplitudeSwift") {
            version = "~> " + libs.versions.amplitudeSwift.get()
        }
    }

    // Source sets
    sourceSets {
        // Common
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        // Android
        val androidMain by getting {
            dependencies {
                implementation(libs.amplitude.android)
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        // iOS - Create a common iOS source set
        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        // Tests
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.mockk)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.junit.jupiter)
                implementation(libs.junit.jupiter.api)
                implementation(libs.kotlin.test.junit)
                implementation(libs.robolectric)
                implementation(libs.test.core)
                implementation(libs.test.ext.junit)
            }
        }

        val iosTest by creating {
            dependsOn(commonTest)
        }

        val iosX64Test by getting {
            dependsOn(iosTest)
        }

        val iosArm64Test by getting {
            dependsOn(iosTest)
        }

        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }

    // Compiler options
    targets.all {
        compilations.all {
            compilerOptions.configure {
                // Enable expect/actual classes
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    // iOS source sets require ExperimentalForeignApi
    sourceSets.all {
        languageSettings.apply {
            if (name.lowercase().startsWith("ios")) {
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
            }
        }
    }
}

android {
    namespace = "com.amplitude.kmp"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    lint {
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
    }
}

// Maven Publishing (following Android SDK pattern)
mavenPublishing {
    coordinates(artifactId = "analytics-kmp")

    pom {
        name.set("Amplitude Analytics KMP")
        description.set("Amplitude Kotlin Multiplatform SDK for Analytics")
        url.set(project.findProperty("POM_URL")?.toString() ?: "")

        licenses {
            license {
                name.set(project.findProperty("POM_LICENSE_NAME")?.toString() ?: "")
                url.set(project.findProperty("POM_LICENSE_URL")?.toString() ?: "")
                distribution.set(project.findProperty("POM_LICENSE_DIST")?.toString() ?: "")
            }
        }

        developers {
            developer {
                id.set(project.findProperty("POM_DEVELOPER_ID")?.toString() ?: "")
                name.set(project.findProperty("POM_DEVELOPER_NAME")?.toString() ?: "")
                email.set(project.findProperty("POM_DEVELOPER_EMAIL")?.toString() ?: "")
                organization.set(project.findProperty("POM_DEVELOPER_ORG")?.toString() ?: "")
                organizationUrl.set(project.findProperty("POM_DEVELOPER_ORG_URL")?.toString() ?: "")
            }
        }

        scm {
            url.set(project.findProperty("POM_SCM_URL")?.toString() ?: "")
            connection.set(project.findProperty("POM_SCM_CONNECTION")?.toString() ?: "")
            developerConnection.set(project.findProperty("POM_SCM_DEV_CONNECTION")?.toString() ?: "")
        }
    }

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
        )
    )
}

// JUnit 5 support for tests (like Android SDK)
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
