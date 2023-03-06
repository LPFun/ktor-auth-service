plugins {
    alias(libs.plugins.jvm)
}

dependencies {
    implementation(projects.common)

    implementation(libs.commons.codec)
    implementation(libs.kodein)
    implementation("com.auth0:java-jwt:4.3.0")


    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}