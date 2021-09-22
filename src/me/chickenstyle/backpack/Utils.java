package me.chickenstyle.backpack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	
	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
    @SuppressWarnings("deprecation")
	public static ItemStack getVersionSkull() {
    	
    	String ver = Bukkit.getVersion();
    	
    	if (ver.contains("1.8") || ver.contains("1.9") || ver.contains("1.10") || ver.contains("1.11") || ver.contains("1.12")) {
    		return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
    	} else {
    		return new ItemStack(Material.valueOf("PLAYER_HEAD"));
    	}
    	
    }
	
    
    @SuppressWarnings("deprecation")
	public static ItemStack getRedVersionGlass() {
    	ItemStack glass = null;
    	
    	String ver = Bukkit.getVersion();
    	
    	if (ver.contains("1.8") || ver.contains("1.9") || ver.contains("1.10") || ver.contains("1.11") || ver.contains("1.12")) {
    		glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 14);
    	} else {
    		glass = new ItemStack(Material.valueOf("RED_STAINED_GLASS_PANE"));
    	}
    	
        
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(color(FancyBags.getInstance().getConfig().getString("slotsLimit")));
        glass.setItemMeta(meta);
        return glass;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack getGreenVersionGlass() {
    	ItemStack glass = null;
    	
    	String ver = Bukkit.getVersion();
    	if (ver.contains("1.8") || ver.contains("1.9") || ver.contains("1.10") || ver.contains("1.11") || ver.contains("1.12")) {
    		glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 13);
    	} else {
    		glass = new ItemStack(Material.valueOf("GREEN_STAINED_GLASS_PANE"));
    	}
        
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Click here to save the recipe!");
        glass.setItemMeta(meta);
        return glass;
    }
    
    
    @SuppressWarnings("deprecation")
	public static ItemStack getGrayVersionGlass() {
    	ItemStack glass = null;
    	
    	String ver = Bukkit.getVersion();
    	if (ver.contains("1.8") || ver.contains("1.9") || ver.contains("1.10") || ver.contains("1.11") || ver.contains("1.12")) {
    		glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
    	} else {
    		glass = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
    	}

        
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        return glass;
    }
    
    public static Sound getVersionChestOpenSound() {
    	Sound glass = null;
        if (Bukkit.getVersion().contains("1.8")) {
        	glass = Sound.valueOf("CHEST_OPEN");
        } else {
        	glass = Sound.valueOf("BLOCK_CHEST_OPEN");
        }
        return glass;
    }
    
    public static Sound getVersionChestCloseSound() {
    	Sound glass = null;
        if (Bukkit.getVersion().contains("1.8")) {
        	glass = Sound.valueOf("CHEST_CLOSE");
        } else {
        	glass = Sound.valueOf("BLOCK_CHEST_CLOSE");
        }
        return glass;
    }


    
    public static ItemStack createCustomSkull(String displayName, String texture) {
    	try {
            ItemStack skull = getVersionSkull();
            if (texture.isEmpty()) {
                return skull;
            }
            texture = "http://textures.minecraft.net/texture/" + texture;
            SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
            skullMeta.setDisplayName(color(displayName));
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
    
    
    @SuppressWarnings("unchecked")
	public static ItemStack createBackpackItemStack(String name,String texture,int slotsAmount,int id) {
    	ItemStack item = null;
		item = FancyBags.getVersionHandler().addIntTag(createCustomSkull(name, texture), "BackpackID", id);
    	item = FancyBags.getVersionHandler().addStringTag(item, "BackpackTitle", name);
		item = FancyBags.getVersionHandler().addIntTag(item, "SlotsAmount", slotsAmount);
    	
		for (int i = 0; i < slotsAmount; i++) {
			
			
			try {
				item = FancyBags.getVersionHandler().addStringTag(item, i + "", Utils.itemstackToBase64(new ItemStack(Material.AIR)));
			} catch (UTFDataFormatException e) {
				
				e.printStackTrace();
			}
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		
		
		for (String line:(ArrayList<String>) FancyBags.getInstance().getConfig().get("backpackLore")) {
			lore.add(Utils.color(line.replace("{slotsAmount}", slotsAmount + "")));
		}
		
		lore.add(" ");
		lore.add(Utils.color(FancyBags.getInstance().getConfig().getString("emptyBackpack")));
		lore.add(" ");
    	
    	ItemMeta meta = item.getItemMeta();
    	meta.setLore(lore);
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
    	
    	item = FancyBags.getVersionHandler().addIntTag(item, "SlotsAmount", FancyBags.getVersionHandler().getIntData(item,"Size"));
    	item = FancyBags.getVersionHandler().addStringTag(item, "BackpackTitle", FancyBags.getVersionHandler().getStringData(item,"Title"));
    	
    	item = FancyBags.getVersionHandler().removeTag(item, "BackPack");
    	item = FancyBags.getVersionHandler().removeTag(item, "Size");
    	item = FancyBags.getVersionHandler().removeTag(item, "Title");
    	return item;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack loadBackpack(Player player,int slotsAmount) {
		ItemStack bag = player.getItemInHand();
		
		boolean sendMessage = false;
		for (int i = 0; i < slotsAmount;i++) {
			try {

				bag = player.getOpenInventory().getTopInventory().getItem(i) == null ?
						FancyBags.getVersionHandler().addStringTag(bag,i + "",itemstackToBase64(new ItemStack(Material.AIR))) :
						FancyBags.getVersionHandler().addStringTag(bag, i + "", Utils.itemstackToBase64(player.getOpenInventory().getTopInventory().getItem(i)));

			} catch (UTFDataFormatException e) {
				addItem(player, player.getOpenInventory().getTopInventory().getItem(i));
			}
		}
		
		if (sendMessage) {
			player.sendMessage(Message.DISABLE_PLACE.getMSG());
		}
		
		ArrayList<String> lore = Utils.loadLoreBackpack(player,slotsAmount);
		ItemMeta meta = bag.getItemMeta();
		meta.setLore(lore);
		bag.setItemMeta(meta);
		return bag;
    }
    
    @SuppressWarnings({ "unchecked" })
	public static ArrayList<String> loadLoreBackpack(Player player,int slots) {
		ArrayList<String> lore = new ArrayList<String>();
		
		
		for (String line:(ArrayList<String>) FancyBags.getInstance().getConfig().get("backpackLore")) {
			lore.add(Utils.color(line.replace("{slotsAmount}", slots + "")));
		}

		if (FancyBags.getInstance().getConfig().getBoolean("showContents")) {
			lore.add(" ");
			if (!isEmpty(player.getOpenInventory().getTopInventory())) {

				//Count and sort all the items in the backpack
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
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
								structure = structure.replace("{item_name}", item.getItemMeta().getDisplayName());
							} else {
								structure = structure.replace("{item_name}", "&f" + getName(item.getType()));
							}

							lore.add(Utils.color(structure));
						}
					} else {
						for (int i = 0; i < 5;i++) {
							ItemStack item = items.get(i);
							String structure = FancyBags.getInstance().getConfig().getString("displayItemInLore");
							structure = structure.replace("{number}", item.getAmount() + "");

							if (item.getItemMeta().hasDisplayName()) {
								structure = structure.replace("{item_name}", item.getItemMeta().getDisplayName());
							} else {
								structure = structure.replace("{item_name}", "&f" + getName(item.getType()));
							}

							lore.add(Utils.color(structure));
						}

						int amount = 0;
						for (int i = 5;i < items.size();i++) {
							amount += items.get(i).getAmount();
						}

						String other = FancyBags.getInstance().getConfig().getString("otherItemsInLore");
						other = other.replace("{amount}", amount + "");
						lore.add(Utils.color(other));

					}
				} else {
					lore.add(Utils.color(FancyBags.getInstance().getConfig().getString("emptyBackpack")));
				}




			} else {
				lore.add(Utils.color(FancyBags.getInstance().getConfig().getString("emptyBackpack")));
			}

			lore.add(" ");
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
