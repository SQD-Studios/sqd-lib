plugins {
    id("test")
    alias(libs.plugins.run.paper) apply true
    alias(libs.plugins.shadowjar)
}

dependencies {
    implementation(project(":sqdlib-paper"))

    compileOnly(libs.paperapi)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
    }

    shadowJar { // The shadowJar configuration
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "libs")
    }
}