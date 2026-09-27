plugins {
    id("java")
}

group = "net.chamosmp.sqdlib.test"
version = "1.1.3"

repositories {
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    mavenCentral()
}