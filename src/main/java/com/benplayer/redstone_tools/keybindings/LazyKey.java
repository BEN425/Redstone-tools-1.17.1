package com.benplayer.redstone_tools.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import org.lwjgl.glfw.GLFW;

/**
  *  Give the player common-used redstone items
  *  Redstone dust, repeater, comparator and torch
  *  */
public class LazyKey {
    private static KeyBinding lazyKeyBinding;

    public static void register() {
        lazyKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.redstone_tools.lazy_key",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "category.redstone_tools.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(LazyKey::execute);
    }

    private static void execute(MinecraftClient client) {
        if (!lazyKeyBinding.isPressed() || client.player == null || !client.player.isCreative())
            return;

        try {
            ServerPlayerEntity player = client.getServer().getPlayerManager().getPlayer(client.player.getUuid());
            PlayerInventory inventory = player.getInventory();

            if (player.isHolding(Items.REDSTONE)) {
                setItem(player, Items.REPEATER, (inventory.selectedSlot+1) % 9);
            } else if (player.isHolding(Items.REPEATER)) {
                setItem(player, Items.COMPARATOR, (inventory.selectedSlot+1) % 9);
            } else if (player.isHolding(Items.COMPARATOR)) {
                setItem(player, Items.REDSTONE_TORCH, (inventory.selectedSlot+1) % 9);
            } else if (!player.isHolding(Items.REDSTONE_TORCH)) {
                setItem(player, Items.REDSTONE, inventory.selectedSlot);
            }
        } catch (NullPointerException e) {
            System.out.println("Something wrong with lazyKey...");
        }
    }

    // Give player the item and arrange the inventory
    private static void setItem(PlayerEntity player, Item item, int slot) {
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = new ItemStack(item);

        // Remove redundant items
        while (inventory.contains(itemStack))
            inventory.removeStack(inventory.indexOf(itemStack));

        // Main hand has another item, move it to free space
        if (inventory.getStack(slot) != ItemStack.EMPTY && inventory.getEmptySlot() >= 0) {
            inventory.insertStack(inventory.getEmptySlot(), inventory.getStack(slot).copy());
            inventory.removeStack(slot);
        }

        inventory.insertStack(slot, itemStack);
    }
}
