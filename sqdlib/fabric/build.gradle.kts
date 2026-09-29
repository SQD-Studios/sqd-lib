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
        val props = mapOf("version" to project.version)
        inputs.properties(props)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}