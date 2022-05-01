package com.benplayer.redstone_tools.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class NightVisionKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.night_vision_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(NightVisionKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (client.player == null || (!client.player.isCreative() && !client.player.isSpectator()))
            return;

        try {
            if (toggleKey.isPressed()) {
                PlayerEntity player = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());

                if (player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
                    player.removeStatusEffect(StatusEffects.NIGHT_VISION);
                    player.sendMessage(new TranslatableText(
                        "Disable night vision"
                    ), false);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false, false
                    ));
                    player.sendMessage(new TranslatableText(
                        "Enable night vision"
                    ), false);
                }

                toggleKey.setPressed(false);
            }
        } catch (NullPointerException e) {
            System.out.println("Something wrong with nightVisionKey...");
        }
    }
}
