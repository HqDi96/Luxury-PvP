package core.luxury.utils;

import net.md_5.bungee.api.ChatColor;

public class ColorUtils {
    public static String format(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
