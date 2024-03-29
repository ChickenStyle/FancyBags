package me.chickenstyle.backpack;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import com.avaje.ebeaninternal.server.lib.util.MailMessage;
import me.chickenstyle.backpack.versions.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import me.chickenstyle.backpack.configs.CustomBackpacks;
import me.chickenstyle.backpack.customholders.BackpackHolder;
import me.chickenstyle.backpack.events.ClickInventoryEvent;
import me.chickenstyle.backpack.events.CloseInventoryEvent;
import me.chickenstyle.backpack.events.CraftEvent;
import me.chickenstyle.backpack.events.DeathPlayerEvent;
import me.chickenstyle.backpack.events.PickupItemEvent;
import me.chickenstyle.backpack.events.PipeEvents;
import me.chickenstyle.backpack.events.RightClickEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class FancyBags extends JavaPlugin implements Listener{
	
	
	public static HashMap<UUID,Backpack> creatingBackpack = new HashMap<UUID,Backpack>();
	public static ArrayList<NamespacedKey> recipes;
	private static NMSHandler versionHandler;
	private static FancyBags instance;
	
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		if (creatingBackpack.containsKey(e.getPlayer().getUniqueId())) creatingBackpack.remove(e.getPlayer().getUniqueId());
	}
	
	
	@Override
	public void onEnable() {
		instance = this;
		
	    //Detects server's version
		if (!getServerVersion()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		//Loads the configs
		this.getConfig().options().copyDefaults();
	    saveDefaultConfig();
	    new CustomBackpacks(this);


		//Loads proper data :)
		if (!Bukkit.getVersion().contains("1.8") &&
				!Bukkit.getVersion().contains("1.9") &&
				!Bukkit.getVersion().contains("1.10") &&
				!Bukkit.getVersion().contains("1.11")) {
		    recipes = new ArrayList<NamespacedKey>();
		}
		
		loadListeners();
		loadRecipes();
		
		if (!Bukkit.getVersion().contains("1.8") &&
			!Bukkit.getVersion().contains("1.9") &&
			!Bukkit.getVersion().contains("1.10")) {
	        new UpdateChecker(this, 79997).getVersion(version -> {
	            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
	                getServer().getConsoleSender().sendMessage(Utils.color("&aFancyBags >> You are using the latest version of the plugin!"));
	            } else {
	            	getServer().getConsoleSender().sendMessage(Utils.color("&cFancyBags >> You are using an outdated version of the plugin!\nYour version is: &b" +  getDescription().getVersion() + "\n&cThe latest version is: &a" + version));
	            }
	        });
		}
		
		
		//Getting data
        int pluginId = 8024;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
		
		//Loads command
		getCommand("fancybags").setExecutor(new BackpackCommand());
		getCommand("fancybags").setTabCompleter(new FancyTab());
		getServer().getConsoleSender().sendMessage("FancyBags plugin has been enabled!");
	}

	@Override
	public void onDisable() {
		for (Player player:getServer().getOnlinePlayers()) {
			if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				player.closeInventory();
			}
		}

		if (!Bukkit.getVersion().contains("1.8") &&
				!Bukkit.getVersion().contains("1.9") &&
				!Bukkit.getVersion().contains("1.10") &&
				!Bukkit.getVersion().contains("1.11")) {
			if (CustomBackpacks.getBackpacks() != null) {
				ArrayList<Backpack> packs = CustomBackpacks.getBackpacks();
				if (!packs.isEmpty() && packs != null) {
					for (Backpack pack:packs) {
						removeRecipe(pack.getRecipe());
					}
				}	
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		if (!Bukkit.getVersion().contains("1.8") &&
			!Bukkit.getVersion().contains("1.9") &&
			!Bukkit.getVersion().contains("1.10")) {
			if (e.getPlayer().isOp()) {
				
				TextComponent message = new TextComponent("Click me");
				message.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/%E2%9A%A1-fancybags-%E2%9A%A1-new-way-to-store-items-1-8-1-16-2.79997/" ) );
				message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("&7Click here to download the latest version of the plugin!")).create()));
				message.setColor(net.md_5.bungee.api.ChatColor.GOLD);
				
				
		        new UpdateChecker(this, 79997).getVersion(version -> {
		            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
		            	e.getPlayer().sendMessage(Utils.color("&cFancyBags >> You are using an outdated version of the plugin!\nlatest version is : &a" + version));
		            	e.getPlayer().spigot().sendMessage(message);
		            }
		        });
			}
		}

	}
	
	public void loadListeners() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new RightClickEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CloseInventoryEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ClickInventoryEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CraftEvent(),this);
		Bukkit.getPluginManager().registerEvents(new DeathPlayerEvent(), this);
		Bukkit.getPluginManager().registerEvents(new PickupItemEvent(), this);
		
		if (getServer().getPluginManager().getPlugin("CraftBook")!=null) {
			Bukkit.getPluginManager().registerEvents(new PipeEvents(), this);
		}
	}
	
	public void loadRecipes() {
		ArrayList<Backpack> packs = CustomBackpacks.getBackpacks();



		if (!Bukkit.getVersion().contains("1.8") &&
				!Bukkit.getVersion().contains("1.9") &&
				!Bukkit.getVersion().contains("1.10") &&
				!Bukkit.getVersion().contains("1.11")) {
			if (!packs.isEmpty() && packs != null) {
				for (Backpack pack: packs) {
					if (pack.getRecipe() != null) {
						removeRecipe(pack.getRecipe());
					}
				}

			}
		}
		
		
		if (!packs.isEmpty() && packs != null) {
			int recipesAmount = 0;
			for (Backpack pack:packs) {
				if (pack.getRecipe() != null) {
					getServer().addRecipe(pack.getRecipe());
					recipesAmount++;
				}



				if (!Bukkit.getVersion().contains("1.8") &&
					!Bukkit.getVersion().contains("1.9") &&
					!Bukkit.getVersion().contains("1.10") &&
					!Bukkit.getVersion().contains("1.11")) {
					if (pack.getRecipe() != null) {
						recipes.add(pack.getRecipe().getKey());
					}
				}


			}
			
			if (recipesAmount != 0) {
				getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "FancyBags >>> loaded " + recipesAmount + " recipes!");
			} else {
				getServer().getConsoleSender().sendMessage(ChatColor.RED + "FancyBags >>> No recipes detected!");
			}
			
			
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "FancyBags >>> No recipes detected!");
		}
	}
	
	public void removeRecipe(ShapedRecipe inputRecipe){
        Iterator<Recipe> it = getServer().recipeIterator();
       
        while(it.hasNext()){
            Recipe itRecipe = it.next();
            if(itRecipe instanceof ShapedRecipe){
                ShapedRecipe itShaped = (ShapedRecipe) itRecipe;
                if (recipes.contains(itShaped.getKey())) {
                	it.remove();
                }
            }
        }
    }
	
    public String charRemoveAt(String str, int p) {  
        return str.substring(0, p) + str.substring(p + 1);  
    }
	
	public boolean getServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName();
		version = version.substring(version.lastIndexOf(".") + 1);
		boolean isValid = true;
		switch (version) {
		case "v1_8_R1":
			versionHandler = new Handler_1_8_R1();
		break;
		
		case "v1_8_R2":
			versionHandler = new Handler_1_8_R2();
		break;
		
		case "v1_8_R3":
			versionHandler = new Handler_1_8_R3();
		break;
		
		case "v1_9_R1":
			versionHandler = new Handler_1_9_R1();
		break;
		
		case "v1_9_R2":
			versionHandler = new Handler_1_9_R2();
		break;
		
		case "v1_10_R1":
			versionHandler = new Handler_1_10_R1();
		break;
		
		case "v1_11_R1":
			versionHandler = new Handler_1_11_R1();
		break;
		
		case "v1_12_R1":
			versionHandler = new Handler_1_12_R1();
		break;
		
		case "v1_13_R1":
			versionHandler = new Handler_1_13_R1();
		break;
		
		case "v1_13_R2":
			versionHandler = new Handler_1_13_R2();
		break;
		
		case "v1_14_R1":
			versionHandler = new Handler_1_14_R1();
		break;
		
		case "v1_15_R1":
			versionHandler = new Handler_1_15_R1();
		break;
		
		case "v1_16_R1":
			versionHandler = new Handler_1_16_R1();
		break;
		
		case "v1_16_R2":
			versionHandler = new Handler_1_16_R2();
		break;
		
		case "v1_16_R3":
			versionHandler = new Handler_1_16_R3();
		break;

		case "v1_17_R1":
			versionHandler = new Handler_1_17_R1();
		break;
		default:
			isValid = false;
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "FancyBags >>> This version isn't supported!");
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "FancyBags >>> Plugin will be disabled!");
		}
		if (isValid) {
			getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "FancyBags >>> NMS Version Detected: " + version);
		}
		return isValid;
		
	}

	public static FancyBags getInstance() {
		return instance;
	}
	
	public static NMSHandler getVersionHandler() {
		return versionHandler;
	}
}
