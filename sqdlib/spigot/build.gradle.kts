plugins {
    id("shared")
    id("xyz.jpenilla.run-paper") version "3.1.0" apply true
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    api(project(":sqdlib-core")) {
        exclude("net.kyori")
    }

    // Plugin APIs
    compileOnlyApi("com.github.MilkBowl:VaultAPI:1.7.1")

    compileOnly("org.spigotmc:spigot-api:26.+")
}

tasks {
    runServer {
        minecraftVersion("26.2")
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}