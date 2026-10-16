plugins {
    id("java-library")
}

group = "net.chamosmp.sqdlib"
version = "3.2.0"

repositories {
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://maven.chamosmp.net/releases")
    mavenCentral()
}