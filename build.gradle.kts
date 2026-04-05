plugins {
    kotlin("jvm")
    kotlin("plugin.spring") apply false
    kotlin("plugin.jpa") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    id("org.jlleitschuh.gradle.ktlint") apply false
}

allprojects {
    group = "${property("projectGroup")}"
    version = "${property("applicationVersion")}"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val mockitoAgent: Configuration by configurations.creating

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudDependenciesVersion")}")
        }
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        val testRuntimeOnly by configurations

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter")

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.ninja-squad:springmockk:5.0.1")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        mockitoAgent("org.mockito:mockito-core:5.11.0") { isTransitive = false }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of("${property("javaVersion")}")
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
        }
    }

    tasks.named<Jar>("bootJar").configure {
        enabled = false
    }

    tasks.named<Jar>("jar").configure {
        enabled = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", "test")
        jvmArgs(
            "-javaagent:${mockitoAgent.asPath}",
            "-Xshare:off" // CDS 기능 비활성화 ->  "OpenJDK 64-Bit Server VM warning: Sharing is only supported for ..." 에러 해결
        )
    }
}