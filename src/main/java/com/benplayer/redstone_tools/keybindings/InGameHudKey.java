package com.benplayer.redstone_tools.keybindings;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InGameHudKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "keys.redstone_tools.ingamehud_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F8,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.isPressed()) {
                Redstone_toolsClient.inGameHud = !Redstone_toolsClient.inGameHud;
                toggleKey.setPressed(false);
            }
        });
    }
}
