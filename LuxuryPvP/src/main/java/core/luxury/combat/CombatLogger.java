package core.luxury.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import core.luxury.PvPPlugin;
import core.luxury.managers.PlayerManager;
import core.luxury.user.PvPPlayer;
import net.md_5.bungee.api.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogger implements Listener {
	
    private final Map<UUID, Long> combatTags = new HashMap<>();
    private static final long COMBAT_TAG_DURATION = PvPPlugin.getInstance().getConfig().getInt("combat.tag_duration") * 1000L;

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            combatTags.put(damager.getUniqueId(), System.currentTimeMillis());
            combatTags.put(damaged.getUniqueId(), System.currentTimeMillis());

            damager.sendMessage(PvPPlugin.prefix + ChatColor.RED + "Be Careful! You are now under combat mode!");
            damaged.sendMessage(PvPPlugin.prefix + ChatColor.RED + "Be Careful! You are now under combat mode!");

            PvPPlugin.getInstance().getServer().getPluginManager().callEvent(new CombatEnterEvent(damager));
            PvPPlugin.getInstance().getServer().getPluginManager().callEvent(new CombatEnterEvent(damaged));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (combatTags.containsKey(damager.getUniqueId()) &&
                        System.currentTimeMillis() - combatTags.get(damager.getUniqueId()) >= COMBAT_TAG_DURATION) {
                        combatTags.remove(damager.getUniqueId());
                        PvPPlugin.getInstance().getServer().getPluginManager().callEvent(new CombatExitEvent(damager));
                    }
                    if (combatTags.containsKey(damaged.getUniqueId()) &&
                        System.currentTimeMillis() - combatTags.get(damaged.getUniqueId()) >= COMBAT_TAG_DURATION) {
                        combatTags.remove(damaged.getUniqueId());
                        PvPPlugin.getInstance().getServer().getPluginManager().callEvent(new CombatExitEvent(damaged));
                    }
                }
            }.runTaskLater(PvPPlugin.getInstance(), COMBAT_TAG_DURATION / 50); 
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (combatTags.containsKey(player.getUniqueId())) {
            PvPPlayer pvpPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());
            pvpPlayer.addDeath();
            PlayerManager.getInstance().savePlayer(pvpPlayer);

            combatTags.remove(player.getUniqueId());
            PvPPlugin.getInstance().getServer().getPluginManager().callEvent(new CombatExitEvent(player));
        }
    }

     public boolean isInCombat(Player player) {
        return combatTags.containsKey(player.getUniqueId());
    }
}
