package me.chickenstyle.backpack.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (player.isDead()) return;
		if (e.getInventory().getHolder() instanceof BackpackHolder) {

			boolean hasTag = FancyBags.getNMSHandler().hasTag(player.getInventory().getItemInMainHand(), "BackpackTitle");
			int slotsAmount = hasTag ? FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "SlotsAmount") : FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "Size");


			ItemStack bag = Utils.loadBackpack(player,slotsAmount);
			if (!hasTag) {
				FancyBags.getInstance().getLogger().info("Removing old tags from bag...");
				bag = Utils.clearOldTags(bag);
			}

			if(!e.getPlayer().getInventory().getItemInMainHand().isSimilar(bag)){
				//player.sendMessage(Component.text("BackPack saved!", NamedTextColor.GREEN));
				//Bukkit.getConsoleSender().sendMessage(Component.text(e.getPlayer().getInventory().getItemInMainHand().toString(), NamedTextColor.GREEN));
				//Bukkit.getConsoleSender().sendMessage(Component.text(bag.toString(), NamedTextColor.YELLOW));

				e.getPlayer().getInventory().setItemInMainHand(bag); //Only if not equal! Removes the unnecessary animation
			}


			player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
			Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, e.getView().getTopInventory()));

		}
		
		if (e.getInventory().getHolder() instanceof RejectItemsHolder) {
			if (FancyBags.creatingBackpack.get(player.getUniqueId()).getReject().getItems() == null) {
				player.sendMessage(FancyBags.getInstance().parse("<red>Backpack creation has been disbanded!"));
				FancyBags.creatingBackpack.remove(player.getUniqueId());
			}
		}
		
		if (e.getInventory().getHolder() instanceof CreateRecipeHolder) {
			if (FancyBags.creatingBackpack.containsKey(player.getUniqueId())) {
				player.sendMessage(FancyBags.getInstance().parse("<red>Backpack creation has been disbanded!"));
				FancyBags.creatingBackpack.remove(player.getUniqueId());	
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemDropEvent(PlayerDropItemEvent e) {
		if (!FancyBags.getNMSHandler().hasTag(e.getItemDrop().getItemStack(),"BackpackID")) return;

		if (e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				e.setCancelled(true);
		}

	}

}
