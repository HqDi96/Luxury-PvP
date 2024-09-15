package core.luxury.events;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import core.luxury.enums.PlayerStates;
import core.luxury.managers.PlayerManager;
import core.luxury.managers.SpawnManager;
import core.luxury.managers.scoreb.DynamicScoreboard;
import dev.mqzen.boards.BoardManager;


public class XEvents implements Listener {

	
	
	private SpawnManager spawnManager;
	

    public XEvents(SpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }
	
    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true); 
    }
    

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        BoardManager.getInstance().setupNewBoard(player, new DynamicScoreboard());
        PlayerManager.getInstance().setPlayerState(player, PlayerStates.PLAYING);

        Location spawnLocation = spawnManager.getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        BoardManager.getInstance().removeBoard(player);

        Location spawnLocation = spawnManager.getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        }
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        event.setCancelled(true); 

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true); 
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true); 
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);

    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true); 

    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
        } else {
            event.setCancelled(true); 
        }
    }
}