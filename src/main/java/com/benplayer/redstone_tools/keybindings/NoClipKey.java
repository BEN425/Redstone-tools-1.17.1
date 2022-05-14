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

public class NoClipKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.noclip_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(NoClipKey::execute);
    }

    private static void execute(MinecraftClient client) {
        // Not creative mode or spectator mode
        if (client.player == null || !client.player.isCreative())
            return;

        if (toggleKey.isPressed()) {
            Redstone_toolsClient.noClip = !Redstone_toolsClient.noClip;
            client.player.sendMessage(new TranslatableText(
                Redstone_toolsClient.noClip ? "Enable noclip" : "Disable noclip"
            ).formatted(
                Redstone_toolsClient.noClip ? Formatting.GREEN : Formatting.RED
            ), false);
            toggleKey.setPressed(false);
        }
    }
}
