plugins {
    id("shared")
    alias(libs.plugins.loom)
}

dependencies {
    include(api(project(":sqdlib-core"))!!)

    include(api(libs.bukkitconfig.get())!!)
    include(api(libs.adventure.mod.get())!!)

    minecraft(libs.minecraft.get())
    compileOnlyApi(libs.fabric.loader.get())

    include(implementation(libs.snakeyaml.get())!!)
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "mcversion" to libs.versions.minecraft.get(),
        "loader" to libs.versions.fabric.loader.get(),
        "java" to java.targetCompatibility
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}