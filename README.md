# sqd-lib

[![Build](https://img.shields.io/github/actions/workflow/status/SQD-Studios/sqd-lib/gradle.yml?style=flat-square)](https://github.com/SQD-Studios/sqd-lib/actions)

A library (Specifically for paper plugins) so I don't have to copy and paste the same things again and again

## It's lightweight

What did you think, it's like only 10 classes. The finished build output is as small as 21,1 Kilobytes so it's not
really going to affect your jar output

## Using sqd-lib

You aren't supposed to use this as a separate plugin. You only need to shadow it.

### Adding the dependency

```kotlin
repositories {
    maven {
        name = "chamosmpRepoReleases"
        url = uri("https://maven.chamosmp.net/releases")
    }
}

dependencies {
    // Replace 2.0.0 with the latest version, and you can replace paper with velocity
    implementation("net.chamosmp.sqdlib:sqdlib-paper:2.0.0")
}
```

### Relocating it

```kotlin
plugins {
    // Adding the shadow plugin
    id("com.gradleup.shadow") version "9.6.1"
}

tasks {
    // The shadowJar configuration
    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("net.chamosmp.sqdlib", "(your plugin group).libs.sqdlib")
    }
}

```