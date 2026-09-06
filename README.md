# sqd-lib

A library (Specifically for paper plugins) so I don't have to copy and paste the same things again and again

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
    implementation("net.chamosmp.sqdlib:sqd-lib:1.1.0") // Replace 1.1.0 with the latest version
}
```

### Relocating it

```kotlin
plugins {
    id("com.gradleup.shadow") version "9.6.1" // Adding the shadow plugin
}

tasks {
    shadowJar { // The shadowJar configuration
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }
        
        relocate("net.chamosmp.sqdlib", "(your plugin group).libs")
    }
}

```