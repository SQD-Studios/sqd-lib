plugins {
    id("test")
    alias(libs.plugins.run.velocity) apply true
    alias(libs.plugins.shadowjar)
}

dependencies {
    implementation(project(":sqdlib-velocity"))

    compileOnly(libs.velocityapi)
    annotationProcessor(libs.velocityapi)
}

tasks {
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }

    shadowJar { // The shadowJar configuration
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "libs")
    }
}