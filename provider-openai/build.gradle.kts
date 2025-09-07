
plugins { id("java") }
dependencies {
    implementation(project(":providers-spi"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}
