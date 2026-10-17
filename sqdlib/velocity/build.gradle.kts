plugins {
    id("shared")
    alias(libs.plugins.run.velocity) apply true
    alias(libs.plugins.shadowjar)
}


dependencies {
    api(project(":sqdlib-core"))
    api(libs.bukkitconfig)

    compileOnly(libs.velocityapi)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }
}