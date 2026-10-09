plugins {
    id("shared")
    id("xyz.jpenilla.run-velocity") version "3.1.0" apply true
    id("com.gradleup.shadow") version "9.6.1"
}


dependencies {
    api(project(":sqdlib-core"))

    compileOnly("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
}

tasks {
    runVelocity {
        velocityVersion("4.1.1-SNAPSHOT")
    }

    processResources {
        val props = mapOf("version" to project.version)
        inputs.properties(props)

        filesMatching("velocity-plugin.json") {
            expand(props)
        }
    }
}