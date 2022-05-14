package com.benplayer.redstone_tools.keybindings;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

public class InstantBreakKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keys.redstone_tools.instant_break_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(InstantBreakKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (!toggleKey.isPressed() || client.player == null || !client.player.isCreative())
            return;

        Redstone_toolsClient.instantBreak = !Redstone_toolsClient.instantBreak;
        client.player.sendMessage(new TranslatableText(
            Redstone_toolsClient.instantBreak ? "Enable instant break" : "Disable instant break"
        ).formatted(
            Redstone_toolsClient.instantBreak ? Formatting.GREEN : Formatting.RED
        ), false);

        toggleKey.setPressed(false);
    }
}
