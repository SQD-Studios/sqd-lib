plugins {
    id("shared")
}

dependencies {
    api(project(":sqdlib-core"))

    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
}