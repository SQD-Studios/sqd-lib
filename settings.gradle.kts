rootProject.name = "sqd-lib"

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

fun importProjectsIn(folder: File) {
    val name = folder.name
    rootDir.resolve(name).listFiles { it.isDirectory }?.forEach {
        include(":${name}-${it.name}")
        project(":${name}-${it.name}").projectDir = it
    }
}

importProjectsIn(rootDir.resolve("test-pl-sep"))
importProjectsIn(rootDir.resolve("test-plugin"))
importProjectsIn(rootDir.resolve("sqdlib"))

includeBuild("build-logic")