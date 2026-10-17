plugins {
    id("test")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("sqdlib")
                url.set("https://github.com/SQD-Studios/sqd-lib")
                scm {
                    url.set("https://github.com/SQD-Studios/sqd-lib")
                    connection.set("scm:git:https://github.com/SQD-Studios/sqd-lib.git")
                    developerConnection.set("scm:git:ssh://git@github.com/SQD-Studios/sqd-lib.git")
                }
            }
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