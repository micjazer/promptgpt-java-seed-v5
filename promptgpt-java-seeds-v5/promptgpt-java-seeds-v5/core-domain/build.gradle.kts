
plugins { id("java") }
dependencies {
    implementation("com.github.spullara.mustache.java:compiler:0.9.10")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("com.knuddels:jtokkit:0.6.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.assertj:assertj-core:3.26.0")
}
