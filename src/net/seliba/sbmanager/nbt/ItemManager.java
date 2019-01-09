package me.mylogo.divineitems.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Dennis Heckmann on 01.07.17
 * Copyright (c) 2017 Dennis Heckmann
 */
public class ItemManager {

    public static final String XML = "xml", YAML = "yml", JSON = "json";

    private DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private DocumentBuilder documentBuilder;
    private Transformer transformer;
    private Map<String, Material> materials;
    private Map<String, Enchantment> enchantments;
    private List<MyItem> items;
    private JavaPlugin plugin;

    public ItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.items = new ArrayList<>();
        materials = new HashMap<>();
        for (Material material : Material.values()) {
            materials.put(convert(material.name()), material);
        }
        enchantments = new HashMap<>();
        for (Enchantment ench : Enchantment.values()) {
            enchantments.put(convert(ench.getName()), ench);
        }
        enchantments.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantments.put("unbreaking", Enchantment.DURABILITY);
        enchantments.put("efficiency", Enchantment.DIG_SPEED);
        enchantments.put("flame", Enchantment.ARROW_FIRE);
        enchantments.put("infinity", Enchantment.ARROW_INFINITE);
        enchantments.put("power", Enchantment.ARROW_DAMAGE);
        enchantments.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantments.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantments.put("lure", Enchantment.LOOT_BONUS_MOBS);
//        enchantments.put("fire")
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not instantiate documentBuilder!");
        }
    }

    public ItemStack getItem(String withId) {
        MyItem myItem1 = this.items.stream().filter(myItem -> myItem.getId().equals(withId)).findFirst().orElse(null);
        if (myItem1 != null)
            return myItem1.getItem();
        return null;
    }

    public void writeItemsIntoYAML(File file, List<MyItem> items) {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        items.forEach(myItem -> conf.set(myItem.getId(), myItem.getId()));
        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerItems(File fromFile) {
        try {
            List<MyItem> myItems = readItems(fromFile);
            Iterator<MyItem> iterator = myItems.iterator();
            while (iterator.hasNext()) {
                MyItem next = iterator.next();
                if (containsId(this.items, next.getId())) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An item with the id '" + next.getId() + "' already exists!");
                    iterator.remove();
                }
            }
            this.items.addAll(myItems);
        } catch (ItemParseException e) {
            System.err.println("Could not properly parse the file:" + fromFile);
            e.printStackTrace();
        }
    }

    private boolean containsId(List<MyItem> myItems, String id) {
        return myItems.stream().map(MyItem::getId).filter(theId -> theId.equals(id)).findFirst().orElse(null) != null;
    }

    public List<MyItem> readItems(File file) throws ItemParseException {
        List<MyItem> readItems = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File inDir : files) {
                    try {
                        List<MyItem> newMyItems = readItems(inDir);
                        Iterator<MyItem> iterator = newMyItems.iterator();
                        while (iterator.hasNext()) {
                            MyItem next = iterator.next();
                            if (containsId(readItems, next.getId())) {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An item with the id '" + next.getId() + "' already exists!");
                                iterator.remove();
                            }
                        }
                        readItems.addAll(newMyItems);
                    } catch (ItemParseException e) {
                        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Something went wrong while parsing " + file + ":" +
                                e.getMessage()
                        );
                    }
                }
            }
        } else {
            String extension = getExtension(file.getName());
            if (XML.equalsIgnoreCase(extension)) {
                readItems.addAll(readFromXML(file));
            } else if (YAML.equalsIgnoreCase(extension)) {
                readItems.addAll(readFromYAML(file));
            } else if (JSON.equalsIgnoreCase(extension)) {

            } else {
                throw new ItemParseException("The file " + file.toString() + " could not be read! Its file type is not supported.");
            }
        }
        return readItems;
    }

    private List<? extends MyItem> readFromYAML(File file) throws ItemParseException {
        List<MyItem> myItems = new ArrayList<>();
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
        if (!conf.isSet("items"))
            throw new ItemParseException("The file " + file.toString() + " does not contain any items!");
        ConfigurationSection section = conf.getConfigurationSection("items");
        Set<String> keys = section.getKeys(false);
        for (String key : keys) {
            Object itemObject = section.get(key);
            if (itemObject instanceof ItemStack) {
                myItems.add(new MyItem(key, (ItemStack) itemObject));
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Inside of " + file.toString() + ", '" + key + "' isn't an item!");
            }
        }
        return myItems;
    }

    public List<MyItem> readFromXML(File file) throws ItemParseException {
        try {
            return readFromXML(documentBuilder.parse(file));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            throw new ItemParseException("Could not parse the file " + file);
        }
    }

    public List<MyItem> readFromXML(InputStream input) throws ItemParseException {
        try {
            return readFromXML(documentBuilder.parse(input));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            throw new ItemParseException("Could not parse the input!");
        }
    }

    private List<MyItem> readFromXML(Document document) throws ItemParseException {
        Element root = document.getDocumentElement();
        if (root == null)
            throw new ItemParseException("The document does not have a root!");
        NodeList children = root.getElementsByTagName("item");
        List<MyItem> items = new ArrayList<>();
        int length = children.getLength();

        // Loop through all elements to start actual parsing
        for (int i = 0; i < length; i++) {
            try {
                Node node = children.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    throw new ItemParseException("The node is not an element!");
                MyItem myItem = readMyItemFromXMLElement((Element) node);
                if (!containsId(items, myItem.getId())) {
                    items.add(myItem);
                } else {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "An item with the id '" + myItem.getId() + "' already exists!");
                }
            } catch (ItemParseException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "There's an error in " + document.getNamespaceURI() +
                        " while reading item number " + (i + 1) + ":" + e.getMessage());
            }
        }
        return items;
    }

    public MyItem readMyItemFromXMLElement(Element element) throws ItemParseException {
        if (!element.hasAttribute("id"))
            throw new ItemParseException("The element did not define an id attribute! Fix it by having the first element being something like this: <item id=\"My ID\">");
        String id = element.getAttribute("id");
        if (id.isEmpty())
            throw new ItemParseException("The element's id is empty!");
        return new MyItem(id, readItemFromXMLElement(element));
    }

    public ItemStack readItemFromXMLElement(Element element) throws ItemParseException {
        // Getting material
        Element materialElement = getElement(element, "type", "material", "mat");
        String materialContent = materialElement.getTextContent();
        ensureContent(materialContent, "There was no value entered for the material!");
        Material mat = getMaterial(materialContent);
        if (mat == null)
            throw new ItemParseException("There was no material found with the name:" + materialContent);

        // Getting optional data
        short damage;
        try {
            Element dataElement = getElement(element, "data");
            String textContent = dataElement.getTextContent();
            try {
                damage = ensureShort(textContent, "");
            } catch (ItemParseException e) {
                throw new ItemParseException("Your entered data '" + textContent + "' was not a number!");
            }
        } catch (ItemParseException e) {
            damage = 0;
        }

        // Doing the optional amount
        int amount;
        try {
            Element amountElement = getElement(element, "amount", "times", "count");
            String textContent = amountElement.getTextContent();
            try {
                amount = ensureInteger(textContent, "");
            } catch (ItemParseException e) {
                throw new ItemParseException("Your entered amount '" + textContent + "' was not a number!");
            }
        } catch (ItemParseException e) {
            amount = 1;
        }

        // Doing the optional name
        String name;
        try {
            Element nameElement = getElement(element, "name", "title");
            name = nameElement.getTextContent();
        } catch (ItemParseException e) {
            name = null;
        }

        List<String> lore;
        try {
            Element loreElement = getElement(element, "lore");
            NodeList nodeLines = loreElement.getElementsByTagName("line");
            if (nodeLines.getLength() > 0) {
                lore = new ArrayList<>();
                for (int i = 0; i < nodeLines.getLength(); i++) {
                    lore.add(nodeLines.item(i).getTextContent());
                }
            } else {
                lore = null;
            }
        } catch (ItemParseException e) {
            lore = null;
        }

        // Doing enchantments
        Map<Enchantment, Integer> enchantments;
        Element enchantmentsElement;
        try {
            enchantmentsElement = getElement(element, "enchantments", "enchants");
            NodeList actualEnchantments = enchantmentsElement.getChildNodes();
            if (actualEnchantments.getLength() > 0) {
                enchantments = new HashMap<>();
                Node enchantmentNode = enchantmentsElement.getFirstChild();
                for (; enchantmentNode.getNextSibling() != null; enchantmentNode = enchantmentNode.getNextSibling()) {
//                    Node enchantmentNode = actualEnchantments.item(i);
                    String nodeName = enchantmentNode.getNodeName();
                    if ("enchantment".equalsIgnoreCase(nodeName)) {
                        // conservative way
                        if (enchantmentNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element enchantmentElement = (Element) enchantmentNode;
                            String enchName = enchantmentElement.getTextContent();
                            Enchantment ench = this.enchantments.get(convert(enchName));
//                            System.out.println("CONVERTED:" + convert(enchName));
                            if (ench != null) {
                                int level;
                                if (enchantmentElement.hasAttribute("level")) {
                                    try {
                                        level = ensureInteger(enchantmentElement.getAttribute("level"), "");
                                    } catch (ItemParseException e) {
                                        level = 1;
                                    }
                                    enchantments.put(ench, level);
                                } else {
                                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "The enchantment '" + enchName + "' does not have a level defined!");
                                }
                            } else {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Having the enchantment name '" + enchName + "' which does not exist!");
                            }
                        }
                    } else {
                        // new, cooler way
                        Enchantment enchantment = this.enchantments.get(convert(nodeName));
                        if (enchantment != null) {
                            int level;
                            try {
                                level = ensureInteger(enchantmentNode.getTextContent(), "");
                            } catch (ItemParseException e) {
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Having the enchantment level '" +
                                        enchantmentNode.getTextContent() + " which isn't an actual number! Will set it to 1 :)");
                                level = 1;
                            }
                            enchantments.put(enchantment, level);
                        } else {
                            if (enchantmentNode.getNodeType() == Node.ELEMENT_NODE)
                                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Having the enchantment name '" + nodeName + "', but this doesn't exist!");
                        }
                    }
                }
            } else {
                enchantments = null;
            }
        } catch (ItemParseException e) {
            enchantments = null;
        }

        ItemBuilder builder = new ItemBuilder(mat, amount, damage);
        if (name != null)
            builder.setName(name);
        if (lore != null)
            builder.setLore(lore);
        ItemStack item = builder.build();
        if (enchantments != null) {
            item.addUnsafeEnchantments(enchantments);
        }
        return item;
    }

    private short ensureShort(String ofContent, String excepteionMessage) throws ItemParseException {
        try {
            return Short.parseShort(ofContent);
        } catch (Exception e) {
            throw new ItemParseException(excepteionMessage);
        }
    }

    private double ensureDouble(String ofContent, String excepteionMessage) throws ItemParseException {
        try {
            return Double.parseDouble(ofContent.replace(',', '.'));
        } catch (Exception e) {
            throw new ItemParseException(excepteionMessage);
        }
    }

    private int ensureInteger(String ofContent, String excepteionMessage) throws ItemParseException {
        try {
            return Integer.parseInt(ofContent);
        } catch (Exception e) {
            throw new ItemParseException(excepteionMessage);
        }
    }

    private void ensureContent(String ofContent, String exceptionMessage) throws ItemParseException {
        if (ofContent == null || ofContent.isEmpty())
            throw new ItemParseException(exceptionMessage);
    }

    private Element getElement(Element element, String... tagNames) throws ItemParseException {
        assert tagNames.length > 0;
        for (int i = 0; i < tagNames.length; i++) {
            String tagName = tagNames[i];
            NodeList nodes = element.getElementsByTagName(tagName);
            if (nodes.getLength() > 0) {
                Node node = nodes.item(0);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    throw new ItemParseException(tagName + " is not an element!");
                return (Element) node;
            }
        }
        throw new ItemParseException("The element did not define a tag of the name " + tagNames[0]);
    }

    public Material getMaterial(String byName) {
        return materials.get(convert(byName));
    }

    public void registerItem(String id, ItemStack item) {
        item = item.clone();
        item.setAmount(1);
        items.add(new MyItem(id, item));
    }

    public static class ItemParseException extends Exception {
        public ItemParseException(String message) {
            super(message);
        }
    }

    private String convert(String name) {
        return name.replace("_", "").toLowerCase();
    }

    public static String niceName(String fromName) {
        StringBuilder sb = new StringBuilder();
        boolean toUpper = true;
        char[] chars = fromName.toCharArray();
        for (char aChar : chars) {
            if (aChar == '_') {
                toUpper = true;
            } else {
                if (toUpper) {
                    aChar = Character.toUpperCase(aChar);
                    toUpper = false;
                } else {
                    aChar = Character.toLowerCase(aChar);
                }
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    public static String getExtension(String name) {
        int lastIndex = -1;
        char[] chars = name.toCharArray();
        for (int i = name.length() - 1; i > -1; i--) {
            if (chars[i] == '.') {
                lastIndex = i;
                break;
            }
        }
        if (lastIndex > 0) {
            return name.substring(lastIndex + 1, name.length());
        }
        return "";
    }

    public class MyItem {
        private String id;
        private ItemStack item;

        public MyItem(String id, ItemStack item) {
            this.id = id;
            this.item = item.clone();
        }

        private void setAmount(int amount) {
            this.item.setAmount(amount);
        }

        public String getId() {
            return id;
        }

        public ItemStack getItem() {
            return item.clone();
        }

        @Override
        public String toString() {
            return "MyItem{" +
                    "id='" + id + '\'' +
                    ", item=" + item +
                    '}';
        }

    }

}
