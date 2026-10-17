plugins {
    id("shared")
    alias(libs.plugins.run.paper) apply true
    alias(libs.plugins.shadowjar)
}

dependencies {
    api(project(":sqdlib-core")) {
        exclude("net.kyori")
    }

    // Plugin APIs
    compileOnly(libs.vault)

    compileOnly(libs.spigotapi)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}