package core.luxury.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SpawnManager {

    private final File spawnFile;
    private FileConfiguration spawnConfig;
    
    private Location spawnLocation;
    private final JavaPlugin plugin;

    public SpawnManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.spawnFile = new File(plugin.getDataFolder(), "spawn.yml");
        if (!spawnFile.exists()) {
            try {
                spawnFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        loadSpawnLocation();
    }

    private void loadSpawnLocation() {
        if (spawnConfig.contains("spawn")) {
            double x = spawnConfig.getDouble("spawn.x");
            double y = spawnConfig.getDouble("spawn.y");
            double z = spawnConfig.getDouble("spawn.z");
            float yaw = (float) spawnConfig.getDouble("spawn.yaw");
            float pitch = (float) spawnConfig.getDouble("spawn.pitch");
            String worldName = spawnConfig.getString("spawn.world");
            this.spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        spawnConfig.set("spawn.world", location.getWorld().getName());
        spawnConfig.set("spawn.x", location.getX());
        spawnConfig.set("spawn.y", location.getY());
        spawnConfig.set("spawn.z", location.getZ());
        spawnConfig.set("spawn.yaw", location.getYaw());
        spawnConfig.set("spawn.pitch", location.getPitch());
        saveConfig();
    }

    public Location getSpawnLocation() {
        if (!spawnConfig.contains("spawn.world")) return null;

        String world = spawnConfig.getString("spawn.world");
        double x = spawnConfig.getDouble("spawn.x");
        double y = spawnConfig.getDouble("spawn.y");
        double z = spawnConfig.getDouble("spawn.z");
        float yaw = (float) spawnConfig.getDouble("spawn.yaw");
        float pitch = (float) spawnConfig.getDouble("spawn.pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    private void saveConfig() {
        try {
            spawnConfig.save(spawnFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        this.spawnConfig = YamlConfiguration.loadConfiguration(spawnFile);
        loadSpawnLocation(); 
    }

	public JavaPlugin getPlugin() {
		return plugin;
	}
}
