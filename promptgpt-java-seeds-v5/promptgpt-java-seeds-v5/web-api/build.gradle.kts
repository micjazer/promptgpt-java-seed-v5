
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("java")
}
dependencies {
    implementation(project(":core-domain"))
    implementation(project(":providers-spi"))
    implementation(project(":provider-echo"))
    implementation(project(":provider-openai"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")
    implementation("com.bucket4j:bucket4j-core:8.10.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks.withType<Test> { useJUnitPlatform() }
