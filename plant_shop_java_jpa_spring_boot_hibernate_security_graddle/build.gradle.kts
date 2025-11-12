plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "com.planteshop"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.0.0")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars:webjars-locator:0.46")

    implementation("net.datafaker:datafaker:2.0.2")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    runtimeOnly("org.postgresql:postgresql:42.7.3")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("plant-shop.jar")
}

tasks.register<JavaExec>("seed") {
    group = "application"
    description = "Populate the database using SeedRunner"
    mainClass.set("com.planteshop.seed.SeedRunner")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf("-Dspring.profiles.active=seed")
}

tasks.register<Copy>("syncProdJar") {
    group = "build"
    description = "Copy bootJar output into the jar/ directory"
    dependsOn(tasks.named("bootJar"))
    from(tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar"))
    into(layout.projectDirectory.dir("jar"))
}

tasks.named("clean") {
    doLast {
        project.layout.projectDirectory.dir("jar").asFile.listFiles()?.forEach { file ->
            if (file.isFile && file.name.endsWith(".jar")) {
                file.delete()
            }
        }
    }
}
