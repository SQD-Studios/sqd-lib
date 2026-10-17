plugins {
    id("java-library")
}

group = "net.chamosmp.sqdlib"
version = "3.2.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://maven.chamosmp.net/releases")
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withSourcesJar()
}