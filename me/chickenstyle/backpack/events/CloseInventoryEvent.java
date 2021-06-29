package me.chickenstyle.backpack.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customevents.BackpackCloseEvent;
import me.chickenstyle.backpack.customholders.BackpackHolder;
import me.chickenstyle.backpack.customholders.CreateRecipeHolder;
import me.chickenstyle.backpack.customholders.RejectItemsHolder;

public class CloseInventoryEvent implements Listener{
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (player.isDead()) return;
		if (e.getInventory().getHolder() instanceof BackpackHolder) {

			boolean hasTag = FancyBags.getVersionHandler().hasTag(player.getItemInHand(), "BackpackTitle");
			int slotsAmount = hasTag ? FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "SlotsAmount") : FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "Size");

			ItemStack bag = Utils.loadBackpack(player,slotsAmount);

			if (!hasTag) {
				bag = Utils.clearOldTags(bag);
			}

			e.getPlayer().setItemInHand(bag);

			player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
			Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, e.getView().getTopInventory()));

		}
		
		if (e.getInventory().getHolder() instanceof RejectItemsHolder) {
			if (FancyBags.creatingBackpack.get(player.getUniqueId()).getReject().getItems() == null) {
				player.sendMessage(ChatColor.RED + "Backpack creation has been disbanded!");
				FancyBags.creatingBackpack.remove(player.getUniqueId());
			}
		}
		
		if (e.getInventory().getHolder() instanceof CreateRecipeHolder) {
			if (FancyBags.creatingBackpack.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "Backpack creation has been disbanded!");
				FancyBags.creatingBackpack.remove(player.getUniqueId());	
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemDropEvent(PlayerDropItemEvent e) {
		if (!FancyBags.getVersionHandler().hasTag(e.getItemDrop().getItemStack(),"BackpackID")) return;

		if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				e.setCancelled(true);
		}

	}

}
