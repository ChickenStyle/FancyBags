package me.chickenstyle.backpack.versions;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.NMSHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Handler_API implements NMSHandler {
    final FancyBags main;

    public Handler_API(final FancyBags main){
        this.main = main;
    }

    @Override
    public ItemStack removeTag(ItemStack item,String tag) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return null;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);


        itemPDB.remove(key);

        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public ItemStack addIntTag(ItemStack item,String tag, int data) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return null;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);


        itemPDB.set(key, PersistentDataType.INTEGER, data);


        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public ItemStack addStringTag(ItemStack item, String tag, String data) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return null;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);


        itemPDB.set(key, PersistentDataType.STRING, data);


        item.setItemMeta(itemMeta);


        return item;
    }

    public ItemMeta getItemMeta(ItemStack item){
        /*ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null){
            itemMeta = Bukkit.getItemFactory().getItemMeta(item.getType());
            item.setItemMeta(itemMeta);
            return itemMeta;
        }*/

        return item.getItemMeta();
    }

    @Override
    public boolean hasTag(ItemStack item,String tag) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return false;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);

        if(itemPDB.has(key, PersistentDataType.INTEGER) || itemPDB.has(key, PersistentDataType.STRING)){
            return true;
        }else{
            if(FancyBags.getLegacyNMSHandler() != null){
                if(FancyBags.getLegacyNMSHandler().hasTag(item, tag)){
                    //Try to convert
                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Converting old NMS Tag to new PDC Tag..."
                    ));
                    final int intTag = FancyBags.getLegacyNMSHandler().getIntData(item, tag);
                    final String stringTag = FancyBags.getLegacyNMSHandler().getStringData(item, tag);

                    FancyBags.getLegacyNMSHandler().removeTag(item, tag);

                    if(intTag != -9304294){
                        addIntTag(item, tag, intTag);
                        main.getServer().getConsoleSender().sendMessage(main.parse(
                                "    <GREEN>-> Added int PDC tag: <GRAY>" + intTag
                        ));
                    }

                    if(stringTag != null && !stringTag.isBlank()){
                        addStringTag(item, tag, stringTag);
                        main.getServer().getConsoleSender().sendMessage(main.parse(
                                "    <GREEN>-> Added String PDC tag: <GRAY>" + stringTag
                        ));
                    }

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "    <GREEN>--> Test: has Tag?: <GRAY>" + hasTag(item, tag)
                    ));

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Conversion completed!"
                    ));
                    return true;
                }
            }
        }

        return itemPDB.has(key, PersistentDataType.INTEGER) || itemPDB.has(key, PersistentDataType.STRING);

    }

    @Override
    public int getIntData(ItemStack item,String tag) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return 0;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);

        if (itemPDB.has(key, PersistentDataType.INTEGER)) {
            return itemPDB.get(key, PersistentDataType.INTEGER);
        }else{
            if(FancyBags.getLegacyNMSHandler() != null){
                if(FancyBags.getLegacyNMSHandler().hasTag(item, tag)){
                    //Try to convert
                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Converting old NMS Tag to new PDC Tag..."
                    ));
                    final int intTag = FancyBags.getLegacyNMSHandler().getIntData(item, tag);

                    FancyBags.getLegacyNMSHandler().removeTag(item, tag);

                    if(intTag != -9304294){
                        addIntTag(item, tag, intTag);
                        main.getServer().getConsoleSender().sendMessage(main.parse(
                                "    <GREEN>-> Added int PDC tag: <GRAY>" + intTag
                        ));
                    }

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "    <GREEN>--> Test: has Tag?: <GRAY>" + hasTag(item, tag)
                    ));

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Conversion completed!"
                    ));

                    return intTag;
                }
            }
        }

        return 0;
    }

    @Override
    public String getStringData(ItemStack item,String tag) {
        final ItemMeta itemMeta = getItemMeta(item);
        if(itemMeta == null){
            return null;
        }

        final PersistentDataContainer itemPDB = itemMeta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(main, tag);

        if (itemPDB.has(key, PersistentDataType.STRING)) {
            return itemPDB.get(key, PersistentDataType.STRING);
        }else{
            if(FancyBags.getLegacyNMSHandler() != null){
                if(FancyBags.getLegacyNMSHandler().hasTag(item, tag)){
                    //Try to convert
                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Converting old NMS Tag to new PDC Tag..."
                    ));
                    final String stringTag = FancyBags.getLegacyNMSHandler().getStringData(item, tag);

                    FancyBags.getLegacyNMSHandler().removeTag(item, tag);


                    if(stringTag != null && !stringTag.isBlank()){
                        addStringTag(item, tag, stringTag);
                        main.getServer().getConsoleSender().sendMessage(main.parse(
                                "    <GREEN>-> Added String PDC tag: <GRAY>" + stringTag
                        ));
                    }

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "    <GREEN>--> Test: has Tag?: <GRAY>" + hasTag(item, tag)
                    ));

                    main.getServer().getConsoleSender().sendMessage(main.parse(
                            "<GREEN>Conversion completed!"
                    ));

                    return stringTag;
                }
            }
        }

        return null;
    }



}
