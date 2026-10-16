plugins {
    id("shared")
    id("xyz.jpenilla.run-waterfall") version "3.1.0" apply true
    id("com.gradleup.shadow") version "9.6.1"
}


dependencies {
    api(project(":sqdlib-core")) {
        exclude("net.kyori")
    }

    compileOnly("net.md-5:bungeecord-api:26.1-R0.1-SNAPSHOT")
}

tasks {
    runWaterfall {
        waterfallVersion("1.21")
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}