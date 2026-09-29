plugins {
    id("shared")
    id("net.fabricmc.fabric-loom") version "1.18-SNAPSHOT"
}

dependencies {
    api(project(":sqdlib-core"))

    minecraft("com.mojang:minecraft:26.2")
    compileOnly("net.fabricmc:fabric-loader:0.19.3")

    implementation("org.yaml:snakeyaml:2.2")
    api(include("net.kyori:adventure-platform-fabric:7.1.1")!!)
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}