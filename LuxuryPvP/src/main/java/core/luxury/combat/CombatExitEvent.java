package core.luxury.combat;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CombatExitEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public CombatExitEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}