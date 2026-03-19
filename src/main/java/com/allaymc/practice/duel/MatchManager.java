package com.allaymc.practice.duel;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.arena.Arena;
import com.allaymc.practice.arena.ArenaState;
import com.allaymc.practice.kit.Kit;
import com.allaymc.practice.player.PlayerState;
import com.allaymc.practice.player.PracticePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MatchManager {

    private final AllayMcPractice plugin;
    private final Map<UUID, DuelRequest> incomingRequests = new HashMap<>();
    private final Map<UUID, Match> activeMatches = new HashMap<>();

    public MatchManager(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    public void sendRequest(Player sender, Player target, String kitName) {
        incomingRequests.put(target.getUniqueId(), new DuelRequest(sender.getUniqueId(), target.getUniqueId(), kitName));
    }

    public DuelRequest getRequest(UUID target) {
        DuelRequest request = incomingRequests.get(target);
        if (request != null && request.isExpired()) {
            incomingRequests.remove(target);
            return null;
        }
        return request;
    }

    public void removeRequest(UUID target) {
        incomingRequests.remove(target);
    }

    public Match getMatch(UUID uuid) {
        return activeMatches.get(uuid);
    }

    public boolean isInMatch(UUID uuid) {
        return activeMatches.containsKey(uuid);
    }

    public void startMatch(Player p1, Player p2, String kitName, Arena arena) {
        Kit kit = plugin.getKitManager().getKit(kitName);
        if (kit == null) {
            p1.sendMessage("§cKit not found.");
            p2.sendMessage("§cKit not found.");
            return;
        }

        arena.setState(ArenaState.IN_USE);

        Match match = new Match(p1.getUniqueId(), p2.getUniqueId(), kitName, arena);
        activeMatches.put(p1.getUniqueId(), match);
        activeMatches.put(p2.getUniqueId(), match);

        PracticePlayer pp1 = plugin.getPlayerManager().getOrCreate(p1.getUniqueId());
        PracticePlayer pp2 = plugin.getPlayerManager().getOrCreate(p2.getUniqueId());
        pp1.setState(PlayerState.IN_DUEL);
        pp2.setState(PlayerState.IN_DUEL);

        preparePlayer(p1, kit, arena.getSpawn1());
        preparePlayer(p2, kit, arena.getSpawn2());

        new BukkitRunnable() {
            int countdown = 3;

            @Override
            public void run() {
                if (!p1.isOnline() || !p2.isOnline()) {
                    cancel();
                    return;
                }

                if (countdown > 0) {
                    p1.sendTitle("§e" + countdown, "", 0, 20, 0);
                    p2.sendTitle("§e" + countdown, "", 0, 20, 0);
                    countdown--;
                    return;
                }

                match.setState(MatchState.FIGHTING);
                p1.sendTitle("§aFight!", "", 0, 20, 10);
                p2.sendTitle("§aFight!", "", 0, 20, 10);
                cancel();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void preparePlayer(Player player, Kit kit, org.bukkit.Location location) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().setContents(kit.getInventoryContents());
        player.getInventory().setArmorContents(kit.getArmorContents());
        player.getInventory().setItemInOffHand(kit.getOffHand());

        player.teleport(location);
    }

    public void endMatch(UUID loserUuid) {
        Match match = activeMatches.get(loserUuid);
        if (match == null) return;

        match.setState(MatchState.ENDING);

        UUID winnerUuid = match.getOpponent(loserUuid);

        Player loser = Bukkit.getPlayer(loserUuid);
        Player winner = Bukkit.getPlayer(winnerUuid);

        if (winner != null) {
            winner.sendMessage("§aYou won the duel.");
            resetToSpawn(winner);
        }

        if (loser != null) {
            loser.sendMessage("§cYou lost the duel.");
            resetToSpawn(loser);
        }

        plugin.getPlayerManager().getOrCreate(match.getPlayer1()).setState(PlayerState.LOBBY);
        plugin.getPlayerManager().getOrCreate(match.getPlayer2()).setState(PlayerState.LOBBY);

        match.getArena().setState(ArenaState.FREE);

        activeMatches.remove(match.getPlayer1());
        activeMatches.remove(match.getPlayer2());
    }

    public void handleQuit(Player quitter) {
        Match match = getMatch(quitter.getUniqueId());
        if (match != null) {
            endMatch(quitter.getUniqueId());
        }
    }

    public void resetToSpawn(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItemInOffHand(null);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.teleport(plugin.getSpawnLocation());
    }
}
