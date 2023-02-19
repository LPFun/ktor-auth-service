plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0-RC")
    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}