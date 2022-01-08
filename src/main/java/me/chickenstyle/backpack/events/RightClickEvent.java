package me.chickenstyle.backpack.events;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getItem() == null || e.getItem().getType() == Material.AIR) return;

			if (FancyBags.getNMSHandler().hasTag(e.getPlayer().getInventory().getItemInOffHand(),"BackpackID")) {
				e.setCancelled(true);
				return;
			}

			if (FancyBags.getNMSHandler().hasTag(e.getItem(),"BackpackID")) {
				
				Player player = e.getPlayer();
				
				if (e.getItem().getAmount() == 1) {
					e.setCancelled(true);
					player.playSound(player.getLocation(), Utils.getVersionChestOpenSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
					
					Inventory gui;
					int slotsAmount = 0;
					if (!FancyBags.getNMSHandler().hasTag(e.getItem(), "BackPack")) {
						String title = FancyBags.getNMSHandler().getStringData(e.getItem(), "BackpackTitle");
						slotsAmount = FancyBags.getNMSHandler().getIntData(e.getItem(),"SlotsAmount");
						
						int neededSlots = slotsAmount;
						
						while (neededSlots % 9 != 0) {
							neededSlots++;
						}

						if(title != null && !title.isBlank()){
							gui = Bukkit.createInventory(new BackpackHolder(), neededSlots, LegacyComponentSerializer.legacyAmpersand().deserialize(title));
						}else{
							gui = Bukkit.createInventory(new BackpackHolder(), neededSlots, Component.text(" "));
						}

						//gui = Bukkit.createInventory(new BackpackHolder(), neededSlots, Utils.color(title));
						
						for (int i = 0;i < slotsAmount;i++) {
							gui.setItem(i, Utils.itemstackFromBase64(FancyBags.getNMSHandler().getStringData(e.getItem(), i + "")));
						}
						
						try {
							for (int i = slotsAmount; i < 54;i++) {
								gui.setItem(i, Utils.getRedVersionGlass());
							}
						} catch (Exception ex) {
							
						}	
					} else {
						Inventory data = Utils.inventoryFromBase64(FancyBags.getNMSHandler().getStringData(e.getItem(), "BackPack"));

						gui = Bukkit.createInventory(data.getHolder(), data.getSize(),
								LegacyComponentSerializer.legacyAmpersand().deserialize(FancyBags.getNMSHandler().getStringData(e.getItem(),"Title")));



						gui.setContents(data.getContents());
						slotsAmount = FancyBags.getNMSHandler().getIntData(e.getItem(),"Size");
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

		if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
			e.setCancelled(true);
		}


	}
}
