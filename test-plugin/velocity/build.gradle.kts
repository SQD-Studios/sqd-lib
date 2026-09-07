plugins {
    id("test")
    id("xyz.jpenilla.run-velocity") version "3.1.0"
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    implementation(project(":sqdlib-velocity"))

    compileOnly("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
}

tasks {
    runVelocity {
        velocityVersion("4.1.1-SNAPSHOT")
    }

    shadowJar { // The shadowJar configuration
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "(your plugin group).libs")
    }
}