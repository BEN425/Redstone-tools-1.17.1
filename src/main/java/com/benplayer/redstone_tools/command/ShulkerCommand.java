package com.benplayer.redstone_tools.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;

public class ShulkerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> shulkerNode = CommandManager
            .literal("shulker")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> shuNode = CommandManager
            .argument("signal", IntegerArgumentType.integer(0, 15))
            .executes(ShulkerCommand::execute)
            .build();

        dispatcher.getRoot().addChild(shulkerNode);
        shulkerNode.addChild(shuNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        int signal = context.getArgument("signal", int.class);

        try {
            source.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.fromNbt(getShulker(signal)));
        } catch (CommandSyntaxException e) {
            return 0;
        }

        return 1;
    }

    // Create the nbt tag of a barrel containing items
    private static NbtCompound getShulker(int signal) {
        NbtCompound shulkerNbt = new NbtCompound();
        NbtCompound itemNbt = new NbtCompound();
        NbtList itemListNbt = new NbtList();
        NbtCompound blockEntityNbt = new NbtCompound();
        NbtCompound enchantNbt = new NbtCompound();
        NbtList enchantListNbt = new NbtList();
        NbtCompound tag = new NbtCompound();

        // The number of items to put in barrel
        int items = (signal <= 1) ? signal :
                    (signal == 15) ? 27 : 2 * (signal-1);

        itemNbt.putByte("Count", (byte) 1);
        itemNbt.putString("id", "minecraft:wooden_axe");
        for (int i = 0; i < items; i++) {
            NbtCompound temp = itemNbt.copy();
            temp.putByte("Slot", (byte) i);
            itemListNbt.add(temp);
        }

        blockEntityNbt.putString("CustomName", String.format("%d", signal));
        blockEntityNbt.put("Items", itemListNbt);

        enchantNbt.putString("id", "minecraft:efficiency");
        enchantNbt.putShort("lvl", (short) signal);
        enchantListNbt.add(enchantNbt);

        tag.put("BlockEntityTag", blockEntityNbt);
        tag.put("Enchantments", enchantListNbt);
        tag.putInt("HideFlags", 1);

        shulkerNbt.putString("id", "minecraft:" + getColor(signal) + "_shulker_box");
        shulkerNbt.putByte("Count", (byte) 1);
        shulkerNbt.put("tag", tag);

        return shulkerNbt;
    }

    // return the corresponding string of color
    private static String getColor(int signal) {
        return switch (signal) {
            case 0 -> "white";
            case 1-> "orange";
            case 2 -> "magenta";
            case 3 -> "light_blue";
            case 4 -> "yellow";
            case 5 -> "lime";
            case 6 -> "pink";
            case 7 -> "gray";
            case 8 -> "light_gray";
            case 9 -> "cyan";
            case 10 -> "purple";
            case 11 -> "blue";
            case 12 -> "brown";
            case 13 -> "green";
            case 14 -> "red";
            case 15 -> "black";
            default -> throw new IllegalArgumentException();
        };
    }
}
