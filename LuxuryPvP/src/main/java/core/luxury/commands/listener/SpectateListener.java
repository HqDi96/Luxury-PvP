package core.luxury.commands.listener;

import core.luxury.enums.PlayerStates;
import core.luxury.managers.PlayerManager;
import core.luxury.material.XItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SpectateListener implements Listener {

    private static final int SPECTATE_ITEM_SLOT = 8;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.getInstance().getPlayerState(player) == PlayerStates.SPECTATING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (PlayerManager.getInstance().getPlayerState(player) == PlayerStates.SPECTATING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerManager.getInstance().getPlayerState(player) == PlayerStates.SPECTATING &&
                event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (PlayerManager.getInstance().getPlayerState(player) == PlayerStates.SPECTATING) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WOODEN_DOOR) {
                event.setCancelled(true);
            }

            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.SLIME_BALL) {
                exitSpectateMode(player);
            }
        }
    }

    public void giveSpectateItem(Player player) {
        ItemStack spectateItem = new ItemStack(Material.SLIME_BALL);
        XItems.setLockedItem(player, spectateItem, SPECTATE_ITEM_SLOT);
    }

    public void exitSpectateMode(Player player) {
        for (Player otherPlayer : player.getServer().getOnlinePlayers()) {
            otherPlayer.showPlayer(player);
        }

        PlayerManager.getInstance().setPlayerState(player, PlayerStates.PLAYING);

        player.setAllowFlight(false);
        player.setFlying(false);
        player.getInventory().clear();

        player.sendMessage("You have exited spectate mode.");
    }
}
