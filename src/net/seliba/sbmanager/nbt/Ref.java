package net.seliba.sbmanager.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Ref {

  public static final String VERSION = org.bukkit.Bukkit.getServer().getClass().getPackage()
      .getName().replace(".", ",").split(",")[3];
  private static final boolean IS_1_8 = VERSION.contains("v1_8_");

  public static final Class<?> CraftPlayer = getOBC(
      "entity.CraftPlayer"), IChatBaseComponent = getNMS("IChatBaseComponent"),
      ChatSerializer = getNMS("IChatBaseComponent$ChatSerializer"),
      PacketPlayOutChat = getNMS("PacketPlayOutChat"),
      EntityPlayer = getNMS("EntityPlayer"),
      PlayerConnection = getNMS("PlayerConnection"),
      Packet = getNMS("Packet");

  public static final Method CRAFT_PLAYER_GET_HANDLE = getMethod(CraftPlayer, "getHandle"),
      CHAT_SERIALIZER_A = getMethod(ChatSerializer, "a", String.class),
      PLAYER_CONNECTION_SEND_PACKET = getMethod(PlayerConnection, "sendPacket", Packet);

  public static final Field ENTITY_PLAYER_PLAYER_CONNECTION = getField(EntityPlayer,
      "playerConnection");

  private static BiConsumer<Player, String> actionBarSender = null;

  static {
    if (VERSION.contains("12")) {
      init12ActionBar();
    } else {
      initPre12ActionBar();
    }
  }

  private static void init12ActionBar() {
    final Class<?> ChatMessageType = getNMS("ChatMessageType");
    final Object[] enumConstants = ChatMessageType.getEnumConstants();
    Object gameInfo = null;
    for (Object enumConstant : enumConstants) {
      if (enumConstant.toString().equals("GAME_INFO")) {
        gameInfo = enumConstant;
        break;
      }
    }
    final Constructor<?> PacketPlayOutChatConstructor = getConstructor(PacketPlayOutChat,
        IChatBaseComponent, ChatMessageType);
    Object finalGameInfo = gameInfo;
    actionBarSender = (p, message) -> {
      try {
        Object handle = CRAFT_PLAYER_GET_HANDLE.invoke(p);
        Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(handle);
        Object baseComponent = CHAT_SERIALIZER_A.invoke(null, "{\"text\": \"" + message + "\"}");
        Object packet = PacketPlayOutChatConstructor.newInstance(baseComponent, finalGameInfo);
        PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, packet);
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
        e.printStackTrace();
      }
    };
  }

  private static void initPre12ActionBar() {
    final Constructor<?> PacketPlayOutChatConstructor = getConstructor(PacketPlayOutChat,
        IChatBaseComponent, Byte.TYPE);
    actionBarSender = (p, message) -> {
      try {
        Object handle = CRAFT_PLAYER_GET_HANDLE.invoke(p);
        Object playerConnection = ENTITY_PLAYER_PLAYER_CONNECTION.get(handle);
        Object baseComponent = CHAT_SERIALIZER_A.invoke(null, "{\"text\": \"" + message + "\"}");
        Object packet = PacketPlayOutChatConstructor.newInstance(baseComponent, (byte) 2);
        PLAYER_CONNECTION_SEND_PACKET.invoke(playerConnection, packet);
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
        e.printStackTrace();
      }
    };
  }

  public static void sendActionBar(Player toPlayer, String withMessage) {
    actionBarSender.accept(toPlayer, withMessage);
  }

  public static Class<?> getNMS(String classPath) {
    try {
      Class<?> clazz = Class.forName("net.minecraft.server." + VERSION + "." + classPath);
      return clazz;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Class<?> getOBC(String classPath) {
    try {
      Class<?> clazz = Class.forName("org.bukkit.craftbukkit." + VERSION + "." + classPath);
      return clazz;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
    while (clazz != null) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        if (method.getName().equals(methodName) && compare(method.getParameterTypes(), params)) {
          method.setAccessible(true);
          return method;
        }
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }

  public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) {
    while (clazz != null) {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors();
      for (Constructor<?> cons : constructors) {
        if (compare(cons.getParameterTypes(), params)) {
          return cons;
        }
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }

  public static Method getMethod(Object object, String methodName, Class<?>... params) {
    return getMethod(object.getClass(), methodName, params);
  }

  public static Field getField(Class<?> clazz, String fieldName) {
    while (clazz != null) {
      Field[] fields = clazz.getDeclaredFields();
      for (Field f : fields) {
        if (f.getName().equals(fieldName)) {
          f.setAccessible(true);
          return f;
        }
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }

  public static boolean compare(Class<?>[] classes, Class<?>[] otherClasses) {
    if (classes == null || otherClasses == null) {
      return false;
    }
    if (classes.length != otherClasses.length) {
      return false;
    }
    for (int i = 0; i < classes.length; i++) {
      if (classes[i] != otherClasses[i]) {
        return false;
      }
    }

    return true;
  }

  private static Class<?>[] params(Class<?>... classes) {
    return classes;
  }

  public static class NBT {

    public static final Class<?>
        CLASS_NBT_TAG_BASE = getNMS("NBTBase"),
        CLASS_NBT_TAG_COMPOUND = getNMS("NBTTagCompound"),
        CLASS_NBT_LIST = getNMS("NBTTagList"),
        CLASS_INTEGER_ARRAY = new int[]{}.getClass(),
        CLASS_BYTE_ARRAY = new int[]{}.getClass(),
        CLASS_CRAFT_ITEM_STACK = getOBC("inventory.CraftItemStack"),
        CLASS_NMS_ITEM_STACK = getNMS("ItemStack");

    private final static Method AS_NMY_COPY_METHOD = getMethod(CLASS_CRAFT_ITEM_STACK, "asNMSCopy",
        ItemStack.class);

    public static Object toNMSItem(ItemStack item) {
      if (item == null) {
        return null;
      }
      try {
        return AS_NMY_COPY_METHOD.invoke(null, item);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Method AS_BUKKIT_COPY_METHOD = getMethod(CLASS_CRAFT_ITEM_STACK,
        "asBukkitCopy", CLASS_NMS_ITEM_STACK);

    public static ItemStack toBukkitItem(Object obcItem) {
      if (obcItem == null) {
        return null;
      }
      try {
        return (ItemStack) AS_BUKKIT_COPY_METHOD.invoke(null, obcItem);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
//			ItemStack it = CraftItemStack.asBukkitCopy(null);
    }

    private static final Method GET_TAG_METHOD = getMethod(CLASS_NMS_ITEM_STACK, "getTag");

    public static Object getNBTTag(Object nmsItem) {
      try {
        Object nbt = GET_TAG_METHOD.invoke(nmsItem);
        if (nbt == null) {
//					System.out.println("it was null");
          nbt = createNBTTag();
        }
        return nbt;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Constructor<?> NBT_TAG_COMPOUND_CONSTRUCTOR = getConstructor(
        CLASS_NBT_TAG_COMPOUND);

    public static Object createNBTTag() {
      try {
//				return CLASS_NBT_TAG_COMPOUND.newInstance();
        return NBT_TAG_COMPOUND_CONSTRUCTOR.newInstance();
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Method NMS_ITEM_FROM_NBT_METHOD = getDesiredMethod(CLASS_NMS_ITEM_STACK,
        CLASS_NMS_ITEM_STACK, params(CLASS_NBT_TAG_COMPOUND));

    public static Object nmsItemStackFromNBT(Object nbt) {
      try {
        return NMS_ITEM_FROM_NBT_METHOD.invoke(null, nbt);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Method SET_TAG_METHOD = getMethod(CLASS_NMS_ITEM_STACK, "setTag",
        CLASS_NBT_TAG_COMPOUND);

    public static void applyNBT(Object nmsItem, Object nbt) {
      try {
        SET_TAG_METHOD.invoke(nmsItem, nbt);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method HAS_KEY_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "hasKey",
        String.class);

    public static boolean hasKey(Object nbt, String key) {
      try {
        return (boolean) HAS_KEY_METHOD.invoke(nbt, key);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return false;
    }

    private static final Method SAVE_NMS_ITEM_TO_NBT = getMethod(CLASS_NMS_ITEM_STACK, "save",
        CLASS_NBT_TAG_COMPOUND);

    public static Object saveNmsItemToNBT(Object nmsItem, Object toNmsCompound) {
      try {
        return SAVE_NMS_ITEM_TO_NBT.invoke(nmsItem, toNmsCompound);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return toNmsCompound;
    }

    private static final Method SET_STRING_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setString",
        params(String.class, String.class));

    public static void setString(Object nbt, String key, String val) {
      try {
        SET_STRING_METHOD.invoke(nbt, key, val);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method SET_INT_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setInt",
        String.class, Integer.TYPE);

    public static void setInt(Object nbt, String key, int val) {
      try {
        SET_INT_METHOD.invoke(nbt, key, val);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method SET_BOOLEAN_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setBoolean",
        String.class, Boolean.TYPE);

    public static void setBoolean(Object nbt, String key, boolean val) {
      try {
        SET_BOOLEAN_METHOD.invoke(nbt, key, val);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method SET_DOUBLE_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setDouble",
        String.class, Double.TYPE);

    public static void setDouble(Object nbt, String key, double val) {
      try {
        SET_DOUBLE_METHOD.invoke(nbt, key, val);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method SET_LONG_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "setLong",
        String.class, Long.TYPE);

    public static void setLong(Object nbt, String key, long val) {
      try {
        SET_LONG_METHOD.invoke(nbt, key, val);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method GET_DOUBLE_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getDouble",
        String.class);

    public static double getDouble(Object nbt, String key) {
      try {
        return (double) GET_DOUBLE_METHOD.invoke(nbt, key);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getDouble");
    }

    private static final Method GET_INT_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getInt",
        String.class);

    public static int getInt(Object nbt, String key) {
      try {
        return (int) GET_INT_METHOD.invoke(nbt, key);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getInt");
    }

    private static final Method GET_LONG_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getLong",
        String.class);

    public static long getLong(Object nbt, String key) {
      try {
        return (long) GET_LONG_METHOD.invoke(nbt, key);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getLong");
    }

    private static final Method GET_STRING_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getString",
        String.class);

    public static String getString(Object nbt, String key) {
      try {
        return (String) GET_STRING_METHOD.invoke(nbt, key);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getString");
    }

    private static final Method GET_COMPOUND_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND,
        "getCompound", String.class);

    public static Object getCompound(Object nbt, String key) {
      try {
        return GET_COMPOUND_METHOD.invoke(nbt, key);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getCompound");
    }

    private static final Method SET_BASE_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "set",
        String.class, CLASS_NBT_TAG_BASE);

    public static void setCompound(Object nbt, String key, Object toPut) {
      try {
        SET_BASE_METHOD.invoke(nbt, key, toPut);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method GET_LIST_METHOD = getMethod(CLASS_NBT_TAG_COMPOUND, "getList",
        String.class, Integer.TYPE);

    public static Object getList(Object nbt, String key, int type) {
      try {
        return GET_LIST_METHOD.invoke(nbt, key, type);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getList");
    }

    private static final Method GET_LIST_STRING_METHOD = getMethod(CLASS_NBT_LIST, "getString",
        Integer.TYPE);

    public static String getListString(Object nbtList, int i) {
      try {
        return (String) GET_LIST_STRING_METHOD.invoke(nbtList, i);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getString");
    }

    //        private static final Method GET_LIST_INT_METHOD = getDesiredMethod(CLASS_NBT_LIST, Integer.TYPE, params(Integer.TYPE));
    public static int getListInt(Object nbtList, int i) {
//            System.out.println("NB_LS:" + nbtList + " GET_METH:" + GET_LIST_INT_METHOD);
      Object base = getListBase(nbtList, i);
      return (int) getNbtBaseData(base, NBTObject.INT);
//                return (int) GET_LIST_INT_METHOD.invoke(nbtList, i);
    }

    private static final Method GET_LIST_BASE_METHOD = getGetListBaseMethod();

    private static Method getGetListBaseMethod() {
      if (IS_1_8) {
        return getMethod(CLASS_NBT_LIST, "g", Integer.TYPE);
      } else {
        Method[] meths = CLASS_NBT_LIST.getDeclaredMethods();
        for (Method meth : meths) {
          if (meth.getReturnType() == CLASS_NBT_TAG_BASE) {
            if (compare(meth.getParameterTypes(), params(Integer.TYPE))) {
              if (!meth.getName().equals("remove")) {
                meth.setAccessible(true);
                return meth;
              }
            }
          }
        }
      }
      throw new RuntimeException("Could not find shit ;(");
    }

    public static Object getListBase(Object nbtList, int i) {
      try {
        return GET_LIST_BASE_METHOD.invoke(nbtList, i);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getBase method");
    }

    private static final Method GET_LIST_DOUBLE_METHOD = getDesiredMethod(CLASS_NBT_LIST,
        Double.TYPE, params(Integer.TYPE));

    public static double getListDouble(Object nbtList, int i) {
      try {
        return (double) GET_LIST_DOUBLE_METHOD.invoke(nbtList, i);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getDouble method");
    }

    private static final Method GET_LIST_FLOAT_METHOD = getDesiredMethod(CLASS_NBT_LIST, Float.TYPE,
        params(Integer.TYPE));

    public static float getListFloat(Object nbtList, int i) {
      try {
        return (float) GET_LIST_FLOAT_METHOD.invoke(nbtList, i);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getFloat method");
    }

    private static final Method GET_LIST_COMPOUND_METHOD = getDesiredMethod(CLASS_NBT_LIST,
        CLASS_NBT_TAG_COMPOUND, params(Integer.TYPE));

    public static Object getListCompound(Object nbtList, int i) {
      try {
        return GET_LIST_COMPOUND_METHOD.invoke(nbtList, i);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke getCompound method");
    }

    private static final Method GET_LIST_SIZE_METHOD = getMethod(CLASS_NBT_LIST, "size");

    public static int getListSize(Object nbtList) {
      try {
        return (int) GET_LIST_SIZE_METHOD.invoke(nbtList);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      throw new RuntimeException("Could not invoke size method");
    }

    private static final Method CREATE_BASE_METHOD = getMethod(CLASS_NBT_TAG_BASE, "createTag",
        Byte.TYPE);

    public static Object createNBTBase(byte type) {
      try {
        return CREATE_BASE_METHOD.invoke(null, type);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Field[] NBT_BASE_DATA_FIELDS = new Field[11];

    static {
      for (byte i = 1; i <= 8; i++) {
        Object base = createNBTBase(i);
        Class<?> clazz = base.getClass();
        try {
          Field field = clazz.getDeclaredField("data");
          field.setAccessible(true);
          NBT_BASE_DATA_FIELDS[i] = field;
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        }
      }
    }

    public static void setNBTBaseData(Object nbtBase, byte type, Object data) {
      try {
        NBT_BASE_DATA_FIELDS[type].set(nbtBase, data);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    public static Object getNbtBaseData(Object nbtBase, byte type) {
      try {
        return NBT_BASE_DATA_FIELDS[type].get(nbtBase);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Method NBT_LIST_ADD_METHOD = getMethod(CLASS_NBT_LIST, "add",
        CLASS_NBT_TAG_BASE);

    public static void addListElement(Object nbtList, Object nbtBase) {
      try {
        NBT_LIST_ADD_METHOD.invoke(nbtList, nbtBase);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static Method getDesiredMethod(Class<?> clazz, Class<?> returnType,
        Class<?>[] parameters) {
      Method[] meths = clazz.getDeclaredMethods();
      for (Method meth : meths) {
        if (meth.getReturnType() == returnType) {
//                    System.out.println("FOUND_RET_TYPE:" + meth.getReturnType());
//                    System.out.println("COMPARING PARAMS:" + Arrays.asList(meth.getParameterTypes()) + " WTIH:" + Arrays.asList(parameters) + " SAYING:" + compare(meth.getParameterTypes(), parameters));
          if (compare(meth.getParameterTypes(), parameters)) {
//                        System.out.println("HELL YES: Returning:" + meth);
            return meth;
          }
        }
      }
      return null;
    }

    private static final Constructor<?> NBT_LIST_CONSTRUCTOR = getConstructor(CLASS_NBT_LIST);

    public static Object createNBTList() {
      try {
        return NBT_LIST_CONSTRUCTOR.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    private static final Class<?> CLASS_NBT_COMPRESSED_STREAM_TOOLS = getNMS(
        "NBTCompressedStreamTools");
    private static final Method WRITE_TO_STREAM = getMethod(CLASS_NBT_COMPRESSED_STREAM_TOOLS, "a",
        CLASS_NBT_TAG_COMPOUND, OutputStream.class);

    public static void writeToStream(Object nbtCompound, OutputStream output) throws IOException {
      System.out.println("THE_METHOD:" + WRITE_TO_STREAM);
      System.out.println("THE_CLASS:" + CLASS_NBT_COMPRESSED_STREAM_TOOLS);
      try {
        WRITE_TO_STREAM.invoke(null, nbtCompound, output);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    private static final Method READ_FROM_STREAM_METHOD = getMethod(
        CLASS_NBT_COMPRESSED_STREAM_TOOLS, "a", InputStream.class);

    public static Object readFromStream(InputStream input) throws IOException {
      try {
        return READ_FROM_STREAM_METHOD.invoke(null, input);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

  }

  public static class Block {

    private static final Class<?> CLASS_CRAFT_CHEST = getOBC("block.CraftChest"),
        CLASS_TILE_ENTITY_CHEST = getNMS("TileEntityChest");
    private static final Method CRAFT_CHEST_GET_TILE_ENTITY = getMethod(CLASS_CRAFT_CHEST,
        "getTileEntity"),
        TILE_ENTITY_CHEST_SET_NAME =
            getMethod(CLASS_TILE_ENTITY_CHEST, "a", params(String.class)) != null ?
                getMethod(CLASS_TILE_ENTITY_CHEST, "a", params(String.class)) :
                getMethod(CLASS_TILE_ENTITY_CHEST, "setCustomName", params(String.class));

    public static void setChestName(Location loc, String newName) {
      setChestName(loc.getBlock(), newName);
    }

    public static void setChestName(org.bukkit.block.Block bl, String newName) {
//			if (TILE_ENTITY_CHEST_SET_NAME == null) {
//				Util.log("IT IS NULL AMK!");
//			}
      if (bl.getType() == Material.CHEST) {
        BlockState state = bl.getState();
        try {
          Object tile = CRAFT_CHEST_GET_TILE_ENTITY.invoke(state);
//					System.out.println("THE TILE:" + tile);
//					System.out.println("THE METHOD:" + TILE_ENTITY_CHEST_SET_NAME);
          TILE_ENTITY_CHEST_SET_NAME.invoke(tile, newName);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }

  }

  public static class EnderEye {

    private final static Class<?> CLASS_ENDER_SIGNAL = getNMS("EntityEnderSignal"),
        CLASS_BLOCK_POSITION = getNMS("BlockPosition"),
        CLASS_CRAFT_ENDER_SIGNAL = getOBC("entity.CraftEnderSignal");
    private final static Constructor<?> CONSTRUCTOR_BLOCK_POSITION = getConstructor(
        CLASS_BLOCK_POSITION, params(Integer.TYPE, Integer.TYPE, Integer.TYPE));
    private final static Method METHOD_ENDER_SIGNAL_SET_TARGET = getMethod(CLASS_ENDER_SIGNAL, "a",
        params(CLASS_BLOCK_POSITION)),
        METHOD_CRAFT_ENDER_SIGNAL_HANDLE = getMethod(CLASS_CRAFT_ENDER_SIGNAL, "getHandle");
    private final static Field FIELD_ENDER_SIGNAL_NO_DROP =
        getField(CLASS_ENDER_SIGNAL, "e") != null ? getField(CLASS_ENDER_SIGNAL, "e") :
            getField(CLASS_ENDER_SIGNAL, "shouldDropItem");

    public static Object getEnderSignalHandle(EnderSignal signal) {
      try {
        return METHOD_CRAFT_ENDER_SIGNAL_HANDLE.invoke(signal);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    }

    public static void setEnderSignalTarget(Object handle, int x, int y, int z) {
      try {
        Object blockPos = CONSTRUCTOR_BLOCK_POSITION.newInstance(x, y, z);
        METHOD_ENDER_SIGNAL_SET_TARGET.invoke(handle, blockPos);
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }

    public static void setEnderSignalDrop(Object handle, boolean shouldItemDrop) {
      try {
        FIELD_ENDER_SIGNAL_NO_DROP.set(handle, shouldItemDrop);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

}
