package core.luxury.managers;


import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import core.luxury.PvPPlugin;

public class WorldManager {
	
    private PvPPlugin plugin;
    private World world;
    private BukkitTask task;

    public WorldManager(PvPPlugin plugin) {
        this.setPlugin(plugin);
        this.world = Bukkit.getWorlds().get(0); 

        world.setWeatherDuration(0);
        world.setTime(6000); 

        task = new BukkitRunnable() {
            @Override
            public void run() {
                world.setTime(6000);
            }
        }.runTaskTimer(plugin, 0, 200); 
    }

    public void disable() {
        task.cancel();
    }

	public PvPPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(PvPPlugin plugin) {
		this.plugin = plugin;
	}
}