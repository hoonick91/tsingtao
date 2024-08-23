plugins {
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "me.hoonick"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("io.github.microutils:kotlin-logging:3.0.5")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

    api("org.springframework.boot:spring-boot-starter-test")
    api("org.jetbrains.kotlin:kotlin-test-junit5")
    api("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
