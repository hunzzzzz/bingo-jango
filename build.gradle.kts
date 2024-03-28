import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
    kotlin("kapt") version "1.8.22"
}

group = "team.b2"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

val awsVersion = "2.2.6.RELEASE"
val jjwtVersion = "0.12.5"
val kotestVersion = "5.5.5"
val kotestExtensionVersion = "1.1.3"
val mockkVersion = "1.13.8"
val queryDslVersion = "5.0.0"
val slackVersion = "1.38.1"
val stompVersion = "2.3.3-1"
val swaggerVersion = "2.3.0"
val webSocketVersion = "1.1.2"

dependencies {
    // ACTUATOR
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // AWS
    implementation("org.springframework.cloud:spring-cloud-starter-aws:${awsVersion}")
    // MAIL
    implementation("org.springframework.boot:spring-boot-starter-mail")
    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    // OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    // SECURITY
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
    // SLACK
    implementation("com.slack.api:slack-api-client:${slackVersion}")
    // SWAGGER
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${swaggerVersion}")
    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // TEST
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("io.kotest:kotest-assertions-core-jvm:${kotestVersion}")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:${kotestVersion}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${kotestExtensionVersion}")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    // VALIDATION
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // WEBSOCKET
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.webjars:sockjs-client:${webSocketVersion}")
    implementation("org.webjars:stomp-websocket:${stompVersion}")
    // WEB
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test>().configureEach() {
    useJUnitPlatform()
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<BootJar>("bootJar") {
        archiveFileName = "bingo-jango.jar"
    }
}