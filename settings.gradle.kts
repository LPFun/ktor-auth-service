rootProject.name = "auth"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val kotlinVersion: String by settings
            version("kodein", "7.18.0")
            version("cache2k", "2.6.1.Final")

            plugin("jvm", "org.jetbrains.kotlin.jvm").version(kotlinVersion)
            plugin("serialization", "org.jetbrains.kotlin.plugin.serialization").version(kotlinVersion)
            plugin("ktor", "io.ktor.plugin").version("2.2.3")

            library("cache2k", "org.cache2k", "cache2k-core").versionRef("cache2k")
            library("kodein", "org.kodein.di", "kodein-di").versionRef("kodein")
            library("kodein-ktor", "org.kodein.di", "kodein-di-framework-ktor-server-jvm").versionRef("kodein")

            version("jupiter", "5.8.1")
            library("jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("jupiter")
            library("jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("jupiter")

            library("kotlin-test-junit", "org.jetbrains.kotlin", "kotlin-test-junit").version(kotlinVersion)

            library("commons-codec","commons-codec","commons-codec").version("1.15")

            library("mockk","io.mockk", "mockk").version("1.13.4")
        }
    }
}

include("app")
include("repo-inmemory")
include("common")
include("transport")
include("logic")
include("security")
