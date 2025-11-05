plugins {
    id("java")
    id("application")
    id("org.graalvm.buildtools.native") version "0.9.22"
}

group = "edu.io"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("edu.io.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

// GraalVM Native Image configuration: creates a native executable `itx`
graalvmNative {
    binaries {
        named("main") {
            imageName.set("itx")
            mainClass.set("edu.io.Main")
            // Avoid fallback to a JVM image; fail if native image cannot be produced
            buildArgs.addAll(listOf("--no-fallback"))
            // Uncomment to enable verbose native-image output during build
            // buildArgs.add("--verbose")
        }
    }
}

// Provide a convenience task alias `nativeImage` if plugin registers a differently-named task
tasks.register("nativeImage") {
    group = "build"
    description = "Build native image using GraalVM native-image (alias to graalvm native task)"
    dependsOn("nativeCompile")
}
