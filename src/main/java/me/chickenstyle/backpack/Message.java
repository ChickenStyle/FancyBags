package me.chickenstyle.backpack;


public enum Message {

    NO_PERMISSION(getString("messages.noPermission")),
    DISABLE_PLACE(getString("messages.disablePlace")),
    GIVE_MESSAGE(getString("messages.giveMessage")),
	DISABLE_CRAFT(getString("messages.disableCraft")),
	CANCEL_OPEN(getString("messages.cancelOpen"));
    private final String error;

    Message(String error) {
        this.error = error;
    }

    public String getMSG() {
        return error;
    }
    
    /*private static String Color(String text) {
    	return ChatColor.translateAlternateColorCodes('&', text);
    }*/
    
    private static String getString(String path) {
    	return FancyBags.getInstance().getConfig().getString(path);
    }
}