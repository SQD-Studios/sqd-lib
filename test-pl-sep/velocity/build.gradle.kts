plugins {
    id("test")
    id("xyz.jpenilla.run-velocity") version "3.1.0" apply true
}

dependencies {
    compileOnly(project(":sqdlib-velocity"))

    compileOnly("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:4.1.1-SNAPSHOT")
}

tasks.runVelocity {
    velocityVersion("4.1.1-SNAPSHOT")
}