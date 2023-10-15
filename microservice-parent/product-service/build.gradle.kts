plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.0.7")
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
    compileOnly("org.projectlombok:lombok:1.18.26")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.0.4")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    implementation("org.testcontainers:testcontainers-bom:1.18.1")
    testImplementation("org.testcontainers:mongodb:1.18.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
