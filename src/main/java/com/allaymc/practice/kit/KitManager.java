package com.allaymc.practice.kit;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private final Map<String, Kit> kits = new HashMap<>();

    public void load(FileConfiguration config) {
        kits.clear();

        ConfigurationSection section = config.getConfigurationSection("kits");
        if (section == null) return;

        for (String kitName : section.getKeys(false)) {
            String path = "kits." + kitName;

            ItemStack[] inventory = new ItemStack[36];
            ItemStack[] armor = new ItemStack[4];

            ConfigurationSection invSection = config.getConfigurationSection(path + ".inventory");
            if (invSection != null) {
                for (String slotKey : invSection.getKeys(false)) {
                    int slot = Integer.parseInt(slotKey);
                    String materialName = config.getString(path + ".inventory." + slotKey);
                    Material material = Material.matchMaterial(materialName);
                    if (material != null && slot >= 0 && slot < inventory.length) {
                        inventory[slot] = new ItemStack(material);
                    }
                }
            }

            ConfigurationSection armorSection = config.getConfigurationSection(path + ".armor");
            if (armorSection != null) {
                armor[0] = getItem(config.getString(path + ".armor.boots"));
                armor[1] = getItem(config.getString(path + ".armor.leggings"));
                armor[2] = getItem(config.getString(path + ".armor.chestplate"));
                armor[3] = getItem(config.getString(path + ".armor.helmet"));
            }

            ItemStack offHand = getItem(config.getString(path + ".offhand"));

            kits.put(kitName.toLowerCase(), new Kit(kitName, inventory, armor, offHand));
        }
    }

    private ItemStack getItem(String materialName) {
        if (materialName == null || materialName.isEmpty()) return null;
        Material material = Material.matchMaterial(materialName);
        return material == null ? null : new ItemStack(material);
    }

    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
}
