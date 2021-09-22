package me.chickenstyle.backpack.versions;

import me.chickenstyle.backpack.NMSHandler;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class Handler_1_17_R1 implements NMSHandler {

	@Override
	public ItemStack removeTag(ItemStack item,String tag) {


		try {
			net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);


			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();


			itemCompound.getClass().getMethod("remove",String.class).invoke(itemCompound,tag); //removes the tag

			nmsItem.getClass().getMethod("setTag",itemCompound.getClass()).invoke(nmsItem,itemCompound);

			return CraftItemStack.asBukkitCopy(nmsItem);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//itemCompound.remove(tag);
		//nmsItem.setTag(itemCompound);
		//return CraftItemStack.asBukkitCopy(nmsItem);
	}
	
	@Override
	public ItemStack addIntTag(ItemStack item,String tag, int data) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

		try {
			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();

			itemCompound.getClass().getMethod("setInt",String.class,int.class).invoke(itemCompound,tag,data);

			nmsItem.getClass().getMethod("setTag",itemCompound.getClass()).invoke(nmsItem,itemCompound);
			return CraftItemStack.asBukkitCopy(nmsItem);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;

		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//itemCompound.setInt(tag, data);
		//nmsItem.setTag(itemCompound);
		//return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public ItemStack addStringTag(ItemStack item,String tag, String data) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

		try {
			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();

			itemCompound.getClass().getMethod("setString",String.class,String.class).invoke(itemCompound,tag,data);

			nmsItem.getClass().getMethod("setTag",itemCompound.getClass()).invoke(nmsItem,itemCompound);
			return CraftItemStack.asBukkitCopy(nmsItem);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;

		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//itemCompound.setString(tag, data);
		//nmsItem.setTag(itemCompound);
		//return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public boolean hasTag(ItemStack item,String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

		try {
			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();

			return (boolean) itemCompound.getClass().getMethod("hasKey",String.class).invoke(itemCompound,tag);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return false;
		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//return itemCompound.hasKey(tag);
	}

	@Override
	public int getIntData(ItemStack item,String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		try {
			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();

			return (int) itemCompound.getClass().getMethod("getInt",String.class).invoke(itemCompound,tag);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return 0;

		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//return itemCompound.getInt(tag);
	}

	@Override
	public String getStringData(ItemStack item,String tag) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		try {
			Object itemCompound = (nmsItem.hasTag()) ? nmsItem.getClass().getMethod("getTag").invoke(nmsItem) :
					getNMSClass("net.minecraft.nbt.NBTTagCompound").newInstance();

			return (String) itemCompound.getClass().getMethod("getString",String.class).invoke(itemCompound,tag);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return "";
		//NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		//return itemCompound.getString(tag);
	}

	private Class<?> getNMSClass(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	 
}
