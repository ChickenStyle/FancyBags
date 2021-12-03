import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.1"
    id("xyz.jpenilla.run-paper") version "1.0.5" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
}

group = "me.chickenstyle.backpack"
version = "2.0"
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
dependencies {
    paperDevBundle("1.18-R0.1-SNAPSHOT")
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
    authors = listOf("Author")
    version = "2.0"
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
