import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.power-assert") version "2.1.20"
    application

}

group = "com.gani"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    implementation(kotlin("reflect"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    implementation("io.ktor:ktor-client-core:3.0.2")
    implementation("io.ktor:ktor-client-java:3.0.2")
    implementation("io.ktor:ktor-client-websockets:3.0.2")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
        optIn.add("kotlinx.coroutines.ExperimentalAtomicApi")
        optIn.add("kotlinx.coroutines.ExperimentalUuidApi")
    }
}

java.sourceSets["test"].java {
    srcDir("src/main/kotlin")
}

// Uncomment to use Loom
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")

}
