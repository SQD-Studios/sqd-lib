plugins {
    id("test")
    id("xyz.jpenilla.run-paper") version "3.1.0" apply true
}

dependencies {
    compileOnly(project(":sqdlib-paper"))

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}

tasks.runServer {
    minecraftVersion("26.2")
}