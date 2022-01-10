import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id ("com.github.johnrengelman.shadow") version "7.1.1"
    id("io.papermc.paperweight.userdev") version "1.3.3"
    id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "me.chickenstyle.backpack"
version = "2.3.1"
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/") {
        content {
            includeGroup("io.papermc.paper")
            includeGroup("net.kyori")
        }
    }
}
dependencies {
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")

    //Shaded
    implementation("net.kyori:adventure-text-minimessage:4.10.0-SNAPSHOT") {
        exclude(group = "net.kyori", module = "adventure-api")
        exclude(group = "net.kyori", module = "adventure-bom")
    }
}




val shadowPath = "me.chickenstyle.backpack.shadow"
tasks.withType<ShadowJar> {

    minimize()
    //MiniMessage
    relocate("net.kyori.adventure.text.minimessage", "$shadowPath.kyori.minimessage")

    dependencies {
        include(dependency("net.kyori:adventure-text-minimessage:"))
    }

    archiveClassifier.set("")
}

tasks {
    // Run reobfJar on build
    build {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = "me.chickenstyle.backpack.FancyBags"
    apiVersion = "1.18"
    authors = listOf("ChickenStyle")
    version = "2.3.1"
    description = "implements cool backpacks to the game"
    commands {
        register("fancybags") {
            description = "Plugin's main command"
            aliases = listOf("fb")
            permission = "FancyBags.help"
            usage = "Just run the command!"
        }
        // ...
    }
}
