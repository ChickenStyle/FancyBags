package me.chickenstyle.backpack.prompts;

import java.util.ArrayList;

import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.backpack.Backpack;
import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.RejectItems;
import me.chickenstyle.backpack.RejectType;
import me.chickenstyle.backpack.Utils;
import me.chickenstyle.backpack.guis.CreateRecipeGui;

public class RejectPrompt extends BooleanPrompt{

	@Override
	public String getPromptText(ConversationContext context) {
		return "<gray>Do you want the backpack to have blacklist/whitelist?";
	}
	

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, boolean boo) {
		Player player = (Player) context.getForWhom();
		Backpack pack = FancyBags.creatingBackpack.get(player.getUniqueId());
		pack.setReject(new RejectItems(boo, RejectType.NONE, new ArrayList<>()));
		FancyBags.creatingBackpack.put(player.getUniqueId(), pack);
		if (boo == true) {
			return new RejectTypePrompt();
		} else {
			new CreateRecipeGui(player);
			return Prompt.END_OF_CONVERSATION;
		}
		

		
		
	}
	

}
