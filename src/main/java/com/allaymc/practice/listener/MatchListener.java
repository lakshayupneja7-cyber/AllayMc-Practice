package com.allaymc.practice.listener;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.duel.Match;
import com.allaymc.practice.duel.MatchState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class MatchListener implements Listener {

    private final AllayMcPractice plugin;

    public MatchListener(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        Match victimMatch = plugin.getMatchManager().getMatch(victim.getUniqueId());
        Match attackerMatch = plugin.getMatchManager().getMatch(attacker.getUniqueId());

        if (victimMatch == null || attackerMatch == null || victimMatch != attackerMatch) {
            return;
        }

        if (victimMatch.getState() != MatchState.FIGHTING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Match match = plugin.getMatchManager().getMatch(player.getUniqueId());
        if (match == null) return;

        event.setDeathMessage(null);

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            plugin.getMatchManager().endMatch(player.getUniqueId());
            player.spigot().respawn();
        });
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Match match = plugin.getMatchManager().getMatch(player.getUniqueId());
        if (match == null) {
            event.setCancelled(true);
        }
    }
}
