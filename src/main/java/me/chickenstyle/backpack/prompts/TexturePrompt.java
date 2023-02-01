package me.chickenstyle.backpack.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.chickenstyle.backpack.Backpack;
import me.chickenstyle.backpack.FancyBags;
import org.jetbrains.annotations.NotNull;

public class TexturePrompt extends StringPrompt{

	@Override
	public @NotNull String getPromptText(@NotNull ConversationContext context) {
		return "<gray>Enter backpack's texture\n <gray>(to get the texture go to <gold>https://minecraft-heads.com/custom-heads \n"
				+ "<gray>choose a texture,"
				+ "<gray>copy the 'Minecraft-URL' part and paste it here)";
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Player player = (Player) context.getForWhom();
		player.sendMessage(
				FancyBags.getInstance().parse(
						"<green>" + input
				)
		);
		Backpack pack = FancyBags.creatingBackpack.get(player.getUniqueId());
		pack.setTexture(input);
		FancyBags.creatingBackpack.put(player.getUniqueId(), pack);
		return new RejectPrompt();
	}

}
