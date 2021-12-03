package me.chickenstyle.backpack.versions;

import me.chickenstyle.backpack.NMSHandler;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class Handler_1_18_R1 implements NMSHandler {

    @Override
    public ItemStack removeTag(ItemStack item,String tag) {


        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        CompoundTag itemCompound;
        if(nmsItem.hasTag()){
            itemCompound = nmsItem.getTag();
        }else{
            itemCompound = new CompoundTag();
        }


        itemCompound.remove(tag);//removes the tag

        nmsItem.setTag(itemCompound);

        //nmsItem.getClass().getMethod("setTag", CompoundTag.class).invoke(nmsItem, (net.minecraft.nbt.CompoundTag)itemCompound);

        return CraftItemStack.asBukkitCopy(nmsItem);

        //NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        //itemCompound.remove(tag);
        //nmsItem.setTag(itemCompound);
        //return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public ItemStack addIntTag(ItemStack item,String tag, int data) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        CompoundTag itemCompound;
        if(nmsItem.hasTag()){
            itemCompound = nmsItem.getTag();
        }else{
            itemCompound = new CompoundTag();
        }


        itemCompound.putInt(tag, data);

        nmsItem.setTag(itemCompound);
        return CraftItemStack.asBukkitCopy(nmsItem);



        //NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        //itemCompound.setInt(tag, data);
        //nmsItem.setTag(itemCompound);
        //return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public ItemStack addStringTag(ItemStack item,String tag, String data) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);


        CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        itemCompound.putString(tag, data);
        nmsItem.setTag(itemCompound);
        return CraftItemStack.asBukkitCopy(nmsItem);



    }

    @Override
    public boolean hasTag(ItemStack item,String tag) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();

        return itemCompound.contains(tag);

    }

    @Override
    public int getIntData(ItemStack item,String tag) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        return itemCompound.getInt(tag);


    }

    @Override
    public String getStringData(ItemStack item,String tag) {
        net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
        return itemCompound.getString(tag);



    }

    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


}
