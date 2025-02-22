package me.badaimweeb.mcelo;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminCommandHandler implements CommandExecutor {
    @NonNull
    private MCElo plugin;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (args.length > 0) {
                switch (args[0]) {
                    case "record": {
                        if (args.length < 4) {
                            sender.sendMessage(
                                    "Usage: /eloadmin record <player/uuid> <opponent/uuid> <result (0 = lose, 0.5 = draw, 1 = win)>");
                            return true;
                        }

                        String playerRaw = args[1];
                        String opponentRaw = args[2];
                        float result = Float.parseFloat(args[3]);

                        UUID playerUUID, opponentUUID;
                        try {
                            playerUUID = UUID.fromString(playerRaw);
                        } catch (IllegalArgumentException e) {
                            playerUUID = Bukkit.getOfflinePlayer(playerRaw).getUniqueId();
                        }

                        try {
                            opponentUUID = UUID.fromString(opponentRaw);
                        } catch (IllegalArgumentException e) {
                            opponentUUID = Bukkit.getOfflinePlayer(opponentRaw).getUniqueId();
                        }

                        var player = plugin.getPlayerDao().createIfNotExists(new EloPlayer(playerUUID, GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));

                        var opponent = plugin.getPlayerDao().createIfNotExists(new EloPlayer(opponentUUID, GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));

                        EloRecord record = new EloRecord();
                        record.setUuid(player.getUuid());
                        record.setOpponent(opponent.getUuid());
                        record.setResult(result);
                        record.setTimestamp(System.currentTimeMillis());
                        record.setBeforeElo(player.getElo());
                        record.setBeforeRD(player.getRd());
                        record.setBeforeVol(player.getVol());
                        record.setOpponentBeforeElo(opponent.getElo());
                        record.setOpponentBeforeRD(opponent.getRd());
                        record.setOpponentBeforeVol(opponent.getVol());

                        player.updateRating(opponent, MatchResult.fromValue(result));
                        plugin.getPlayerDao().update(player);
                        plugin.getPlayerDao().update(opponent);
                        
                        record.setAfterElo(player.getElo());
                        record.setAfterRD(player.getRd());
                        record.setAfterVol(player.getVol());
                        record.setOpponentAfterElo(opponent.getElo());
                        record.setOpponentAfterRD(opponent.getRd());
                        record.setOpponentAfterVol(opponent.getVol());

                        plugin.getRecordDao().create(record);

                        break;
                    }

                    case "recordanon": {
                        if (args.length < 5) {
                            sender.sendMessage(
                                    "Usage: /eloadmin recordanon <player/uuid> <result (0 = lose, 0.5 = draw, 1 = win)> <opponent rating> <opponent rd>");
                            return true;
                        }

                        String playerRaw = args[1];
                        float result = Float.parseFloat(args[2]);

                        UUID playerUUID;
                        try {
                            playerUUID = UUID.fromString(playerRaw);
                        } catch (IllegalArgumentException e) {
                            playerUUID = Bukkit.getOfflinePlayer(playerRaw).getUniqueId();
                        }

                        var player = plugin.getPlayerDao().createIfNotExists(new EloPlayer(playerUUID, GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));

                        double opponentElo = Double.parseDouble(args[3]);
                        double opponentRD = Double.parseDouble(args[4]);

                        EloRecord record = new EloRecord();
                        record.setUuid(player.getUuid());
                        record.setOpponent(UUID.nameUUIDFromBytes(new byte[0]));
                        record.setResult(result);
                        record.setTimestamp(System.currentTimeMillis());
                        record.setBeforeElo(player.getElo());
                        record.setBeforeRD(player.getRd());
                        record.setBeforeVol(player.getVol());
                        record.setOpponentBeforeElo(opponentElo);
                        record.setOpponentBeforeRD(opponentRD);
                        record.setOpponentBeforeVol(0);
 
                        player.updateRating(opponentElo, opponentRD, MatchResult.fromValue(result));
                        plugin.getPlayerDao().update(player);
                        
                        record.setAfterElo(player.getElo());
                        record.setAfterRD(player.getRd());
                        record.setAfterVol(player.getVol());
                        record.setOpponentAfterElo(opponentElo);
                        record.setOpponentAfterRD(opponentRD);
                        record.setOpponentAfterVol(0);

                        plugin.getRecordDao().create(record);

                        break;
                    }

                    case "setelo": {
                        if (args.length < 3) {
                            sender.sendMessage("Usage: /eloadmin setelo <player/uuid> <elo> [rd] [volatility]");
                            return true;
                        }

                        String playerRaw = args[1];
                        double elo = Double.parseDouble(args[2]);

                        UUID playerUUID;
                        try {
                            playerUUID = UUID.fromString(playerRaw);
                        } catch (IllegalArgumentException e) {
                            playerUUID = Bukkit.getOfflinePlayer(playerRaw).getUniqueId();
                        }

                        var player = plugin.getPlayerDao().createIfNotExists(new EloPlayer(playerUUID, GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));
                        player.setElo(elo);

                        if (args.length > 3) {
                            player.setRd(Double.parseDouble(args[3]));
                        }

                        if (args.length > 4) {
                            player.setVol(Double.parseDouble(args[4]));
                        }

                        plugin.getPlayerDao().update(player);

                        break;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}