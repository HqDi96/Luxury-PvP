package core.luxury.commands;

import core.luxury.PvPPlugin;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp()) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        Player player = (Player) sender;

        try {
            PvPPlugin.getInstance().reloadConfigs();
            player.sendMessage(PvPPlugin.prefix + ChatColor.GREEN + "All configurations reloaded successfully.");
        } catch (Exception e) {
            player.sendMessage(PvPPlugin.prefix + ChatColor.RED +"Failed to reload configurations.");
            e.printStackTrace();
        }

        return true;
    }
}
