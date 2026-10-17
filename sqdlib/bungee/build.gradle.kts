plugins {
    id("shared")
    alias(libs.plugins.run.waterfall) apply true
    alias(libs.plugins.shadowjar)
}


dependencies {
    api(project(":sqdlib-core")) {
        exclude("net.kyori")
    }

    compileOnly(libs.bungee)
}

tasks {
    runWaterfall {
        waterfallVersion(libs.versions.waterfall.get())
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("bungee.yml") {
            expand(props)
        }
    }
}