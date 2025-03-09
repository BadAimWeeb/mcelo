package me.badaimweeb.mcelo;

public class Messages {
    public static String playerElo;
    public static String playerEloOther;
    public static String playerNotFound;
    public static String recalculatingGlixare;
    public static String recalculatingGlixareFinished;
    public static String errorOccurred;
    public static String commandNotFound;

    public static void load(MCElo main) {
        var config = main.getConfig();

        playerElo = config.getString("messages.player-elo");
        playerEloOther = config.getString("messages.player-elo-other");
        playerNotFound = config.getString("messages.player-not-found");
        recalculatingGlixare = config.getString("messages.recalculating-glixare");
        recalculatingGlixareFinished = config.getString("messages.recalculating-glixare-finished");
        errorOccurred = config.getString("messages.error-occurred");
        commandNotFound = config.getString("messages.command-not-found");
    }
}
