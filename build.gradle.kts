import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.sonarqube.gradle.SonarQubeExtension

plugins {
    kotlin("multiplatform") version "1.5.20"
    id("org.sonarqube") version "3.3"
    jacoco
}

group = "com.github.nanodeath"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
//    explicitApi()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }

        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        compilations.all {
            compileKotlinTask.kotlinOptions.freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
        browser {
            commonWebpackConfig {
//                cssSupport.enabled = true
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
//    val hostOs = System.getProperty("os.name")
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" -> macosX64("native")
//        hostOs == "Linux" -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
            }
        }
        val jvmTest by getting {
            dependencies {
//                implementation("io.kotest:kotest-runner-junit5:")
            }
        }
        val jsMain by getting
        val jsTest by getting
//        val nativeMain by getting
//        val nativeTest by getting
    }
}

configure<SonarQubeExtension> {
    properties {
        property("sonar.projectKey", "nanodeath_crowlib-kt")
        property("sonar.organization", "nanodeath")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
    }
}

tasks.named<KotlinJsCompile>("compileKotlinJs").configure {
    kotlinOptions.moduleKind = "plain"
}

tasks.named("jvmTest").configure {
    finalizedBy("jacocoTestReport")
}

// https://github.com/vanniktech/gradle-android-junit-jacoco-plugin/issues/140#issuecomment-704672510
tasks.create<JacocoReport>("jacocoTestReport") {
    dependsOn("jvmTest")
    group = "verification"
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(file("${buildDir}/classes/kotlin/jvm/main"))
    sourceDirectories.setFrom(files("src/commonMain", "src/jvmMain"))
    executionData.setFrom(files("${buildDir}/jacoco/jvmTest.exec"))
}
