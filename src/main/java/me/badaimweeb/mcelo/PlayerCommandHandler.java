package me.badaimweeb.mcelo;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerCommandHandler implements CommandExecutor {
    @NonNull
    private MCElo plugin;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (args.length > 0) {
                UUID playerUUID;
                try {
                    playerUUID = UUID.fromString(args[0]);
                } catch (IllegalArgumentException e) {
                    playerUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
                }

                OfflinePlayer p = Bukkit.getOfflinePlayer(playerUUID);

                var playerRecord = plugin.getPlayerDao().queryForId(p.getUniqueId());
                if (playerRecord == null) {
                    sender.sendMessage(Messages.playerNotFound);
                    return true;
                }

                sender.sendMessage(Messages.playerEloOther
                        .replace("<player>", p.getName() == null ? p.getUniqueId().toString() : p.getName())
                        .replace("<glixare>",
                                String.valueOf(
                                        Math.round(playerRecord.getGlixareRating() * GlobalVariable.glixareScale)))
                        .replace("<rating>", String.valueOf(Math.round(playerRecord.getElo())))
                        .replace("<rd>", String.valueOf(Math.round(playerRecord.getRd() * 100) / 100)));
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                var playerRecord = plugin.getPlayerDao()
                        .createIfNotExists(new EloPlayer(((Player) sender).getUniqueId(), GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));

                sender.sendMessage(Messages.playerElo
                        .replace("<glixare>", String.valueOf(
                                Math.round(playerRecord.getGlixareRating() * GlobalVariable.glixareScale)))
                        .replace("<rating>", String.valueOf(Math.round(playerRecord.getElo())))
                        .replace("<rd>", String.valueOf(Math.round(playerRecord.getRd() * 100) / 100)));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
