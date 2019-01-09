package me.mylogo.divineitems.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dennis Heckmann on 02.07.17
 * Copyright (c) 2017 Dennis Heckmann
 */
public class ItemBuilder {

    private List<String> lore;
    private ItemMeta meta;
    private ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material mat, int amount, short damage) {
        this(new ItemStack(mat, amount, damage));
    }

    public ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short) 0);
    }

    public ItemBuilder(Material mat) {
        this(mat, 1);
    }

    public ItemBuilder setName(String name) {
        if (meta != null)
            meta.setDisplayName(name);
        return this;
    }

    public String getName() {
        return meta != null ? meta.getDisplayName() : null;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public int getAmount() {
        return this.item.getAmount();
    }

//    public int getDamage() {
//        return this.item.getData().getData();
//    }

    public ItemStack build() {
        if (this.meta != null) {
            if (this.lore != null) {
                this.meta.setLore(this.lore);
            }
            this.item.setItemMeta(this.meta);
        }
        return this.item;
    }

    public ItemStack buildCloned() {
        return this.build().clone();
    }

    public ItemBuilder setLore(String ... lore) {
        return setLore(new ArrayList<>(Arrays.asList(lore)));
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = new ArrayList<>(lore);
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(List<String> lore) {
        if (this.lore == null) {
            if (!meta.hasLore())
                this.lore = new ArrayList<>();
            else
                this.lore = new ArrayList<>(meta.getLore());
        }
        this.lore.addAll(lore);
        return this;
    }

    public ItemBuilder setData(short data) {
        item.setDurability(data);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        meta.addEnchant(ench, level, false);
        return this;
    }

}
