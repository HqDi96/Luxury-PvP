package core.luxury.commands;

import core.luxury.PvPPlugin;
import core.luxury.managers.SpawnManager;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final SpawnManager spawnManager;

    public SpawnCommand(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PvPPlugin.prefix + ChatColor.RED +"Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("spawn")) {
            if (!player.hasPermission("luxury.spawn") && !player.isOp()) {
                player.sendMessage(PvPPlugin.prefix + ChatColor.RED +"You do not have permission to use this command.");
                return true;
            }

            Location spawnLocation = spawnManager.getSpawnLocation();
            if (spawnLocation == null) {
                player.sendMessage(PvPPlugin.prefix + ChatColor.RED +"No spawn point is set.");
                return true;
            }

            player.teleport(spawnLocation);
            player.sendMessage("You have been teleported to the spawn.");
        } else if (label.equalsIgnoreCase("spawnp")) {
            if (!player.hasPermission("luxury.spawn") && !player.isOp()) {
                player.sendMessage(PvPPlugin.prefix + ChatColor.RED +"You do not have permission to set the spawn point.");
                return true;
            }

            spawnManager.setSpawnLocation((Location) player);
            player.sendMessage(PvPPlugin.prefix + ChatColor.GREEN + "Spawn point has been set at your current location.");
        }

        return true;
    }
}
