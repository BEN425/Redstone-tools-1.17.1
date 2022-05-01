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

public class GlaTerraCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> terraNode = CommandManager
                .literal("glaterra")
                .requires(source -> source.hasPermissionLevel(4))
                .build();

        CommandNode<ServerCommandSource> terraColorNode = CommandManager
                .argument("color", StringArgumentType.string())
                .executes(GlaTerraCommand::execute)
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

            if (!player.isCreative()) {
                source.sendError(new TranslatableText("You need creative mode to use this command."));
                return 0;
            }

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
            case ("white"), ("wh"), ("w") -> Items.WHITE_GLAZED_TERRACOTTA;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_GLAZED_TERRACOTTA;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_GLAZED_TERRACOTTA;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_GLAZED_TERRACOTTA;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_GLAZED_TERRACOTTA;
            case ("lime"), ("lm"), ("l") -> Items.LIME_GLAZED_TERRACOTTA;
            case ("pink"), ("pi"), ("p") -> Items.PINK_GLAZED_TERRACOTTA;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_GLAZED_TERRACOTTA;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_GLAZED_TERRACOTTA;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_GLAZED_TERRACOTTA;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_GLAZED_TERRACOTTA;
            case ("blue"), ("bu") -> Items.BLUE_GLAZED_TERRACOTTA;
            case ("brown"), ("br") -> Items.BROWN_GLAZED_TERRACOTTA;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_GLAZED_TERRACOTTA;
            case ("red"), ("r") -> Items.RED_GLAZED_TERRACOTTA;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_GLAZED_TERRACOTTA;
            default -> throw new IllegalArgumentException();
        };
    }
}
