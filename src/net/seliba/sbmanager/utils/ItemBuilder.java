package net.seliba.sbmanager.utils;

/*
SchematicBrushManager created by Seliba
*/

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

  private ItemStack itemStack;
  private ItemMeta itemMeta;

  public ItemBuilder(Material material) {
    itemStack = new ItemStack(material);
    itemMeta = itemStack.getItemMeta();
  }

  public ItemBuilder setName(String name) {
    itemMeta.setDisplayName(name);
    return this;
  }

  public ItemBuilder setAmount(int amount) {
    itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
    itemStack.addEnchantment(enchantment, level);
    return this;
  }

  public ItemBuilder addItemFlag(ItemFlag itemFlag) {
    itemMeta.addItemFlags(itemFlag);
    return this;
  }

  public ItemBuilder setUnbreakable() {
    itemMeta.setUnbreakable(true);
    return this;
  }

  public ItemBuilder setLore(String[] lore) {
    itemMeta.setLore(Arrays.asList(lore));
    return this;
  }

  public ItemStack build() {
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

}
