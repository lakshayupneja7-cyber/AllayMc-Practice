package com.allaymc.practice.listener;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.player.PlayerState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final AllayMcPractice plugin;

    public PlayerConnectionListener(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var profile = plugin.getPlayerManager().getOrCreate(event.getPlayer().getUniqueId());
        profile.setState(PlayerState.LOBBY);
        plugin.getMatchManager().resetToSpawn(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getMatchManager().handleQuit(event.getPlayer());
        plugin.getPlayerManager().remove(event.getPlayer().getUniqueId());
    }
}
