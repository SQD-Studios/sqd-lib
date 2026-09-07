rootProject.name = "sqd-lib"

include("sqdlib-core")
include("sqdlib-paper")
include("sqdlib-velocity")

fun importProjectsIn(folder: File) {
    val name = folder.name
    rootDir.resolve(name).listFiles { it.isDirectory }?.forEach {
        include(":${name}-${it.name}")
        project(":${name}-${it.name}").projectDir = it
    }
}

importProjectsIn(rootDir.resolve("test-plugin"))