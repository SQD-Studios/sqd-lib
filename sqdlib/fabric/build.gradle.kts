plugins {
    id("shared")
    alias(libs.plugins.loom)
}

dependencies {
    api(project(":sqdlib-core"))

    api(libs.bukkitconfig)
    api(libs.adventure.mod)

    minecraft(libs.minecraft)
    compileOnlyApi(libs.fabric.loader)

    implementation(libs.snakeyaml)
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "mcversion" to libs.versions.minecraft,
        "loader" to libs.versions.fabric.loader,
        "java" to java.targetCompatibility
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}