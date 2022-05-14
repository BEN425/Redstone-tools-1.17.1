package com.benplayer.redstone_tools.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

public class WoolCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> woolNode = CommandManager
            .literal("wool")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> woolColorNode = CommandManager
            .argument("color", StringArgumentType.string())
            .executes(WoolCommand::execute)
            .build();

        dispatcher.getRoot().addChild(woolNode);
        woolNode.addChild(woolColorNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        String color = context.getArgument("color", String.class);
        ItemStack wool = new ItemStack(getWool(color));

        try {
            PlayerEntity player = context.getSource().getPlayer();
            PlayerInventory inventory = player.getInventory();

            // Remove redundant wools
            while (inventory.contains(wool))
                inventory.removeStack(inventory.indexOf(wool));

            // set main hand to wool
            player.setStackInHand(Hand.MAIN_HAND, wool);

            return 1;
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            context.getSource().sendError(new TranslatableText("Command syntax error."));
            return 0;
        }
    }

    // return the corresponding wool of color
    private static Item getWool(String color) {
       return switch (color) {
            case ("white"), ("wh"), ("w") -> Items.WHITE_WOOL;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_WOOL;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_WOOL;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_WOOL;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_WOOL;
            case ("lime"), ("lm"), ("l") -> Items.LIME_WOOL;
            case ("pink"), ("pi"), ("p") -> Items.PINK_WOOL;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_WOOL;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_WOOL;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_WOOL;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_WOOL;
            case ("blue"), ("bu") -> Items.BLUE_WOOL;
            case ("brown"), ("br") -> Items.BROWN_WOOL;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_WOOL;
            case ("red"), ("r") -> Items.RED_WOOL;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_WOOL;
            default -> throw new IllegalArgumentException();
        };
    }
}
