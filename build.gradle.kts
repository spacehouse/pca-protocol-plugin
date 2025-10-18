plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.0.0-beta12"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17" apply false
}

allprojects {
    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()

        maven(url = "https://maven.enginehub.org/repo/")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
        maven(url = "https://repo.dmulloy2.net/repository/public/")
        maven(url = "https://repo.codemc.io/repository/maven-public/")
        maven(url = "https://repo.codemc.io/repository/nms/")
        maven(url = "https://jitpack.io")
        maven(url = "https://hub.spigotmc.org/nexus/content/groups/public/")
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.javadoc {
        options.encoding = "UTF-8"
    }
}

dependencies {
    api(project(":core"))
    api(project(":v1_17_1", configuration = "reobf"))
    api(project(":v1_18", configuration = "reobf"))
    api(project(":v1_19", configuration = "reobf"))
    api(project(":v1_19_1", configuration = "reobf"))
    api(project(":v1_19_3", configuration = "reobf"))
    api(project(":v1_19_4", configuration = "reobf"))
    api(project(":v1_20", configuration = "reobf"))
    api(project(":v1_20_2", configuration = "reobf"))
    api(project(":v1_20_3", configuration = "reobf"))
    api(project(":v1_20_6", configuration = "reobf"))
    api(project(":v1_21", configuration = "reobf"))
    api(project(":v1_21_3", configuration = "reobf"))
    api(project(":v1_21_4", configuration = "reobf"))
    api(project(":v1_21_5", configuration = "reobf"))
    api(project(":v1_21_6", configuration = "reobf"))
    api(project(":v1_21_7", configuration = "reobf"))
}

tasks {
//    shadowJar {
//        archiveClassifier = ""
//        minimize()
//        manifest {
//            attributes["paperweight-mappings-namespace"] = "spigot"
//        }
//    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release = 16
    }

    processResources {
        from("resources")
        expand(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "main" to "${project.group}.PcaProtocol"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            from(project.components["java"])
        }
    }
}