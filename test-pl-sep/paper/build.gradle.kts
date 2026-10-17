plugins {
    id("test")
    alias(libs.plugins.run.paper) apply true
}

dependencies {
    compileOnly(project(":sqdlib-paper"))

    compileOnly(libs.paperapi)
}

tasks.runServer {
    minecraftVersion(libs.versions.minecraft.get())
}