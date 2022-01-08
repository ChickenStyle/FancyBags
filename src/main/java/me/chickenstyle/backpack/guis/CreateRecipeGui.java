package me.chickenstyle.backpack.guis;


import java.util.ArrayList;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.customholders.CreateRecipeHolder;


public class CreateRecipeGui {
	public CreateRecipeGui(Player player) {
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		emptySlots.add(12);
		emptySlots.add(13);
		emptySlots.add(14);
		emptySlots.add(21);
		emptySlots.add(22);
		emptySlots.add(23);
		emptySlots.add(30);
		emptySlots.add(31);
		emptySlots.add(32);

		Inventory gui = Bukkit.createInventory(new CreateRecipeHolder(), 45, Component.text("Add recipe for the backpack!", NamedTextColor.GRAY, TextDecoration.BOLD));
		
		for (int i = 0; i < 45;i++) {
			if (!emptySlots.contains(i)) {
				gui.setItem(i, Utils.getGrayVersionGlass());
			}
			
			if (i == 44) {
				gui.setItem(i, Utils.getGreenVersionGlass());
			}
		}
		player.openInventory(gui);
	}
}
