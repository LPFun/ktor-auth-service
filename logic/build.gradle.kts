plugins {
    alias(libs.plugins.jvm)
}

dependencies {
    implementation(projects.common)

    implementation(libs.kodein)
    implementation("com.crowdproj:kotlin-cor:0.5.5")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}