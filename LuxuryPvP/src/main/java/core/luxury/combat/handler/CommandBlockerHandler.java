package core.luxury.combat.handler;

import core.luxury.combat.CombatLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlockerHandler implements Listener {

    private final CombatLogger combatLogger;

    public CommandBlockerHandler(CombatLogger combatLogger) {
        this.combatLogger = combatLogger;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (combatLogger.isInCombat(event.getPlayer())) {
            event.setCancelled(true);
            player.sendMessage("You cannot use commands while in combat!");
        }
    }
}
