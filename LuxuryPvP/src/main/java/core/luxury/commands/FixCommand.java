package core.luxury.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import core.luxury.PvPPlugin;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FixCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>(); 

    public FixCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (cooldowns.containsKey(playerId)) {
            long timeLeft = (cooldowns.get(playerId) - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {
                player.sendMessage(PvPPlugin.prefix+ChatColor.RED+"§cYou are on cooldown! Please wait " + timeLeft + " seconds.");
                return true;
            }
        }

        cooldowns.put(playerId, System.currentTimeMillis() + (3 * 1000));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showPlayer(player);
                }
                player.sendMessage(PvPPlugin.prefix+ChatColor.GREEN+"You have been fixed continue playing!");
            }
        }.runTaskLater(plugin, 5L);
		return true; 
    }
}
