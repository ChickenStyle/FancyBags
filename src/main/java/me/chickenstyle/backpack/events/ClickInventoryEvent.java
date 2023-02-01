package me.chickenstyle.backpack.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.chickenstyle.backpack.Backpack;
import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Message;
import me.chickenstyle.backpack.RejectType;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.configs.CustomBackpacks;
import me.chickenstyle.backpack.customholders.BackpackHolder;
import me.chickenstyle.backpack.customholders.CreateRecipeHolder;
import me.chickenstyle.backpack.customholders.RejectItemsHolder;
import me.chickenstyle.backpack.guis.CreateRecipeGui;


public class ClickInventoryEvent implements Listener{
	private final FancyBags main;

	public ClickInventoryEvent(final FancyBags main){
		this.main = main;
	}


	@EventHandler
	public void onClickInventory(InventoryClickEvent e) {

		Player player = (Player) e.getWhoClicked();

		if (e.getInventory().getType().equals(InventoryType.HOPPER) || e.getInventory().getType().equals(InventoryType.SHULKER_BOX)) {
			if (!main.getConfig().getBoolean("putBackpacksIntoShulkers")) {

				if (e.getClick().equals(ClickType.NUMBER_KEY)) {
					e.setCancelled(true);
				}

				if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
					return;
				}
				if (FancyBags.getNMSHandler().hasTag(e.getCurrentItem(),"BackpackID")) {
					e.setCancelled(true);
					return;
				}
			}

		}
			
		
		
		
		if (e.getView().getTopInventory().getHolder() instanceof BackpackHolder) {
			if (e.getClick().equals(ClickType.NUMBER_KEY) || e.getClick().equals(ClickType.UNKNOWN)) {
				e.setCancelled(true);
				return;
			}
			
			if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {return;}
			
			if (FancyBags.getNMSHandler().hasTag(e.getCurrentItem(),"BackpackID")) {
				e.setCancelled(true);
				player.sendMessage(FancyBags.getInstance().parse(Message.DISABLE_PLACE.getMSG()));
				return;
			}
				if (CustomBackpacks.hasBackpack(FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(),"BackpackID"))) {
					int id = FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(),"BackpackID");
					Backpack pack = CustomBackpacks.getBackpack(id);
					
					if (pack.getReject().isRejecting()) {
						if (!pack.getReject().getItems().isEmpty()) {
							if (pack.getReject().getType().equals(RejectType.BLACKLIST)) {
								for (ItemStack item:pack.getReject().getItems()) {
									
									if (!FancyBags.getNMSHandler().hasTag(e.getCurrentItem(),"BackpackID")) {
										if (isSimilar(item,e.getCurrentItem())) {
											e.setCancelled(true);
											player.sendMessage(FancyBags.getInstance().parse(Message.DISABLE_PLACE.getMSG()));
											break;
										}
									}
								}
							}

							if (pack.getReject().getType().equals(RejectType.WHITELIST)) {
								boolean contains = false;
								for (ItemStack item:pack.getReject().getItems()) {
									if (isSimilar(item,e.getCurrentItem())) {
										contains = true;
									}
								}
								if (!FancyBags.getNMSHandler().hasTag(e.getCurrentItem(),"BackpackID")) {
									if (!contains) {
										e.setCancelled(true);
										player.sendMessage(FancyBags.getInstance().parse(Message.DISABLE_PLACE.getMSG()));
									}
								}
							}
						}
					}	
				}


			
			
			
			if (e.getCurrentItem().equals(Utils.getRedVersionGlass())) {
				e.setCancelled(true);
			}
		}
		
		if (e.getInventory().getHolder() instanceof CreateRecipeHolder) {
			Backpack pack = FancyBags.creatingBackpack.get(player.getUniqueId());
			if (e.getCurrentItem() == null) {
				return;
			}
			
			if (e.getCurrentItem().equals(Utils.getGrayVersionGlass())) {
				e.setCancelled(true);
			}
			if (e.getCurrentItem().equals(Utils.getGreenVersionGlass())) {
				ArrayList<Integer> emptySlots = new ArrayList<>();
				emptySlots.add(12);
				emptySlots.add(13);
				emptySlots.add(14);
				emptySlots.add(21);
				emptySlots.add(22);
				emptySlots.add(23);
				emptySlots.add(30);
				emptySlots.add(31);
				emptySlots.add(32);

				ArrayList<ItemStack> materials = new ArrayList<>();
				int airAmount = 0;
				for (Integer i:emptySlots) {
					if (e.getInventory().getItem(i) == null || e.getInventory().getItem(i).getType().equals(Material.AIR)) {
						materials.add(new ItemStack(Material.AIR));
						airAmount++;
					} else {
						materials.add(e.getInventory().getItem(i));
					}
				}


				if (airAmount == 9) {
					pack.setRecipe(null);
					CustomBackpacks.addBackpack(pack, new HashMap<>());
					FancyBags.creatingBackpack.remove(player.getUniqueId());
					player.sendMessage(main.parse(
							"<GREEN>Backpack has been created! type /fb reload to load the recipe!"
					));
					e.setCancelled(true);
					player.closeInventory();
					return;
				}
				
				HashMap<Character,ItemStack> ingredients = new HashMap<Character,ItemStack>();
				for (ItemStack mat:materials) {
					boolean contains = false;	
					do {
						Random r = new Random();
						char symbol = (char)(r.nextInt(26) + 'a');
						if (!ingredients.isEmpty()) {
							if (!ingredients.containsKey(symbol)) {
								boolean containsMaterial = false;
								for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
								    if (entry.getValue().equals(mat)) {
								    	containsMaterial = true;
								    }
								}
								
								if (containsMaterial == false) {
									ingredients.put(symbol, mat);
								} 
								contains = true;
							}
						} else {
							ingredients.put(symbol, mat);
							contains = true;
						}
						
					} while (contains == false);
				}
				
				char matAir = 0;
				ArrayList<Character> symbols = new ArrayList<Character>(); 
				for (ItemStack mat:materials) {
					for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
					    if (mat.equals(entry.getValue())) {
					    	symbols.add(entry.getKey());
					    }
					    if (entry.getValue().getType().equals(Material.AIR)) {
					    	matAir = entry.getKey();
					    }
					}
				}
				
				
				
				String firstLine = "";
				String secondLine = "";
				String thirdLine = "";
				for (int i = 0; i < 3;i++) firstLine = (firstLine + symbols.get(i)).replace(matAir, ' ');
				for (int i = 3; i < 6;i++) secondLine = (secondLine + symbols.get(i)).replace(matAir, ' ');
				for (int i = 6; i < 9;i++) thirdLine = (thirdLine + symbols.get(i)).replace(matAir, ' ');
				
				
				ItemStack bagItem = Utils.createBackpackItemStack(pack.getName(), pack.getTexture(), pack.getSlotsAmount(),pack.getId());
				
        		final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(main,"key"),bagItem);

				recipe.shape(firstLine,secondLine,thirdLine);
				
				for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
					if (!entry.getValue().getType().equals(Material.AIR) && !(entry.getValue() == null)) {
						recipe.setIngredient(entry.getKey(), entry.getValue().getData());	
					}
				}
				
				pack.setRecipe(recipe);
				CustomBackpacks.addBackpack(pack,ingredients);
				FancyBags.creatingBackpack.remove(player.getUniqueId());
				player.sendMessage(main.parse(
						"<GREEN>Backpack has been added! type <AQUA>/fb reload</AQUA> to load the recipe!"
				));
				e.setCancelled(true);
				player.closeInventory();
			}
			
		}
		
		if (e.getInventory().getHolder() instanceof RejectItemsHolder) {
			ItemStack green = Utils.getGreenVersionGlass();
			ItemMeta meta = green.getItemMeta();
			meta.displayName(main.parse(
					"<GREEN>Click here to save the blacklist/whitelist!"
			).decoration(TextDecoration.ITALIC, false));
			green.setItemMeta(meta);

			if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
				if (e.getCurrentItem().isSimilar(green)) {
					e.setCancelled(true);
					ArrayList<ItemStack> items = new ArrayList<>();
					ItemStack[] contents = e.getView().getTopInventory().getContents();
					for (int i = 0;i < 53;i++) {
						if (contents[i] != null && !contents[i].getType().equals(Material.AIR)) {
							items.add(contents[i]);
						}
					}
					
					Backpack pack = FancyBags.creatingBackpack.get(player.getUniqueId());
					pack.getReject().setItems(items);
					FancyBags.creatingBackpack.put(player.getUniqueId(), pack);

					player.sendMessage(main.parse(
							"<GREEN>Okey, and the now lets create the recipes!"
					));

					new CreateRecipeGui(player);
				}
			}
		}
	  
	}
	
	public static boolean isSimilar(ItemStack a, ItemStack b) {
		
	    if(a == null || b == null)
	        return false;
	    
	    if(a.getType() != b.getType())
	        return false;

	    

	    ItemMeta first = a.getItemMeta();
	    ItemMeta second = b.getItemMeta();
	    

	    if (first.hasDisplayName() != second.hasDisplayName())
	    	return false;
	    
	    if (first.hasDisplayName() && second.hasDisplayName()) {
		    if (!first.displayName().equals(second.displayName()))
		    	return false;
	    }
	    
	    
	    if (first.hasLore() && second.hasLore()) {
		    if (!first.lore().equals(second.lore()))
		    	return false;
	    }

	    if (!first.getEnchants().equals(second.getEnchants())){
			return false;
		}

	    return true;
	}
	
}