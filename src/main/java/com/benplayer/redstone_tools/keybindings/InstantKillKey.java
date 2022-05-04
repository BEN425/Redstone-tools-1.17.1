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

public class InstantKillKey {
    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.instant_kill_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(InstantKillKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (!toggleKey.isPressed() || client.player == null || !client.player.isCreative())
            return;

        try {
            PlayerEntity player = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());
            if (player.hasStatusEffect(StatusEffects.STRENGTH)) {
                player.removeStatusEffect(StatusEffects.STRENGTH);
                player.sendMessage(new TranslatableText(
                    "Disable instant kill"
                ), false);
            } else {
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.STRENGTH, Integer.MAX_VALUE, Short.MAX_VALUE, false, false, false
                ));
                player.sendMessage(new TranslatableText(
                    "Enable instant kill"
                ), false);
            }

            toggleKey.setPressed(false);
        } catch (Exception e) {
            System.out.println("Something wrong with instantKillKey...");
        }
    }
}
