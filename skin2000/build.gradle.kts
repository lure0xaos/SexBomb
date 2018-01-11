val javaVersion: String = JavaVersion.VERSION_16.toString()

group = "gargoyle.sexbomb"
version = "1.0"
description = "sexbomb"

plugins {
    `java-library`
    kotlin("jvm") version ("1.6.21")

    id("org.javamodularity.moduleplugin") version ("1.8.11")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(kotlin("bom")))

    api(project(":services"))
}

tasks.compileJava {
    modularity.inferModulePath.set(true)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    options.encoding = Charsets.UTF_8.toString()
}

tasks.compileKotlin {
    destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
    targetCompatibility = javaVersion
    kotlinOptions {
        jvmTarget = javaVersion
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
