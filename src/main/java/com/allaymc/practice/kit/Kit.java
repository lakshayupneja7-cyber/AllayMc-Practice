package com.allaymc.practice.kit;

import org.bukkit.inventory.ItemStack;

public class Kit {

    private final String name;
    private final ItemStack[] inventoryContents;
    private final ItemStack[] armorContents;
    private final ItemStack offHand;

    public Kit(String name, ItemStack[] inventoryContents, ItemStack[] armorContents, ItemStack offHand) {
        this.name = name;
        this.inventoryContents = inventoryContents;
        this.armorContents = armorContents;
        this.offHand = offHand;
    }

    public String getName() {
        return name;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public ItemStack getOffHand() {
        return offHand;
    }
}
