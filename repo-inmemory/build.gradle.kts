plugins {
    alias(libs.plugins.jvm)
}

dependencies {
    implementation(projects.common)

    implementation(libs.kodein)
    implementation (libs.cache2k)
    runtimeOnly (libs.cache2k)

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}