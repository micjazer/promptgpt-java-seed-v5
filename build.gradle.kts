
plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
}
allprojects {
    group = "com.codeiguanas"
    version = "0.1.0-SNAPSHOT"
    repositories { mavenCentral() }
}
subprojects {
    apply(plugin = "java")
    java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }
    tasks.withType<Test> { useJUnitPlatform() }
}
