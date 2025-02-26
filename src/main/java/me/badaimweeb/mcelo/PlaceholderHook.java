package me.badaimweeb.mcelo;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderHook extends PlaceholderExpansion {
    private final MCElo plugin;

    public PlaceholderHook(MCElo plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "mcelo";
    }

    @Override
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("elo")) {
            try {
                EloPlayer eloPlayer = plugin.getPlayerDao().queryForId(player.getUniqueId());
                if (eloPlayer == null) {
                    return "0";
                }
                return String.valueOf(eloPlayer.getElo());
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        } else if (identifier.equals("rd")) {
            try {
                EloPlayer eloPlayer = plugin.getPlayerDao().queryForId(player.getUniqueId());
                if (eloPlayer == null) {
                    return "0";
                }
                return String.valueOf(eloPlayer.getRd());
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        } else if (identifier.equals("glixare")) {
            try {
                EloPlayer eloPlayer = plugin.getPlayerDao().queryForId(player.getUniqueId());
                if (eloPlayer == null) {
                    return "0";
                }
                return String.valueOf(Math.round(eloPlayer.getGlixare() * GlobalVariable.glixareScale));
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }

        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }
}
