package core.luxury.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import core.luxury.PvPPlugin;
import core.luxury.enums.PlayerStates;
import core.luxury.user.PvPPlayer;

public class PlayerManager {
    private static PlayerManager instance;
    private final Map<UUID, PlayerStates> playerStates = new ConcurrentHashMap<>();
    private final Map<UUID, PvPPlayer> playerData = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public PvPPlayer getPlayer(UUID uuid) {
        lock.lock();
        try {
            return playerData.get(uuid);
        } finally {
            lock.unlock();
        }
    }

    public void loadPlayer(UUID uuid) {
        lock.lock();
        try {
            try (Connection connection = PvPPlugin.getInstance().getHikari().getConnection()) {
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    PvPPlayer player = new PvPPlayer(uuid, rs.getInt("kills"), rs.getInt("deaths"), rs.getInt("kill_streak"), rs.getInt("top_kill_streak"));
                    playerData.put(uuid, player);
                } else {
                    PvPPlayer player = new PvPPlayer(uuid);
                    playerData.put(uuid, player);
                    savePlayer(player);
                }
            } catch (Exception e) {
                PvPPlugin.getInstance().getLogger().severe("Error loading player: " + e.getMessage());
            }
        } finally {
            lock.unlock();
        }
    }

    public void savePlayer(PvPPlayer player) {
        lock.lock();
        try {
            try (Connection connection = PvPPlugin.getInstance().getHikari().getConnection()) {
                PreparedStatement ps = connection.prepareStatement(
                    "REPLACE INTO players (uuid, kills, deaths, kill_streak, top_kill_streak) VALUES (?, ?, ?, ?, ?)");
                ps.setString(1, player.getUuid().toString());
                ps.setInt(2, player.getKills());
                ps.setInt(3, player.getDeaths());
                ps.setInt(4, player.getKillStreak());
                ps.setInt(5, player.getTopKillStreak());
                ps.executeUpdate();
            } catch (Exception e) {
                PvPPlugin.getInstance().getLogger().severe("Error saving player: " + e.getMessage());
            }
        } finally {
            lock.unlock();
        }
    }

    public void saveAllPlayers() {
        lock.lock();
        try {
            for (PvPPlayer player : playerData.values()) {
                savePlayer(player);
            }
        } finally {
            lock.unlock();
        }
    }

    public void loadAllPlayers() {
        lock.lock();
        try {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                loadPlayer(onlinePlayer.getUniqueId());
            }
        } finally {
            lock.unlock();
        }
    }

    public PlayerStates getPlayerState(Player player) {
        return playerStates.getOrDefault(player.getUniqueId(), PlayerStates.PLAYING);
    }

    public void setPlayerState(Player player, PlayerStates state) {
        playerStates.put(player.getUniqueId(), state);
    }
}