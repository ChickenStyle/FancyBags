package me.chickenstyle.backpack.events;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Message;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customevents.BackpackOpenEvent;
import me.chickenstyle.backpack.customholders.BackpackHolder;


public class RightClickEvent implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;
			if (!Bukkit.getVersion().contains("1.8")) {
				if (FancyBags.getVersionHandler().hasTag(e.getPlayer().getInventory().getItemInOffHand(),"BackpackID")) {e.setCancelled(true); return;}
			}
			
			if (FancyBags.getVersionHandler().hasTag(e.getItem(),"BackpackID")) {
				
				Player player = e.getPlayer();
				
				if (e.getItem().getAmount() == 1) {
					e.setCancelled(true);
					player.playSound(player.getLocation(), Utils.getVersionChestOpenSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
					
					Inventory gui = null;
					int slotsAmount = 0;
					if (!FancyBags.getVersionHandler().hasTag(e.getItem(), "BackPack")) {
						String title = FancyBags.getVersionHandler().getStringData(e.getItem(), "BackpackTitle");
						slotsAmount = FancyBags.getVersionHandler().getIntData(e.getItem(),"SlotsAmount");
						
						int neededSlots = slotsAmount;
						
						while (neededSlots % 9 != 0) {
							neededSlots++;
						}
						
						
						gui = Bukkit.createInventory(new BackpackHolder(), neededSlots, Utils.color(title));
						
						for (int i = 0;i < slotsAmount;i++) {
							gui.setItem(i, Utils.itemstackFromBase64(FancyBags.getVersionHandler().getStringData(e.getItem(), i + "")));
						}
						
						try {
							for (int i = slotsAmount; i < 54;i++) {
								gui.setItem(i, Utils.getRedVersionGlass());
							}
						} catch (Exception ex) {
							
						}	
					} else {
						Inventory data = Utils.inventoryFromBase64(FancyBags.getVersionHandler().getStringData(e.getItem(), "BackPack"));
						gui = Bukkit.createInventory(data.getHolder(), data.getSize(),
								Utils.color(FancyBags.getVersionHandler().getStringData(e.getItem(),"Title")));
						
						gui.setContents(data.getContents());
						slotsAmount = FancyBags.getVersionHandler().getIntData(e.getItem(),"Size");
					}

					for (int i = slotsAmount ;i < gui.getSize() ;i++) {
						gui.setItem(i, Utils.getRedVersionGlass());
					}
					player.openInventory(gui);
					BackpackOpenEvent event = new BackpackOpenEvent(player, gui);
					Bukkit.getPluginManager().callEvent(event);
					

				} else {
					e.setCancelled(true);
					player.sendMessage(Message.CANCEL_OPEN.getMSG());
				}
			
			}
		}
	}

	@EventHandler
	public void onSlotChangeEvent(PlayerItemHeldEvent e) {
		if (e.getPlayer().getOpenInventory().getTopInventory() == null) return;

		if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
			e.setCancelled(true);
		}


	}
}
