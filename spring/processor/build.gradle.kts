import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.spring") version "1.8.20"
}

group = "dev.bright"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

extra["springCloudVersion"] = "2022.0.1"
extra["otelJavaVersion"] = "1.24.0"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

configurations.create("runtimeAgent")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // for jackson autoconfiguration
    implementation("org.springframework:spring-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")

    implementation(platform("io.opentelemetry:opentelemetry-bom:${property("otelJavaVersion")}"))
    implementation("io.opentelemetry:opentelemetry-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    configurations["runtimeAgent"]("io.opentelemetry.javaagent:opentelemetry-javaagent:${property("otelJavaVersion")}")
    configurations["runtimeAgent"]("io.opentelemetry:opentelemetry-extension-kotlin:1.22.0")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}


val copyRuntimeToBuild = tasks.register<Copy>("copyRuntimeToBuild") {
    from(configurations["runtimeAgent"])
    into(buildDir)
}

tasks.withType<BootRun> {
    val javaOpts = System.getenv("BOOT_RUN_JAVA_OPTS")
    if (!javaOpts.isNullOrEmpty()) {
        jvmArgs = jvmArgs!! + javaOpts.split(' ')
    }
    dependsOn(copyRuntimeToBuild.name)
}
