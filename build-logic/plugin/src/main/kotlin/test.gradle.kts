plugins {
    id("java-library")
}

group = "net.chamosmp.sqdlib"
version = "3.2.0-BETA"

repositories {
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven("https://jitpack.io")
    mavenCentral()
}