package me.badaimweeb.mcelo.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.badaimweeb.mcelo.ColorTextParser;
import me.badaimweeb.mcelo.EloPlayer;
import me.badaimweeb.mcelo.GlobalVariable;
import me.badaimweeb.mcelo.MCElo;
import me.badaimweeb.mcelo.Messages;
import net.kyori.adventure.audience.Audience;

@RequiredArgsConstructor
public class PlayerCommandHandler implements CommandExecutor, TabCompleter {
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

                Audience audience = plugin.getAdventure().sender(sender);

                var playerRecord = plugin.getPlayerDao().queryForId(p.getUniqueId());
                if (playerRecord == null) {
                    audience.sendMessage(ColorTextParser.parse(Messages.playerNotFound));
                    return true;
                }

                audience.sendMessage(ColorTextParser.parse(Messages.playerEloOther
                        .replace("<player>", p.getName() == null ? p.getUniqueId().toString() : p.getName())
                        .replace("<glixare>",
                                String.valueOf(
                                        Math.round(playerRecord.getGlixare() * GlobalVariable.glixareScale)))
                        .replace("<rating>", String.valueOf(Math.round(playerRecord.getElo())))
                        .replace("<rd>", String.valueOf(Math.round(playerRecord.getRd() * 100) / 100))));
            } else {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("You must be a player to use this command.");
                    return true;
                }

                var playerRecord = plugin.getPlayerDao()
                        .createIfNotExists(new EloPlayer(((Player) sender).getUniqueId(), GlobalVariable.initialRating,
                                GlobalVariable.initialRD, GlobalVariable.initialVolatility));

                Audience audience = plugin.getAdventure().sender(sender);

                audience.sendMessage(ColorTextParser.parse(Messages.playerElo
                        .replace("<glixare>", String.valueOf(
                                Math.round(playerRecord.getGlixare() * GlobalVariable.glixareScale)))
                        .replace("<rating>", String.valueOf(Math.round(playerRecord.getElo())))
                        .replace("<rd>", String.valueOf(Math.round(playerRecord.getRd() * 100) / 100))));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.getLogger().info(String.format("label: %s, args: %s", label, String.join(", ", args)));
        if (args.length == 0) {
            return null;
        } else {
            // Return empty list
            return new ArrayList<String>() {
            };
        }
    }
}
