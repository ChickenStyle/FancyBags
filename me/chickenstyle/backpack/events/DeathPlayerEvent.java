package me.chickenstyle.backpack.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customevents.BackpackCloseEvent;
import me.chickenstyle.backpack.customholders.BackpackHolder;

public class DeathPlayerEvent implements Listener {
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
			if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				boolean hasTag = FancyBags.getVersionHandler().hasTag(player.getItemInHand(), "BackpackTitle");
				int slotsAmount = hasTag ? FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "SlotsAmount") : FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "Size");
				ItemStack backpack = Utils.loadBackpack(player,slotsAmount);
				
				if (!hasTag) {
					backpack = Utils.clearOldTags(backpack);
				}
				
				
				List<ItemStack> list = new ArrayList<ItemStack>();
				
				for (ItemStack item:e.getDrops()) {
					if (item.equals(player.getItemInHand())) {
						list.add(backpack);
					} else {
						list.add(item);
					}
				}
				
				e.getDrops().clear();
				e.getDrops().addAll(list);

				player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
				Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, player.getOpenInventory().getTopInventory()));	
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void playerLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
			if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				boolean hasTag = FancyBags.getVersionHandler().hasTag(player.getItemInHand(), "BackpackTitle");
				int slotsAmount = hasTag ? FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "SlotsAmount") : FancyBags.getVersionHandler().getIntData(player.getItemInHand(), "Size");
				ItemStack backpack = Utils.loadBackpack(player,slotsAmount);
				player.setItemInHand(backpack);

				player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
				Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, player.getOpenInventory().getTopInventory()));	
			}
		}
	}
	

}
