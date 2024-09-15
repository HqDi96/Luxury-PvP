package core.luxury;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariDataSource;

import core.luxury.combat.CombatLogger;
import core.luxury.combat.handler.CommandBlockerHandler;
import core.luxury.commands.FixCommand;
import core.luxury.commands.ReloadCommand;
import core.luxury.commands.SpawnCommand;
import core.luxury.commands.SpectateCommand;
import core.luxury.commands.listener.SpectateListener;
import core.luxury.events.XEvents;
import core.luxury.managers.PlayerManager;
import core.luxury.managers.SpawnManager;
import core.luxury.managers.WorldManager;
import core.luxury.utils.ColorUtils;
import dev.mqzen.boards.BoardManager; 

public class PvPPlugin extends JavaPlugin {
	
	
    private static PvPPlugin instance;
    private HikariDataSource hikari;
    
    private SpectateListener spectateListener;
    private WorldManager worldManager;
    private CombatLogger combatLogger;
    
    

    private FileConfiguration scoreboardConfig;
    private File scoreboardConfigFile;
    
    private SpawnManager spawnManager;
    public static String prefix = ColorUtils.format("&8▏  &cPvP &8» ");

    @Override
    public void onEnable() {
    	
        instance = this;
        
        /*
         * Debugs
         */ 
        getLogger().info("Starting LuxuryPvP Plugin...");
        
        getLogger().info("Calling saveDefaultConfig()");
        saveDefaultConfig();
        loadConfigs();
        getLogger().info("Finished calling saveDefaultConfig()");
        
        registerCommands();
        registerListener();
        setupDatabase();
        registerBoard();
        PlayerManager.getInstance().loadAllPlayers();
        
        
        log(
            "&6-----------------------------------------",
            "&e           &aLuxury x &cPvP &ev1.0.1          ",
            "&6-----------------------------------------",
            "    &bLoading configuration...",
            "    &aPlugin developed by &e@ Hadi",
            "    &aThank you for using the supporting &b@ Luxury Developments.",
            "&6-----------------------------------------"
        );
    }

    @Override
    public void onDisable() {
    	
        worldManager.disable();
        PlayerManager.getInstance().saveAllPlayers();
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
        }
        
        log(
                "&6-----------------------------------------",
                "&e           &aLuxury x &cPvP &ev1.0.1          ",
                "&6-----------------------------------------",
                "           &cPlugin shutting down.",
                "&6-----------------------------------------"
            );
        }

    private void log(String... messages) {
        CommandSender receiver = this.getServer().getConsoleSender();
        for (String message : messages) {
            receiver.sendMessage(ColorUtils.format(message));
        }
    }
    
    
    public void loadConfigs() {
        saveDefaultConfig();
        loadScoreboardConfig();
    }

    public void reloadConfigs() {
        reloadConfig();  
        loadScoreboardConfig(); 
        spawnManager.reloadConfig(); 
    }

    private void registerCommands() {
        getCommand("fix").setExecutor(new FixCommand(this));
        getCommand("spectate").setExecutor(new SpectateCommand(spectateListener));
        
        getCommand("spawn").setExecutor(new SpawnCommand(spawnManager));
        getCommand("spawnp").setExecutor(new SpawnCommand(spawnManager));
        getCommand("luxuryreload").setExecutor(new ReloadCommand());
    }
    
    private void registerListener() {
        PluginManager pm = getServer().getPluginManager();
        spectateListener = new SpectateListener();
        spawnManager = new SpawnManager(this);
        combatLogger = new CombatLogger(); 
        
        pm.registerEvents(new CombatLogger(), this);
        pm.registerEvents(new XEvents(spawnManager), this);
        pm.registerEvents(spectateListener, this);
        pm.registerEvents(new CommandBlockerHandler(combatLogger), this);
        setWorldManager(new WorldManager(this));
    }
    
    private void registerBoard() {
		BoardManager.load(this);
		BoardManager.getInstance().setUpdateInterval(4L);
		BoardManager.getInstance().startBoardUpdaters();
    }
    
    private void setupDatabase() {
        FileConfiguration config = getConfig();
        hikari = new HikariDataSource();
        hikari.setJdbcUrl("jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getInt("mysql.port") + "/" + config.getString("mysql.database"));
        hikari.setUsername(config.getString("mysql.username"));
        hikari.setPassword(config.getString("mysql.password"));
        saveConfig();
        
      }
    
    public void loadScoreboardConfig() {
        scoreboardConfigFile = new File(getDataFolder(), "scoreboard.yml");
        if (!scoreboardConfigFile.exists()) {
            this.saveResource("scoreboard.yml", false);
        }
        scoreboardConfig = YamlConfiguration.loadConfiguration(scoreboardConfigFile);
    }
    

    public static PvPPlugin getInstance() {
        return instance;
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

	public WorldManager getWorldManager() {
		return worldManager;
	}

	public void setWorldManager(WorldManager worldManager) {
		this.worldManager = worldManager;
	}
    public CombatLogger getCombatLogger() {
        return combatLogger;
    }
    
    public SpectateListener getSpectateListener() {
        return spectateListener;
    }
    
    public FileConfiguration getScoreboardConfig() {
        return scoreboardConfig;
    }
}

