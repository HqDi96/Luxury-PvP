package core.luxury.commands;

import core.luxury.PvPPlugin;
import core.luxury.commands.listener.SpectateListener;
import core.luxury.enums.PlayerStates;
import core.luxury.managers.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {

    private final SpectateListener spectateListener;

    public SpectateCommand(SpectateListener spectateListener) {
        this.spectateListener = spectateListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        PlayerStates currentState = PlayerManager.getInstance().getPlayerState(player);

        if (currentState == PlayerStates.PLAYING) {
            enterSpectateMode(player);
        } else {
            spectateListener.exitSpectateMode(player);
        }
        return true;
    }

    private void enterSpectateMode(Player player) {
        Bukkit.getOnlinePlayers().forEach(otherPlayer -> otherPlayer.hidePlayer(player));

        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        player.setHealth(player.getMaxHealth());

        PlayerManager.getInstance().setPlayerState(player, PlayerStates.SPECTATING);
        player.setAllowFlight(true);
        player.setFlying(true);

        /*
         * Hi, Hadi in the future there is some errors here do not forget em.
         */
        if (spectateListener != null) {
            spectateListener.giveSpectateItem(player);
        } else {
            player.sendMessage(PvPPlugin.prefix + ChatColor.RED + "SpectateListener is not initialized.");
        }
        player.sendMessage(PvPPlugin.prefix + ChatColor.YELLOW + "You are now in spectate mode! Right-click the slime in your hotbar to exit.");
    }
}
