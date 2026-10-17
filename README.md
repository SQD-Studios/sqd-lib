# SQDLib

[![Build](https://img.shields.io/github/actions/workflow/status/SQD-Studios/sqd-lib/gradle.yml?style=flat-square)](https://github.com/SQD-Studios/sqd-lib/actions)

A library so I don't have to copy and paste the same things again and again

## It's lightweight

What did you think, it's like only 10 classes? The finished build output is as small as 21,1 Kilobytes so it's not
really going to affect your jar output

## Using as an administrator

If you have a plugin which _**clearly states you need**_ to have SQDLib installed for it to work, where can you download
it? Well you can get it from either _Modrinth (Not yet approved)_
or [GitHub Releases](https://github.com/SQD-Studios/sqd-lib/releases). Warning though because SQDLib changes a lot, very
fast
that can do it that 1 version works while the other just breaks all the plugins which may use an older or newer version
of SQDLib.

## Developing with SQDLib

<details>
<summary>Bundling (Recommended)</summary>

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
    // Replace 3.2.0-BETA with the latest version, and you can replace "paper", with the following platforms:
    // velocity, bungee, spigot
    implementation("com.sqdstudios.sqdlib:sqdlib-paper:3.2.0-BETA")
}

tasks {
    // The shadowJar configuration
    shadowJar {
        configurations = project.configurations.runtimeClasspath.map { setOf(it) }

        relocate("com.sqdstudios.sqdlib", "(your plugin group).libs.sqdlib")
    }
}
```

### Adding the dependency (Mod)

```kotlin
dependencies {
    // Replace 3.2.0-BETA with the latest version
    implementation("com.sqdstudios.sqdlib:sqdlib-fabric:3.2.0-BETA")
}
```

</details>


<details>
<summary>Mod/Plugin Dependency (Not Recommended)</summary>

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

When using a `paper-plugin.yml`, make sure you have `join-classpath: true` when using this, or else you'll get class not
found exceptions

```kotlin
dependencies {
    // Replace 3.2.0-BETA with the latest version, and you can replace "paper", with the following platforms:
    // velocity, bungee, spigot
    compileOnly("com.sqdstudios.sqdlib:sqdlib-paper:3.2.0-BETA")
}
```

### Adding the dependency (Mod)

```kotlin
dependencies {
    // Replace 3.2.0-BETA with the latest version
    compileOnly("com.sqdstudios.sqdlib:sqdlib-fabric:3.2.0-BETA")
}
```

</details>

## Migrating from v3 to v4

Version 4 is probably the biggest major release and it contains some breaking changes.

You'll first need to replace every instance of `net.chamosmp` with `com.sqdstudios` (And in the dependency too)

If you're migrating from even older versions like v2, you should delete the packages that are not found and make your
IDE suggest them to you because they're undocumented