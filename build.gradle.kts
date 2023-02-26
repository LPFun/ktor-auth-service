group = "com.dark"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}

tasks.withType<JavaCompile> {
    targetCompatibility = "11"
}