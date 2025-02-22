package me.badaimweeb.mcelo;

public class Messages {
    public static String playerElo;
    public static String playerEloOther;
    public static String playerNotFound;

    public static void load(MCElo main) {
        var config = main.getConfig();

        playerElo = config.getString("messages.player-elo");
        playerEloOther = config.getString("messages.player-elo-other");
        playerNotFound = config.getString("messages.player-not-found");
    }
}
