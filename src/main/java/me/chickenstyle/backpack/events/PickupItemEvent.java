package me.chickenstyle.backpack.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;

import me.chickenstyle.backpack.FancyBags;

public class PickupItemEvent implements Listener{
	@EventHandler
	public void onPickupItem(InventoryPickupItemEvent e) {
		if (!FancyBags.getInstance().getConfig().getBoolean("putBackpacksIntoShulkers")) {
			if (e.getInventory().getType().equals(InventoryType.HOPPER)) {
				if (FancyBags.getNMSHandler().hasTag(e.getItem().getItemStack(),"BackpackID")) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	@EventHandler
	public void onItemInventoryMove(InventoryMoveItemEvent e) {
		if (!FancyBags.getInstance().getConfig().getBoolean("putBackpacksIntoShulkers")) {
			if (e.getDestination().getType().equals(InventoryType.HOPPER)) {
				if (FancyBags.getNMSHandler().hasTag(e.getItem(),"BackpackID")) {
					e.setCancelled(true);
				}
			}
		}
	}
}
