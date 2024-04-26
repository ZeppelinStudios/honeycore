package com.honeysurvival.core;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoreMessage {

    private String message;

    public CoreMessage(String message) {
        this.message = message;
    }

    public CoreMessage color() {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
        return this;
    }

    public CoreMessage hex() {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        this.message = ChatColor.translateAlternateColorCodes('&', message);

        return this;
    }

    public CoreMessage placeholder(String placeholder, Object object) {
        String string = object instanceof String ? (String) object : object.toString();
        this.message = message.replaceAll(placeholder, string);
        return this;
    }

    public CoreMessage defaultPlaceholders(String playerName, String targetName) {

        if (!playerName.isEmpty()) {
            placeholder("%player_name%", playerName);
        }

        if (!targetName.isEmpty()) {
            placeholder("%target_name%", targetName);
        }

        return this;

    }

    @Override
    public String toString() {
        return message;
    }
}
