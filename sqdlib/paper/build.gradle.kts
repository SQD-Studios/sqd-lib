plugins {
    id("shared")
    id("xyz.jpenilla.run-paper") version "3.1.0" apply true
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    api(project(":sqdlib-core"))

    // Plugin APIs
    compileOnlyApi("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnlyApi("me.clip:placeholderapi:2.12.3")

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

tasks {
    runServer {
        minecraftVersion("26.2")
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}