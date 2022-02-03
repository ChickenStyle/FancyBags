package me.chickenstyle.backpack.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.chickenstyle.backpack.Backpack;
import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.RejectItems;
import me.chickenstyle.backpack.RejectType;
import me.chickenstyle.backpack.Utils;

public class CustomBackpacks {
	
	
	/*
	 *  ID{int}:
	 *     title: "Looks NICE"
	 *     slotsAmount: 34
	 *     texture: "long af texture"
	 *	   craftRecipe:
	 *     - "XIX"
	 *     - "XDX"
	 *     - "XAX"
	 *	   ingredients: 
	 *	   - "I:diamond"
	 *     - "D:stick"
	 *     - "A:iron_ingot"
	 *
	 * 
	 * 
	 * 
	 */
	
	private static File file;
	private static YamlConfiguration config;
	public CustomBackpacks(FancyBags main) {
  	  file = new File(main.getDataFolder(), "Backpacks.yml");
  	 if (!file.exists()) {
  		 try {
				 file.createNewFile();
		    	 config = YamlConfiguration.loadConfiguration(file);
		    	  	try {
		    				config.save(file);
		    		    	config = YamlConfiguration.loadConfiguration(file);
		    			} catch (IOException e) {
		    				e.printStackTrace();
		    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
  		 
  	 }
  	config = YamlConfiguration.loadConfiguration(file);
   }
    
	static public ArrayList<Backpack> getBackpacks(){
    	ArrayList<Backpack> list = new ArrayList<Backpack>();
        	if (config.getConfigurationSection("Backpacks") == null) return new ArrayList<Backpack>();
        	for (String id:config.getConfigurationSection("Backpacks").getKeys(false)) {
        		
        		list.add(getBackpack(Integer.valueOf(id)));
        	}

        return list;

    	
    }
    
	static public void addBackpack(Backpack pack,HashMap<Character,ItemStack> map) {
    		config = YamlConfiguration.loadConfiguration(file);
    		String path = "Backpacks." + pack.getId();
    		
    		config.set(path + ".title", pack.getName());
    		config.set(path + ".slotsAmount", pack.getSlotsAmount());
    		config.set(path + ".texture", pack.getTexture());
    		

    		try {
        		config.set(path + ".craftRecipe", pack.getRecipe().getShape());
        		ArrayList<String> ingredients = new ArrayList<String>();
        		
        		for (char symbol:map.keySet()) {
        			if (map.get(symbol) != null && map.get(symbol).getType() != Material.AIR) {

						ingredients.add(symbol + ":" + map.get(symbol).getType());

        			}
        		}
        		config.set(path + ".ingredients", ingredients);
    		} catch (Exception e) {
    			config.set(path + ".craftRecipe", "none");
    			config.set(path + ".ingredients", "none");
			}

    		config.set(path + ".rejectItems.rejects", pack.getReject().isRejecting());
    		config.set(path + ".rejectItems.rejectType", pack.getReject().getType().toString());
    		config.set(path + ".rejectItems.items", pack.getReject().getItems());
    		
    		
    	  	try {
    			config.save(file);
    	    	config = YamlConfiguration.loadConfiguration(file);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    }
    
	static public Backpack getBackpack(int id) {
		String path = "Backpacks." + id;
		String name = config.getString(path + ".title");
		int slotsAmount = config.getInt(path + ".slotsAmount");
		String texture = config.getString(path + ".texture");
		
		ItemStack item = Utils.createBackpackItemStack(name, texture,slotsAmount,id);
		
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(FancyBags.getInstance(),path),item);



		
		if (!config.get(path + ".craftRecipe").equals("none")) {
			ArrayList<String> str = (ArrayList<String>) config.get(path + ".craftRecipe");
			String[] someList = {};
			recipe.shape(str.toArray(someList));
			
			
			for (String data: (ArrayList<String>)config.getList(path + ".ingredients")) {
				char symbol = data.split(":")[0].charAt(0);


				ItemStack mat = new ItemStack(Material.valueOf(data.split(":")[1].split(",")[0]));
				recipe.setIngredient(symbol, mat.getType());
			}
		} else {
			recipe = null;
		}

		

		
		boolean reject = config.getBoolean(path + ".rejectItems.rejects");
		RejectType type = RejectType.valueOf(config.getString(path + ".rejectItems.rejectType"));
		ArrayList<ItemStack> items;
		if (config.get(path + ".rejectItems.items") != null) {
			items = (ArrayList<ItemStack>) config.get(path + ".rejectItems.items");
		} else {
			items = new ArrayList<ItemStack>();
		}
		
		return new Backpack(id, name, texture, slotsAmount, new RejectItems(reject, type, items), recipe);
    }
    	
    static public boolean hasBackpack(int id) {
		return config.get("Backpacks." + id) != null;
	}
	
	static public void configReload() {
   	 config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
			config = YamlConfiguration.loadConfiguration(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}