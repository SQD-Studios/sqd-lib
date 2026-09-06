plugins {
    id("java")
    id("maven-publish")
}

group = "net.chamosmp.sqdlib"
version = "1.0.1"

repositories {
    maven {
        name = "PaperMC"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("me.clip:placeholderapi:2.12.3")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "ChamoSMP-Releases"
            url = uri("https://maven.chamosmp.net/releases")
            credentials {
                username = System.getenv("REPOSILITE_USER")
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
        maven {
            name = "ChamoSMP-Snapshots"
            url = uri("https://maven.chamosmp.net/snapshots")
            credentials {
                username = System.getenv("REPOSILITE_USER")
                password = System.getenv("REPOSILITE_TOKEN")
            }
        }
    }
}