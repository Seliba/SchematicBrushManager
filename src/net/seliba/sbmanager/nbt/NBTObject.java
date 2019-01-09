package me.mylogo.divineitems.items;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dennis Heckmann on 24.08.17
 * Copyright (c) 2017 Dennis Heckmann
 * GitHub: https://github.com/Mylogo
 * Web: http://mylogo.me
 * Mail: contact@mylogo.me
 */
public class NBTObject<This extends NBTObject<This>> {

    public static final byte BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, BYTE_ARRAY = 7,
            STRING = 8, LIST = 9, TAG = 10, INT_ARRAY = 11;

    Object NBT;

    NBTObject() {}

    public NBTObject(Object NMS_NBT) {
        this.NBT = NMS_NBT;
    }

    public String getString(String key) {
        return Ref.NBT.getString(NBT, key);
    }

    public This setString(String key, String value) {
        Ref.NBT.setString(NBT, key, value);
        return (This) this;
    }

    public int getInt(String key) {
        return Ref.NBT.getInt(NBT, key);
    }

    public This setInt(String key, int value) {
        Ref.NBT.setInt(NBT, key, value);
        return (This) this;
    }

    public double getDouble(String key) {
        return Ref.NBT.getDouble(NBT, key);
    }

    public This setDouble(String key, double value) {
        Ref.NBT.setDouble(NBT, key, value);
        return (This) this;
    }

    public long getLong(String key) {
        return Ref.NBT.getLong(NBT, key);
    }

    public This setLong(String key, long value) {
        Ref.NBT.setLong(NBT, key, value);
        return (This) this;
    }

    public boolean hasKey(String key) {
        return Ref.NBT.hasKey(NBT, key);
    }

    public NBTObject getTag(String key) {
        return new NBTObject(Ref.NBT.getCompound(NBT, key));
    }

    public void setTag(String key, NBTObject nbt) {
        Ref.NBT.setCompound(NBT, key, nbt.NBT);
    }

    public NBTList getList(String key, byte type) {
        return new NBTList(Ref.NBT.getList(NBT, key, type));
    }

    public static void writeToStream(NBTObject nbt, OutputStream output) throws IOException {
        if (nbt.getClass() != NBTObject.class) {
            throw new IllegalArgumentException("No inherited classes of NBTObject may be passed. " +
                    "Only actual NBTObjects are allowed.");
        }
        Ref.NBT.writeToStream(nbt.NBT, output);
    }

    public static NBTObject readFromStream(InputStream input) throws IOException {
        return new NBTObject(Ref.NBT.readFromStream(input));
    }

    public static NBTObject newNBTObject() {
        return new NBTObject(Ref.NBT.createNBTTag());
    }

    @Override
    public String toString() {
        return NBT.toString();
    }

    //    public List<String> getStringList(String key) {
//        return Ref.NBT.get
//    }

}
