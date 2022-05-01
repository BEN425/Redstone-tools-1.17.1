package com.benplayer.redstone_tools.keybindings;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class PlaceRedstoneKey {
    private static KeyBinding toggleKey;

    public static void regsiter() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.place_redstone_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(PlaceRedstoneKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (!toggleKey.isPressed() || client.player == null || !client.player.isCreative())
            return;

        Redstone_toolsClient.placeRedstone = !Redstone_toolsClient.placeRedstone;
        client.player.sendMessage(new TranslatableText(
            Redstone_toolsClient.placeRedstone ? "Enable placing redstone" : "Disable placing redstone"
        ), false);

        toggleKey.setPressed(false);
    }
}
