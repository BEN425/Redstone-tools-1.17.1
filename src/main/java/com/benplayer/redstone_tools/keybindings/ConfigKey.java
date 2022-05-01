package com.benplayer.redstone_tools.keybindings;

import com.benplayer.redstone_tools.ConfigGui;
import com.benplayer.redstone_tools.RTScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ConfigKey {
    private static KeyBinding configKey;

    public static void register() {
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keys.redstone_tools.config_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.isPressed() && client.player != null && client.player.isCreative())
                MinecraftClient.getInstance().setScreen(new RTScreen(new ConfigGui()));
        });
    }
}