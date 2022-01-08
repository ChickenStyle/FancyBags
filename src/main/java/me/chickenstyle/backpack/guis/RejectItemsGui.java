package me.chickenstyle.backpack.guis;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customholders.RejectItemsHolder;


public class RejectItemsGui {
	public RejectItemsGui(Player player) {
		Inventory gui = Bukkit.createInventory(new RejectItemsHolder(), 54, Component.text("Put the items here!", NamedTextColor.GRAY, TextDecoration.BOLD));

		ItemStack green = Utils.getGreenVersionGlass();
		ItemMeta meta = green.getItemMeta();
		meta.displayName(Component.text("Click here to save the blacklist/whitelist!", NamedTextColor.GREEN));
		green.setItemMeta(meta);
		gui.setItem(53, green);
		
		player.openInventory(gui);
	}
}
