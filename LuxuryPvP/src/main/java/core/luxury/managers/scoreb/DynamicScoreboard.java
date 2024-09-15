package core.luxury.managers.scoreb;

import core.luxury.PvPPlugin;
import core.luxury.enums.PlayerStates;
import core.luxury.managers.PlayerManager;
import core.luxury.user.PvPPlayer;
import core.luxury.utils.ColorUtils;
import dev.mqzen.boards.base.BoardAdapter;
import dev.mqzen.boards.entity.Body;
import dev.mqzen.boards.entity.Title;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class DynamicScoreboard implements BoardAdapter {

    private final FileConfiguration scoreboardConfig;

    public DynamicScoreboard() {
        if (PvPPlugin.getInstance().getScoreboardConfig() == null) {
            PvPPlugin.getInstance().loadScoreboardConfig();
        }
        this.scoreboardConfig = PvPPlugin.getInstance().getScoreboardConfig();
    }


    @Override
    public Title<?> title(Player player) {
        PlayerStates state = PlayerManager.getInstance().getPlayerState(player);
        String title = scoreboardConfig.getString(state == PlayerStates.PLAYING ? "scoreboard.playing.title" : "scoreboard.spectating.title", "&aLuxury &cx &aPvP");
        return Title.legacy().ofText(ColorUtils.format(title));
    }

    @Override
    public Body<?> getBody(Player player) {
        PlayerStates state = PlayerManager.getInstance().getPlayerState(player);
        
        if (state == PlayerStates.PLAYING) {
            String kills = "NaN";
            String deaths = "NaN";
            String killstreak = "NaN";
            String topKillstreak = "NaN";

            try {
                PvPPlayer pvpPlayer = PlayerManager.getInstance().getPlayer(player.getUniqueId());

                if (pvpPlayer != null) {
                    kills = String.valueOf(pvpPlayer.getKills());
                    deaths = String.valueOf(pvpPlayer.getDeaths());
                    killstreak = String.valueOf(pvpPlayer.getKillStreak());
                    topKillstreak = String.valueOf(pvpPlayer.getTopKillStreak());
                }
            } catch (Exception e) {
                player.sendMessage(PvPPlugin.prefix + ColorUtils.format("&cError connecting to the database. Stats set to NaN."));
                
                for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                    if (onlinePlayer.isOp()) {
                        onlinePlayer.sendMessage(PvPPlugin.prefix + ColorUtils.format("&cThe database connection has failed. Please reconnect."));
                    }
                }
            }

            List<String> lines = scoreboardConfig.getStringList("scoreboard.playing.lines");
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, ColorUtils.format(lines.get(i)
                        .replace("{kills}", kills)
                        .replace("{deaths}", deaths)
                        .replace("{killstreak}", killstreak)
                        .replace("{topKillstreak}", topKillstreak)));
            }
            return Body.legacy(lines.toArray(new String[0]));

        } else {
            List<String> lines = scoreboardConfig.getStringList("scoreboard.spectating.lines");
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, ColorUtils.format(lines.get(i)));
            }
            return Body.legacy(lines.toArray(new String[0]));
        }
    }
}
