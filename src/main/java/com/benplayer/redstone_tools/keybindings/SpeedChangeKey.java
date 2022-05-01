package com.benplayer.redstone_tools.keybindings;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class SpeedChangeKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.speed_change_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(SpeedChangeKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (client.player == null || (!client.player.isCreative() && !client.player.isSpectator()))
            return;

        if (toggleKey.isPressed()) {
            Redstone_toolsClient.highSpeed = !Redstone_toolsClient.highSpeed;

            client.player.sendMessage(new TranslatableText(
                Redstone_toolsClient.highSpeed ? "Enable high speed" : "Disable high speed"
            ), false);

            toggleKey.setPressed(false);
        }
    }
}
