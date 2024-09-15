package core.luxury.user;

import java.util.UUID;

public class PvPPlayer {
    private final UUID uuid;
    private int kills;
    private int deaths;
    private int killStreak;
    private int topKillStreak;

    public PvPPlayer(UUID uuid) {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.killStreak = 0;
        this.topKillStreak = 0;
    }

    public PvPPlayer(UUID uuid, int kills, int deaths, int killStreak, int topKillStreak) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.killStreak = killStreak;
        this.topKillStreak = topKillStreak;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
        killStreak++;
        if (killStreak > topKillStreak) {
            topKillStreak = killStreak;
        }
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
        killStreak = 0;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public int getTopKillStreak() {
        return topKillStreak;
    }
}
