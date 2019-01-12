package net.seliba.sbmanager.nbt;

/**
 * Created by Dennis Heckmann on 24.08.17
 * Copyright (c) 2017 Dennis Heckmann
 * GitHub: https://github.com/Mylogo
 * Web: http://mylogo.me
 * Mail: contact@mylogo.me
 */
public class NBTList extends NBTObject<NBTList> {

    public NBTList() {
        super(Ref.NBT.createNBTList());
    }

    public NBTList(Object NMS_NBT) {
        super(NMS_NBT);
    }

    public NBTList(NBTObject nbt) {
        this.NBT = nbt.NBT;
    }

    public NBTObject getTag(int i) {
        return new NBTObject(Ref.NBT.getListCompound(NBT, i));
    }

    public String getString(int i) {
        return Ref.NBT.getListString(NBT, i);
    }

    public int getInt(int i) {
        return Ref.NBT.getListInt(NBT, i);
    }

    public double getDouble(int i ) {
        return Ref.NBT.getListDouble(NBT, i);
    }

    public float getFloat(int i) {
        return Ref.NBT.getListFloat(NBT, i);
    }

    public NBTList add(NBTObject nbt) {
        Ref.NBT.addListElement(NBT, nbt.NBT);
        return this;
    }

    public NBTList add(byte type, Object value) {
        if (BYTE <= type && type <= STRING || type == INT_ARRAY) {
            Object nbtBase = Ref.NBT.createNBTBase(type);
            Ref.NBT.setNBTBaseData(nbtBase, type, value);
            Ref.NBT.addListElement(NBT, nbtBase);
        }
        return this;
    }

    public int size() {
        return Ref.NBT.getListSize(NBT);
    }

}
