plugins {
    alias(libs.plugins.jvm)
}

dependencies {
    implementation(projects.common)

    implementation(libs.kodein)
    implementation("com.crowdproj:kotlin-cor:0.5.5")

    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}