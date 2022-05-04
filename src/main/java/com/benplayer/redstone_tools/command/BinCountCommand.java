package com.benplayer.redstone_tools.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class BinCountCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // root node
        LiteralCommandNode<ServerCommandSource> rootNode = CommandManager
            .literal("binCount")
            .requires(source -> source.hasPermissionLevel(2))
            .build();

        // Number of bits of the binary number
        CommandNode<ServerCommandSource> bitNode = CommandManager
            .argument("bits", IntegerArgumentType.integer(1, Long.SIZE-1))
            .build();

        // Signed or Unsigned
        CommandNode<ServerCommandSource> signNode = CommandManager
            .argument("signed", BoolArgumentType.bool())
            .build();

        // Distance between 2 blocks
        CommandNode<ServerCommandSource> distNode = CommandManager
            .argument("distance", IntegerArgumentType.integer(0))
            .build();

        // Direction of block series
        CommandNode<ServerCommandSource> dirNode = CommandManager
            .argument("direction", StringArgumentType.string())
            .executes(BinCountCommand::execute)
            .build();

        dispatcher.getRoot().addChild(rootNode);
        rootNode.addChild(bitNode);
        bitNode.addChild(signNode);
        signNode.addChild(distNode);
        distNode.addChild(dirNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        // The position of the player
        BlockPos pos = new BlockPos(context.getSource().getPosition().add(0, -1, 0));
        // Number of bits of the binary number
        int bits = context.getArgument("bits", int.class);
        // Signed
        boolean signed = context.getArgument("signed", boolean.class);
        // Distance
        int dist = context.getArgument("distance", int.class);
        // Direction
        String dir = context.getArgument("direction", String.class);

        // Vector of direction of blocks
        BlockPos blockDir = parseDirection(dir, dist);

        long sum = 0;
        World world = context.getSource().getWorld();
        for (int i = 0; i < bits; i++) {
            BlockState block = world.getBlockState(pos.add(blockDir.multiply(i)));

            // Redstone lamp & redstone torch
            if (block.contains(Properties.LIT) && block.get(Properties.LIT))
                sum += 1L << (bits - i - 1);
        }

        // Set sign bit
        BlockState block = world.getBlockState(pos);
        if (signed && block.contains(Properties.LIT) && block.get(Properties.LIT))
            sum += (-1L) << bits;

        context.getSource().sendFeedback(Text.of(String.valueOf(sum)), false);

        return 1;
    }

    // Parse direction argument
    private static BlockPos parseDirection(String dir, int dist) {
        return switch (dir.toLowerCase(Locale.ROOT)) {
            case ("d"), ("down"), ("y"), ("-y") -> new BlockPos(0, -dist - 1, 0);
            case ("north"), ("n"), ("-z") -> new BlockPos(0, 0, -dist - 1);
            case ("south"), ("s"), ("+z") -> new BlockPos(0, 0, dist + 1);
            case ("east"), ("e"), ("+x") -> new BlockPos(dist + 1, 0, 0);
            case ("west"), ("w"), ("-x") -> new BlockPos(-dist - 1, 0, 0);
            default -> throw new CommandException(new TranslatableText(
                    "Command syntax error."
                ));
        };
    }
}