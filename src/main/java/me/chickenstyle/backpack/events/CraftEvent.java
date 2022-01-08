package me.chickenstyle.backpack.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.Message;
import me.chickenstyle.backpack.RandomString;

public class CraftEvent implements Listener{
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if (FancyBags.getNMSHandler().hasTag(e.getRecipe().getResult(),"BackpackID")) {
			if (e.getClickedInventory() == null) return;
				if (e.isShiftClick()) {
					e.setCancelled(true);
					e.getWhoClicked().sendMessage(Message.DISABLE_CRAFT.getMSG());
					return;
				}
			e.getInventory().setResult(FancyBags.getNMSHandler().addStringTag(e.getRecipe().getResult(),"Random",new RandomString().nextString()));
		}
	}
}
