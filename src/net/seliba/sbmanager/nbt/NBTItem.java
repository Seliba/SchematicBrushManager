package net.seliba.sbmanager.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Dennis Heckmann on 24.08.17
 * Copyright (c) 2017 Dennis Heckmann
 * GitHub: https://github.com/Mylogo
 * Web: http://mylogo.me
 * Mail: contact@mylogo.me
 */
public class NBTItem extends NBTObject<NBTItem> {

    private ItemStack item;
    private Object nmsItem;

    public NBTItem(ItemBuilder builder) {
        this(builder.build());
    }

    public NBTItem(ItemStack item) {
        this.item = item;
        this.nmsItem = Ref.NBT.toNMSItem(item);
        this.NBT = Ref.NBT.getNBTTag(nmsItem);
    }

//    public NBTItem(NBTObject nbt) {
//        this.nmsItem = Ref.NBT.nmsItemStackFromNBT(nbt.NBT);
//    }

    public ItemStack getItem() {
        Ref.NBT.applyNBT(nmsItem, NBT);
        item = Ref.NBT.toBukkitItem(nmsItem);
        return item;
    }

    public ItemStack build() {
        return getItem();
    }

    public static NBTObject toNBTObject(ItemStack item) {
        Object nmsItem = Ref.NBT.toNMSItem(item);
        NBTObject nbt = new NBTObject(Ref.NBT.createNBTTag());
        Ref.NBT.saveNmsItemToNBT(nmsItem, nbt.NBT);
        return nbt;
    }

    public static ItemStack fromNBTObject(NBTObject nbt) {
        Object nmsItem = Ref.NBT.nmsItemStackFromNBT(nbt.NBT);
        return Ref.NBT.toBukkitItem(nmsItem);
    }

//    public NBTObject toNBTObject(NBTObject nbt) {
//        Ref.NBT.saveNmsItemToNBT(nmsItem, nbt.NBT);
//        return nbt;
//    }
//
//    public NBTObject toNBTObject() {
//        return toNBTObject(new NBTObject(Ref.NBT.createNBTTag()));
////        Object savedNbt = Ref.NBT.saveNmsItemToNBT(nmsItem, Ref.NBT.createNBTTag());
////        return new NBTObject(savedNbt);
//    }

}
