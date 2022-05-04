package com.benplayer.redstone_tools.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class TerraCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> terraNode = CommandManager
            .literal("terra")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> terraColorNode = CommandManager
            .argument("color", StringArgumentType.string())
            .executes(TerraCommand::execute)
            .build();

        dispatcher.getRoot().addChild(terraNode);
        terraNode.addChild(terraColorNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        try {
            ServerPlayerEntity player = source.getPlayer().getServer().getPlayerManager().getPlayer(source.getPlayer().getEntityName());
            PlayerInventory inventory = player.getInventory();
            String color = context.getArgument("color", String.class);
            ItemStack terra = new ItemStack(getTerra(color));

            // Remove redundant wools
            while (inventory.contains(terra))
                inventory.removeStack(inventory.indexOf(terra));

            // set main hand to wool
            player.setStackInHand(Hand.MAIN_HAND, terra);

            return 1;
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            context.getSource().sendError(new TranslatableText("Command syntax error."));
            return 0;
        } catch (NullPointerException e) {return 0;}
    }

    // return corresponding terracotta of color
    private static Item getTerra(String color) {
        return switch (color) {
            case ("white"), ("wh"), ("w") -> Items.WHITE_TERRACOTTA;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_TERRACOTTA;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_TERRACOTTA;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_TERRACOTTA;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_TERRACOTTA;
            case ("lime"), ("lm"), ("l") -> Items.LIME_TERRACOTTA;
            case ("pink"), ("pi"), ("p") -> Items.PINK_TERRACOTTA;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_TERRACOTTA;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_TERRACOTTA;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_TERRACOTTA;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_TERRACOTTA;
            case ("blue"), ("bu") -> Items.BLUE_TERRACOTTA;
            case ("brown"), ("br") -> Items.BROWN_TERRACOTTA;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_TERRACOTTA;
            case ("red"), ("r") -> Items.RED_TERRACOTTA;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_TERRACOTTA;
            case ("none"), ("n") -> Items.TERRACOTTA;
            default -> throw new IllegalArgumentException();
        };
    }
}
