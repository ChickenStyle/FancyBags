package me.chickenstyle.backpack.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customevents.BackpackCloseEvent;
import me.chickenstyle.backpack.customholders.BackpackHolder;

public class DeathPlayerEvent implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();

		//Prevent dupe bug where if you have keepInventory enabled, open the backpack, hold an item from the backpack on the cursor
		//and then die like that with the gui and everything open, the item will drop on the floor AND will stay in the backpack.
		//thus, it will be duped. This sets the cursor item to null on Death if keepInventory is enabled.
		if(e.getKeepInventory() || e.getDrops().size() == 0){
			player.setItemOnCursor(null);
		}


		if (Boolean.TRUE.equals(player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY))) {
			player.getInventory().getItemInMainHand();
			if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
				if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {

					boolean hasTag = FancyBags.getNMSHandler().hasTag(player.getInventory().getItemInMainHand(), "BackpackTitle");
					int slotsAmount = hasTag ? FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "SlotsAmount") : FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "Size");

					ItemStack bag = Utils.loadBackpack(player,slotsAmount);

					if (!hasTag) {
						bag = Utils.clearOldTags(bag);
					}

					player.getInventory().setItemInMainHand(bag);

					player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
					Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, player.getOpenInventory().getTopInventory()));

				}
			}
		} else {
			if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
				if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
					boolean hasTag = FancyBags.getNMSHandler().hasTag(player.getInventory().getItemInMainHand(), "BackpackTitle");
					int slotsAmount = hasTag ? FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "SlotsAmount") : FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "Size");
					ItemStack backpack = Utils.loadBackpack(player,slotsAmount);

					if (!hasTag) {
						backpack = Utils.clearOldTags(backpack);
					}


					List<ItemStack> list = new ArrayList<>();

					for (ItemStack item:e.getDrops()) {
						if (item.equals(player.getInventory().getItemInMainHand())) {
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



	}

	@EventHandler
	public void onPlayerTeleport(EntityPortalEnterEvent e) {
		if (!(e.getEntity() instanceof Player player)) { return;}

		if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {

			boolean hasTag = FancyBags.getNMSHandler().hasTag(player.getInventory().getItemInMainHand(), "BackpackTitle");
			int slotsAmount = hasTag ? FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "SlotsAmount") : FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "Size");

			ItemStack bag = Utils.loadBackpack(player,slotsAmount);

			if (!hasTag) {
				bag = Utils.clearOldTags(bag);
			}

			player.setItemInHand(bag);

			player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
			Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, player.getOpenInventory().getTopInventory()));

		}

	}
	
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		player.getInventory().getItemInMainHand();
		if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
			if (player.getOpenInventory().getTopInventory().getHolder() instanceof BackpackHolder) {
				boolean hasTag = FancyBags.getNMSHandler().hasTag(player.getInventory().getItemInMainHand(), "BackpackTitle");
				int slotsAmount = hasTag ? FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "SlotsAmount") : FancyBags.getNMSHandler().getIntData(player.getInventory().getItemInMainHand(), "Size");
				ItemStack backpack = Utils.loadBackpack(player,slotsAmount);
				player.getInventory().setItemInMainHand(backpack);

				player.playSound(player.getLocation(), Utils.getVersionChestCloseSound(), (float) FancyBags.getInstance().getConfig().getDouble("soundLevelOfBackpacks"), (float) FancyBags.getInstance().getConfig().getDouble("pitchLevelOfBackpacks"));
				Bukkit.getPluginManager().callEvent(new BackpackCloseEvent(player, player.getOpenInventory().getTopInventory()));	
			}
		}
	}
	

}
