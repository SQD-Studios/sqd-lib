plugins {
    id("shared")
}


dependencies {
    api(project(":sqdlib-core"))

    compileOnly("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
}