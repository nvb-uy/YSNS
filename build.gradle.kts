import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import gg.essential.gradle.util.noServerRunConfigs

plugins {
    alias(libs.plugins.kotlin)
    id(egt.plugins.multiversion.get().pluginId)
    id(egt.plugins.defaults.get().pluginId)
    alias(libs.plugins.shadow)
    alias(libs.plugins.blossom)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.cursegradle)
}

val archiveBase: String by project
val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

val necronomicon_version: String by project
val fabric_api_version: String by project
val playeranimator_version_12001: String by project
val playeranimator_version_11902: String by project

val spellengine_version: String by project

preprocess {
    vars.put("MODERN", if (project.platform.mcMinor >= 16) 1 else 0)
}

blossom {
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
    replaceToken("@VER@", mod_version)
}

version = mod_version
group = "elocindev.ysns"

base {
    archivesName.set("$archiveBase-${platform.loaderStr}-${getMcVersionStr()}")
}

loom.noServerRunConfigs()

loom {
    if (project.platform.isLegacyForge) runConfigs {
        "client" { programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker") }
    }
    if (project.platform.isForge) forge {
        mixinConfig("${mod_id}.mixins.json")
    }

    if (project.platform.isFabric) {

    }

    mixin.defaultRefmapName.set("${mod_id}.refmap.json")
}

repositories {
    maven("https://maven.terraformersmc.com/releases/")
    maven("https://repo.essential.gg/repository/maven-public/")
    maven("https://maven.dediamondpro.dev/releases")
    maven("https://maven.isxander.dev/releases")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    
    maven("https://api.modrinth.com/maven")

    maven("https://maven.fabricmc.net")
    maven("https://maven.minecraftforge.net")

    mavenCentral()
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    if (project.platform.isFabric) {
        modImplementation("maven.modrinth:necronomicon:${necronomicon_version}-fabric")
        
          
        if (project.platform.mcMinor == 20) {
            modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.2+1.20.1") 
        } else if (project.platform.mcMinor == 19) {
            modImplementation("net.fabricmc.fabric-api:fabric-api:0.77.0+1.19.2") 
        }
    } else if (project.platform.isForge) {
        implementation("maven.modrinth:necronomicon:${necronomicon_version}-forge")
    }

    implementation("org.joml:joml:1.10.5")
}

tasks.processResources {
    inputs.property("id", mod_id)
    inputs.property("name", mod_name)
    val java = if (project.platform.mcMinor >= 18) {
        17
    } else {
        if (project.platform.mcMinor == 17) 16 else 8
    }
    val compatLevel = "JAVA_${java}"
    inputs.property("java", java)
    inputs.property("java_level", compatLevel)
    inputs.property("version", mod_version)
    inputs.property("mcVersionStr", project.platform.mcVersionStr)
    filesMatching(listOf("mcmod.info", "mods.toml", "fabric.mod.json")) {
        expand(
            mapOf(
                "id" to mod_id,
                "name" to mod_name,
                "java" to java,
                "java_level" to compatLevel,
                "version" to mod_version,
                "mcVersionStr" to getInternalMcVersionStr()
            )
        )
    }
}

tasks {
    withType<Jar> {
        if (project.platform.isFabric) {
            exclude("mcmod.info", "mods.toml", "pack.mcmeta")
        } else {
            exclude("fabric.mod.json")
            if (project.platform.isLegacyForge) {
                exclude("mods.toml")
            } else {
                exclude("mcmod.info")
            }
        }
        from(rootProject.file("LICENSE"))
        from(rootProject.file("LICENSE.LESSER"))
    }

    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("dev")
        configurations = listOf(shade)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
        finalizedBy("copyJar")
    }

    jar {
        if (project.platform.isLegacyForge) {
            manifest {
                attributes(
                    mapOf(
                        "ModSide" to "CLIENT",
                        // "TweakOrder" to "0",
                        // "TweakClass" to "org.spongepowered.asm.launch.MixinTweaker",
                        "ForceLoadAsMod" to true
                    )
                )
            }
        }
        dependsOn(shadowJar)
        archiveClassifier.set("")
        enabled = false
    }

    register<Copy>("copyJar") {
        File("${project.rootDir}/jars").mkdir()
        from(remapJar.get().archiveFile)
        into("${project.rootDir}/jars")
    }

    clean { delete("${project.rootDir}/jars") }

    project.modrinth {
        token.set(System.getenv("MR_TOKEN"))
        projectId.set("jCikwMS7")
        versionNumber.set(mod_version)
        versionName.set("You Shall Not Spawn $mod_version")
        uploadFile.set(remapJar.get().archiveFile as Any)
        gameVersions.addAll(getMcVersionList())
        if (platform.isFabric) {
            loaders.add("fabric")
            loaders.add("quilt")
        } else if (platform.isForge) {
            loaders.add("forge")
            if (platform.mcMinor >= 20) loaders.add("neoforge")
        }
        changelog.set(file("../../changelog.md").readText())
        dependencies {
            required.project("necronomicon")
        }
    }

    project.curseforge {
        project(closureOf<CurseProject> {
            apiKey = System.getenv("CF_TOKEN")
            id = "1028436"
            changelog = file("../../changelog.md")
            changelogType = "markdown"
            relations(closureOf<CurseRelation> {
                requiredDependency("necronomicon")
            })
            gameVersionStrings.addAll(getMcVersionList())
            if (platform.isFabric) {
                addGameVersion("Fabric")
                addGameVersion("Quilt")
            } else if (platform.isForge) {
                addGameVersion("Forge")
                if (platform.mcMinor >= 20) addGameVersion("NeoForge")
            }
            if (platform.isFabric) {
                releaseType = "release"
            } else {
                releaseType = "beta"
            }
            mainArtifact(remapJar.get().archiveFile, closureOf<CurseArtifact> {
                displayName = "[${getMcVersionStr()}-${platform.loaderStr}] You Shall Not Spawn v$mod_version"
            })
        })
        options(closureOf<Options> {
            javaVersionAutoDetect = false
            javaIntegration = false
            forgeGradleIntegration = false
        })
    }

    register("publish") {
        dependsOn(modrinth)
        dependsOn(curseforge)
    }
}

fun getMcVersionStr(): String {
    return when (project.platform.mcVersionStr) {
        else -> {
            val dots = project.platform.mcVersionStr.count { it == '.' }
            if (dots == 1) "${project.platform.mcVersionStr}.x"
            else "${project.platform.mcVersionStr.substringBeforeLast(".")}.x"
        }
    }
}

fun getInternalMcVersionStr(): String {
    return when (project.platform.mcVersionStr) {
        else -> {
            val dots = project.platform.mcVersionStr.count { it == '.' }
            if (dots == 1) "${project.platform.mcVersionStr}.x"
            else "${project.platform.mcVersionStr.substringBeforeLast(".")}.x"
        }
    }
}

fun getMcVersionList(): List<String> {
    return when (project.platform.mcVersionStr) {
        "1.20.1" -> listOf("1.20", "1.20.1")
        "1.19.2" -> listOf("1.19.2")
        else -> error("Unknown version")
    }
}