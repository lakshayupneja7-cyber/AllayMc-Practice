package com.allaymc.practice.command;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.player.PlayerState;
import com.allaymc.practice.player.PracticePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    private final AllayMcPractice plugin;

    public DuelCommand(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length < 2) {
            player.sendMessage("§cUsage: /duel <player> <kit>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        String kitName = args[1];

        if (target == null || !target.isOnline()) {
            player.sendMessage("§cPlayer not found.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage("§cYou cannot duel yourself.");
            return true;
        }

        if (plugin.getKitManager().getKit(kitName) == null) {
            player.sendMessage("§cThat kit does not exist.");
            return true;
        }

        PracticePlayer senderProfile = plugin.getPlayerManager().getOrCreate(player.getUniqueId());
        PracticePlayer targetProfile = plugin.getPlayerManager().getOrCreate(target.getUniqueId());

        if (senderProfile.getState() != PlayerState.LOBBY || targetProfile.getState() != PlayerState.LOBBY) {
            player.sendMessage("§cBoth players must be in lobby state.");
            return true;
        }

        plugin.getMatchManager().sendRequest(player, target, kitName);

        player.sendMessage("§aDuel request sent to " + target.getName() + " using kit §e" + kitName + "§a.");
        target.sendMessage("§e" + player.getName() + " §ahas challenged you with kit §e" + kitName + "§a.");
        target.sendMessage("§7Use §f/accept " + player.getName() + " §7to accept.");

        return true;
    }
}
