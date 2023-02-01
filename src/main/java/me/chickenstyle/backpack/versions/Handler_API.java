package me.chickenstyle.backpack.versions;

import me.chickenstyle.backpack.FancyBags;
import me.chickenstyle.backpack.NMSHandler;
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
        if(itemMeta == null || tag == null || data == null){
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

        final NamespacedKey key = new NamespacedKey(main, tag);

        if (itemPDB.has(key, PersistentDataType.INTEGER)) {
            final Object intData = itemPDB.get(key, PersistentDataType.INTEGER);

            return intData != null ? (int)intData : 0;
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
        }

        return null;
    }



}
