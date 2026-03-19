package com.allaymc.practice.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {

    private final Map<String, Arena> arenas = new HashMap<>();

    public void load(FileConfiguration config) {
        arenas.clear();

        ConfigurationSection section = config.getConfigurationSection("arenas");
        if (section == null) return;

        for (String arenaName : section.getKeys(false)) {
            String path = "arenas." + arenaName;
            String worldName = config.getString(path + ".world");
            String kit = config.getString(path + ".kit");

            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            Location spawn1 = new Location(
                    world,
                    config.getDouble(path + ".spawn1.x"),
                    config.getDouble(path + ".spawn1.y"),
                    config.getDouble(path + ".spawn1.z"),
                    (float) config.getDouble(path + ".spawn1.yaw"),
                    (float) config.getDouble(path + ".spawn1.pitch")
            );

            Location spawn2 = new Location(
                    world,
                    config.getDouble(path + ".spawn2.x"),
                    config.getDouble(path + ".spawn2.y"),
                    config.getDouble(path + ".spawn2.z"),
                    (float) config.getDouble(path + ".spawn2.yaw"),
                    (float) config.getDouble(path + ".spawn2.pitch")
            );

            arenas.put(arenaName.toLowerCase(), new Arena(arenaName, kit, spawn1, spawn2));
        }
    }

    public Arena findFreeArena(String kitName) {
        for (Arena arena : arenas.values()) {
            if (arena.getState() == ArenaState.FREE && arena.getKitName().equalsIgnoreCase(kitName)) {
                return arena;
            }
        }
        return null;
    }
}
