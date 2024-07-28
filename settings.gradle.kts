plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "tsingtao"

register(rootDir, "")

fun register(rootDir: File, path: String) {
    rootDir.listFiles()
        ?.filter { it.isDirectory }
        ?.filter { it.name != "gradle" }
        ?.forEach { file ->
            val name = if (path.isBlank()) file.name else "$path:${file.name}"
            register(file, name)
            if (File(file, "build.gradle.kts").exists()) {
                println("> Include :$name")
                include(name)
            }
        }
}
