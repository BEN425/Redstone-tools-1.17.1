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

public class BarrelCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> barrelNode = CommandManager
            .literal("bar")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> barNode = CommandManager
            .argument("signal", IntegerArgumentType.integer(0, 15))
            .executes(BarrelCommand::execute)
            .build();

        dispatcher.getRoot().addChild(barrelNode);
        barrelNode.addChild(barNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        int signal = context.getArgument("signal", int.class);

        try {
            source.getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.fromNbt(getBarrel(signal)));
        } catch (CommandSyntaxException e) {
            return 0;
        }

        return 1;
    }

    // Create the nbt tag of a barrel containing items
    private static NbtCompound getBarrel(int signal) {
        NbtCompound barrelNbt = new NbtCompound();
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

        barrelNbt.putString("id", "minecraft:barrel");
        barrelNbt.putByte("Count", (byte) 1);
        barrelNbt.put("tag", tag);

        return barrelNbt;
    }
}
