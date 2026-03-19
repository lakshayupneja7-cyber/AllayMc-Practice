package com.allaymc.practice.command;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.arena.Arena;
import com.allaymc.practice.duel.DuelRequest;
import com.allaymc.practice.player.PlayerState;
import com.allaymc.practice.player.PracticePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {

    private final AllayMcPractice plugin;

    public AcceptCommand(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) return true;

        if (args.length < 1) {
            target.sendMessage("§cUsage: /accept <player>");
            return true;
        }

        DuelRequest request = plugin.getMatchManager().getRequest(target.getUniqueId());
        if (request == null) {
            target.sendMessage("§cYou have no active duel request.");
            return true;
        }

        Player senderPlayer = Bukkit.getPlayer(request.getSender());
        if (senderPlayer == null || !senderPlayer.isOnline()) {
            target.sendMessage("§cThe requesting player is offline.");
            plugin.getMatchManager().removeRequest(target.getUniqueId());
            return true;
        }

        if (!senderPlayer.getName().equalsIgnoreCase(args[0])) {
            target.sendMessage("§cThat player did not send your active request.");
            return true;
        }

        PracticePlayer senderProfile = plugin.getPlayerManager().getOrCreate(senderPlayer.getUniqueId());
        PracticePlayer targetProfile = plugin.getPlayerManager().getOrCreate(target.getUniqueId());

        if (senderProfile.getState() != PlayerState.LOBBY || targetProfile.getState() != PlayerState.LOBBY) {
            target.sendMessage("§cOne of the players is no longer available.");
            plugin.getMatchManager().removeRequest(target.getUniqueId());
            return true;
        }

        Arena arena = plugin.getArenaManager().findFreeArena(request.getKitName());
        if (arena == null) {
            target.sendMessage("§cNo free arena found for that kit.");
            senderPlayer.sendMessage("§cNo free arena found for that kit.");
            return true;
        }

        plugin.getMatchManager().removeRequest(target.getUniqueId());
        plugin.getMatchManager().startMatch(senderPlayer, target, request.getKitName(), arena);

        return true;
    }
}
