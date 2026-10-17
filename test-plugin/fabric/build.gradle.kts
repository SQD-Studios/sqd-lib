plugins {
    id("test")
    alias(libs.plugins.loom)
}

dependencies {
    implementation(project(":sqdlib-fabric"))

    minecraft(libs.minecraft)
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