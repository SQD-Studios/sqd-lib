plugins {
    id("test")
    id("xyz.jpenilla.run-paper") version "3.1.0"
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    implementation(project(":sqdlib-paper"))

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

tasks {
    runServer {
        minecraftVersion("26.2")
    }

    shadowJar { // The shadowJar configuration
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "(your plugin group).libs")
    }
}