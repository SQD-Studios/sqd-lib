plugins {
    id("shared")
    alias(libs.plugins.run.paper) apply true
    alias(libs.plugins.shadowjar)
}

dependencies {
    api(project(":sqdlib-core"))

    // Plugin APIs
    compileOnly(libs.vault)
    compileOnly(libs.placeholderapi)

    compileOnly(libs.paperapi)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}