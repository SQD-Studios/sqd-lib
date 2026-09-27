plugins {
    id("test")
    id("net.fabricmc.fabric-loom") version "1.18-SNAPSHOT"
}

dependencies {
    implementation(project(":sqdlib-fabric"))

    minecraft("com.mojang:minecraft:26.2")
    implementation("net.fabricmc:fabric-loader:0.19.3")
    implementation("net.fabricmc.fabric-api:fabric-api:0.161.0+26.2")
}