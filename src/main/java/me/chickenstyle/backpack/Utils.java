package me.chickenstyle.backpack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.chickenstyle.backpack.customholders.BackpackHolder;

public class Utils {

	
	public static String itemstackToBase64(ItemStack item) throws UTFDataFormatException {
		String itemString = null;
		try {
			ByteArrayOutputStream io = new ByteArrayOutputStream();
			BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
			os.writeObject(item);
			os.flush();
			
			if (io.toByteArray().length > 65535) {
				throw new UTFDataFormatException();
			}
			
			itemString = Base64.getEncoder().encodeToString(io.toByteArray());
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return itemString;
	}
	
	public static ItemStack itemstackFromBase64(String base64) {
		ItemStack item = null;
		
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
			BukkitObjectInputStream is = new BukkitObjectInputStream(in);
			item = (ItemStack) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
		
			e.printStackTrace();
		}
		return item;
	}
	
	/*public static Component color(String text) {
		return FancyBags.getInstance().parse(text);
	}*/
	
	public static ItemStack getVersionSkull() {
		return new ItemStack(Material.valueOf("PLAYER_HEAD"));
    }
	
    
	public static ItemStack getRedVersionGlass() {
    	final ItemStack glass = new ItemStack(Material.valueOf("RED_STAINED_GLASS_PANE"));

        
        ItemMeta meta = glass.getItemMeta();
		meta.displayName(FancyBags.getInstance().parse(FancyBags.getInstance().getConfig().getString("slotsLimit")).decoration(TextDecoration.ITALIC, false));

        glass.setItemMeta(meta);
        return glass;
    }
    
	public static ItemStack getGreenVersionGlass() {
    	final ItemStack glass = new ItemStack(Material.valueOf("GREEN_STAINED_GLASS_PANE"));
        
        ItemMeta meta = glass.getItemMeta();
		meta.displayName(Component.text("Click here to save the recipe!", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        glass.setItemMeta(meta);
        return glass;
    }
    
    
	public static ItemStack getGrayVersionGlass() {
    	final ItemStack glass = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));

        
        ItemMeta meta = glass.getItemMeta();
		meta.displayName(Component.text(" ").decoration(TextDecoration.ITALIC, false));
        glass.setItemMeta(meta);
        return glass;
    }
    
    public static Sound getVersionChestOpenSound() {
		return Sound.valueOf("BLOCK_CHEST_OPEN");
    }
    
    public static Sound getVersionChestCloseSound() {
        return Sound.valueOf("BLOCK_CHEST_CLOSE");
    }


    
    public static ItemStack createCustomSkull(Component displayName, String texture) {
    	try {
            ItemStack skull = getVersionSkull();
            if (texture.isEmpty()) {
                return skull;
            }
            texture = "https://textures.minecraft.net/texture/" + texture;
            SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
            skullMeta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            }
            catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            assert profileField != null;
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            skull.setItemMeta(skullMeta);
            return skull;
    	} catch (AuthorNagException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    
	public static ItemStack createBackpackItemStack(String name,String texture,int slotsAmount,int id) {
    	ItemStack item;
		item = FancyBags.getNMSHandler().addIntTag(createCustomSkull(FancyBags.getInstance().parse(name), texture), "BackpackID", id);
    	item = FancyBags.getNMSHandler().addStringTag(item, "BackpackTitle", name);
		item = FancyBags.getNMSHandler().addIntTag(item, "SlotsAmount", slotsAmount);
    	
		for (int i = 0; i < slotsAmount; i++) {
			
			
			try {
				item = FancyBags.getNMSHandler().addStringTag(item, i + "", Utils.itemstackToBase64(new ItemStack(Material.AIR)));
			} catch (UTFDataFormatException e) {
				
				e.printStackTrace();
			}
		}
		
		ArrayList<Component> lore = new ArrayList<>();
		
		
		for (String line: FancyBags.getInstance().getConfig().getStringList("backpackLore")) {
			lore.add(FancyBags.getInstance().parse(line.replace("{slotsAmount}", slotsAmount + "")).decoration(TextDecoration.ITALIC, false));
		}
		
		lore.add(FancyBags.getInstance().parse(" ").decoration(TextDecoration.ITALIC, false));
		lore.add(FancyBags.getInstance().parse(FancyBags.getInstance().getConfig().getString("emptyBackpack")).decoration(TextDecoration.ITALIC, false));
		lore.add(FancyBags.getInstance().parse(" ").decoration(TextDecoration.ITALIC, false));
    	
    	ItemMeta meta = item.getItemMeta();
    	meta.lore(lore);
    	item.setItemMeta(meta);
    	
    	return item;
    	
    }
    
    private static void addItem(Player player,ItemStack item) {
    	if (player.getInventory().firstEmpty() != -1) {
    		player.getInventory().addItem(item);
    	} else {
    		player.getWorld().dropItem(player.getLocation(), item);
    	}
    }
    
    
    public static ItemStack clearOldTags(ItemStack item) {

		if(FancyBags.getNMSHandler().hasTag(item, "Size")){
			item = FancyBags.getNMSHandler().addIntTag(item, "SlotsAmount", FancyBags.getNMSHandler().getIntData(item,"Size"));
		}
		if(FancyBags.getNMSHandler().hasTag(item, "Title")){
			item = FancyBags.getNMSHandler().addStringTag(item, "BackpackTitle", FancyBags.getNMSHandler().getStringData(item,"Title"));
		}

    	item = FancyBags.getNMSHandler().removeTag(item, "BackPack");
    	item = FancyBags.getNMSHandler().removeTag(item, "Size");
    	item = FancyBags.getNMSHandler().removeTag(item, "Title");
    	return item;
    }
    
	public static ItemStack loadBackpack(Player player,int slotsAmount) {
		ItemStack bag = player.getInventory().getItemInMainHand();
		
		boolean sendMessage = false;
		for (int i = 0; i < slotsAmount;i++) {
			try {

				bag = player.getOpenInventory().getTopInventory().getItem(i) == null ?
						FancyBags.getNMSHandler().addStringTag(bag,i + "",itemstackToBase64(new ItemStack(Material.AIR))) :
						FancyBags.getNMSHandler().addStringTag(bag, i + "", Utils.itemstackToBase64(player.getOpenInventory().getTopInventory().getItem(i)));

			} catch (UTFDataFormatException e) {
				addItem(player, player.getOpenInventory().getTopInventory().getItem(i));
			}
		}
		
		if (sendMessage) {
			player.sendMessage(FancyBags.getInstance().parse(Message.DISABLE_PLACE.getMSG()));
		}
		
		ArrayList<Component> lore = Utils.loadLoreBackpack(player,slotsAmount);
		if(bag.hasItemMeta()){
			ItemMeta meta = bag.getItemMeta();
			meta.lore(lore);
			bag.setItemMeta(meta);
		}

		return bag;
    }
    
	public static ArrayList<Component> loadLoreBackpack(Player player,int slots) {
		ArrayList<Component> lore = new ArrayList<>();
		
		
		for (String line: FancyBags.getInstance().getConfig().getStringList("backpackLore")) {
			lore.add(FancyBags.getInstance().parse(line.replace("{slotsAmount}", slots + "")).decoration(TextDecoration.ITALIC, false));
		}

		if (FancyBags.getInstance().getConfig().getBoolean("showContents")) {
			lore.add(FancyBags.getInstance().parse(" ").decoration(TextDecoration.ITALIC, false));
			if (!isEmpty(player.getOpenInventory().getTopInventory())) {

				//Count and sort all the items in the backpack
				ArrayList<ItemStack> items = new ArrayList<>();
				for (ItemStack item:player.getOpenInventory().getTopInventory().getContents()) {
					if (item != null && item.getType() != Material.AIR && !item.equals(Utils.getRedVersionGlass())) {

						if (!items.isEmpty()) {
							boolean similar = false;
							for (ItemStack checkedItem:items) {
								if (item.isSimilar(checkedItem)) {
									checkedItem.setAmount(checkedItem.getAmount() + item.getAmount());
									similar = true;
								}
							}

							if (!similar) {
								items.add(item);
							}

						} else {
							items.add(item);
						}
					}
				}

				if (items.size() != 0) {
					if (!(items.size() > 5)) {
						for (ItemStack item:items) {
							String structure = FancyBags.getInstance().getConfig().getString("displayItemInLore");
							structure = structure.replace("{number}", item.getAmount() + "");

							if (item.getItemMeta().hasDisplayName()) {
								structure = structure.replace("{item_name}", FancyBags.getInstance().getMiniMessage().serialize(item.getItemMeta().displayName()));
							} else {
								structure = structure.replace("{item_name}", "<white>" + getName(item.getType()));
							}

							lore.add(FancyBags.getInstance().parse(structure).decoration(TextDecoration.ITALIC, false));
						}
					} else {
						for (int i = 0; i < 5;i++) {
							ItemStack item = items.get(i);
							String structure = FancyBags.getInstance().getConfig().getString("displayItemInLore");
							if(structure != null){
								structure = structure.replace("{number}", item.getAmount() + "");

								if (item.getItemMeta().hasDisplayName()) {
									structure = structure.replace("{item_name}", FancyBags.getInstance().getMiniMessage().serialize(item.getItemMeta().displayName()));
								} else {
									structure = structure.replace("{item_name}", "<white>" + getName(item.getType()));
								}

								lore.add(FancyBags.getInstance().parse(structure).decoration(TextDecoration.ITALIC, false));
							}

						}

						int amount = 0;
						for (int i = 5;i < items.size();i++) {
							amount += items.get(i).getAmount();
						}

						String other = FancyBags.getInstance().getConfig().getString("otherItemsInLore");
						if(other != null){
							other = other.replace("{amount}", amount + "");
							lore.add(FancyBags.getInstance().parse(other).decoration(TextDecoration.ITALIC, false));
						}


					}
				} else {
					lore.add(FancyBags.getInstance().parse(FancyBags.getInstance().getConfig().getString("emptyBackpack")).decoration(TextDecoration.ITALIC, false));
				}




			} else {
				lore.add(FancyBags.getInstance().parse(FancyBags.getInstance().getConfig().getString("emptyBackpack")).decoration(TextDecoration.ITALIC, false));
			}

			lore.add(FancyBags.getInstance().parse(" ").decoration(TextDecoration.ITALIC, false));
		}
		return lore;
    }
    
	private static String getName(Material mat) {
		String name = mat.name().replace('_',' ').toLowerCase();
		String[] data = name.split("");
		
		for (int i = 0;i < data.length;i++) {
			if (i != 0) {
				if (data[i - 1].equals(" ")) {
					data[i] = data[i].toUpperCase();
				}
			} else {
				data[i] = data[i].toUpperCase();
			}
		}
		
		name = arrayToString(data);
		return name;
	}
	
    public static Inventory inventoryFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(new BackpackHolder(), dataInput.readInt());
            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            dataInput.close();
            return inventory;
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException("Unable to decode the class type.", e);
        }
        catch(IOException e) {
            throw new RuntimeException("Unable to convert Inventory to Base64.", e);
        }
    }
	
	private static boolean isEmpty(Inventory inv) {
		for(ItemStack it : inv.getContents()) {
		    if(it != null) return false;
		}
		return true;
	}
	
	private static String arrayToString(String[] arr) {
		String str = "";
		for (String chr:arr) {
			str = str + chr;
		}
		return str;
	}
}
