package com.allaymc.practice.command;

import com.allaymc.practice.AllayMcPractice;
import com.allaymc.practice.player.PlayerState;
import com.allaymc.practice.player.PracticePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final AllayMcPractice plugin;

    public SpawnCommand(AllayMcPractice plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        PracticePlayer profile = plugin.getPlayerManager().getOrCreate(player.getUniqueId());
        if (profile.getState() == PlayerState.IN_DUEL) {
            player.sendMessage("§cYou cannot use /spawn during a duel.");
            return true;
        }

        plugin.getMatchManager().resetToSpawn(player);
        profile.setState(PlayerState.LOBBY);
        player.sendMessage("§aTeleported to spawn.");
        return true;
    }
}


