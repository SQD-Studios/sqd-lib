# sqd-lib

[![Build](https://img.shields.io/github/actions/workflow/status/SQD-Studios/sqd-lib/gradle.yml?style=flat-square)](https://github.com/SQD-Studios/sqd-lib/actions)

A library so I don't have to copy and paste the same things again and again

## It's lightweight

What did you think, it's like only 10 classes? The finished build output is as small as 21,1 Kilobytes so it's not
really going to affect your jar output

## Using this sqdlib

### Adding the repository

```kotlin
repositories {
    maven {
        name = "chamosmpRepoReleases"
        url = uri("https://maven.chamosmp.net/releases")
    }
}
```

### Adding the dependency (Plugin)

```kotlin
plugins {
    // Adding the shadow plugin
    id("com.gradleup.shadow") version "9.6.1"
}

dependencies {
    // Replace 2.0.0 with the latest version, and you can replace paper with velocity
    implementation("net.chamosmp.sqdlib:sqdlib-paper:3.0.0")
}

tasks {
    // The shadowJar configuration
    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "(your plugin group).libs.sqdlib")
    }
}
```

### Adding the dependency (Mod)

```kotlin
dependencies {
    // Replace 3.0.0 with the latest version
    implementation("net.chamosmp.sqdlib:sqdlib-fabric:3.0.0")
}
```