package me.badaimweeb.mcelo;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorTextParser {
    public static MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component parse(String text) {
        return miniMessage.deserialize(LegacyColorCodeHandler.fromLegacy(text, "&"));
    }
}
