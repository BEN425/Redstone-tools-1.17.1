package com.benplayer.redstone_tools.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Hand;

public class LecternCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> lecNode = CommandManager
            .literal("lectern")
            .requires(source -> source.hasPermissionLevel(4))
            .executes(LecternCommand::execute)
            .build();

        dispatcher.getRoot().addChild(lecNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        // A book has 15 pages
        NbtCompound bookNbt = new NbtCompound();
        bookNbt.putByte("Count", (byte) 1);
        bookNbt.putString("id", "minecraft:written_book");
        bookNbt.put("tag", new NbtCompound());
        // Write data to the book
        bookNbt.getCompound("tag").putString("author", "Redstone Tools");
        bookNbt.getCompound("tag").putString("title", "The book");
        NbtList pages = new NbtList();
        for (int i = 1; i <= 15; i++)
            pages.add(NbtString.of(String.valueOf(i)));
        bookNbt.getCompound("tag").put("pages", pages);

        // A lectern contains the book
        NbtCompound lecNbt = new NbtCompound();
        NbtCompound tag = new NbtCompound();
        lecNbt.putByte("Count", (byte) 1);
        lecNbt.putString("id", "minecraft:lectern");
        lecNbt.put("tag", tag);
        tag.put("BlockEntityTag", new NbtCompound());
        tag.getCompound("BlockEntityTag").put("Book", bookNbt);
        // Add enchantment effect
        NbtCompound enchNbt = new NbtCompound();
        enchNbt.putString("id", "minecraft:unbreaking");
        enchNbt.putShort("lvl", (short) 1);
        tag.put("Enchantments", new NbtList());
        tag.getList("Enchantments", NbtElement.COMPOUND_TYPE).add(enchNbt);
        tag.putInt("HideFlags", 1);

        try {
            context.getSource().getPlayer().setStackInHand(Hand.MAIN_HAND, ItemStack.fromNbt(lecNbt));
        } catch (CommandSyntaxException e) {
            return 0;
        }

        return 1;
    }
}
