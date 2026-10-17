plugins {
    id("test")
    alias(libs.plugins.run.velocity) apply true
}

dependencies {
    compileOnly(project(":sqdlib-velocity"))

    compileOnly(libs.velocityapi)
    annotationProcessor(libs.velocityapi)
}

tasks.runVelocity {
    velocityVersion(libs.versions.velocity.get())
}