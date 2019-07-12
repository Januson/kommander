import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.41"
}

group = "org.kommander"
version = "1.0-SNAPSHOT"
val moduleName by extra("org.kommander")

repositories {
    mavenCentral()
}

val junitVersion = "5.3.2"

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.slf4j:slf4j-api:1.7.26")
    testImplementation("org.slf4j:slf4j-simple:1.7.26")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Wrapper> {
    gradleVersion = "5.5.1"
    distributionType = Wrapper.DistributionType.ALL
}
