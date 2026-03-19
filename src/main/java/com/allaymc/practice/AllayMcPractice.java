package com.allaymc.practice;

import com.allaymc.practice.arena.ArenaManager;
import com.allaymc.practice.command.AcceptCommand;
import com.allaymc.practice.command.DuelCommand;
import com.allaymc.practice.command.SpawnCommand;
import com.allaymc.practice.duel.MatchManager;
import com.allaymc.practice.kit.KitManager;
import com.allaymc.practice.listener.MatchListener;
import com.allaymc.practice.listener.PlayerConnectionListener;
import com.allaymc.practice.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class AllayMcPractice extends JavaPlugin {

    private PlayerManager playerManager;
    private KitManager kitManager;
    private ArenaManager arenaManager;
    private MatchManager matchManager;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResourceIfNotExists("kits.yml");
        saveResourceIfNotExists("arenas.yml");

        this.playerManager = new PlayerManager();
        this.kitManager = new KitManager();
        this.arenaManager = new ArenaManager();
        this.matchManager = new MatchManager(this);

        loadSpawn();
        loadFiles();
        registerCommands();
        registerListeners();

        getLogger().info("AllayMc-Practice enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("AllayMc-Practice disabled.");
    }

    private void loadSpawn() {
        String worldName = getConfig().getString("spawn.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            world = Bukkit.getWorlds().getFirst();
        }

        this.spawnLocation = new Location(
                world,
                getConfig().getDouble("spawn.x"),
                getConfig().getDouble("spawn.y"),
                getConfig().getDouble("spawn.z"),
                (float) getConfig().getDouble("spawn.yaw"),
                (float) getConfig().getDouble("spawn.pitch")
        );
    }

    private void loadFiles() {
        File kitsFile = new File(getDataFolder(), "kits.yml");
        File arenasFile = new File(getDataFolder(), "arenas.yml");

        kitManager.load(YamlConfiguration.loadConfiguration(kitsFile));
        arenaManager.load(YamlConfiguration.loadConfiguration(arenasFile));
    }

    private void registerCommands() {
        getCommand("duel").setExecutor(new DuelCommand(this));
        getCommand("accept").setExecutor(new AcceptCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new MatchListener(this), this);
    }

    private void saveResourceIfNotExists(String name) {
        File file = new File(getDataFolder(), name);
        if (!file.exists()) {
            saveResource(name, false);
        }
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public MatchManager getMatchManager() {
        return matchManager;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
