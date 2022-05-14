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

public class ConpowCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> conpoNode = CommandManager
            .literal("conpow")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> conpoColorNode = CommandManager
            .argument("color", StringArgumentType.string())
            .executes(ConpowCommand::execute)
            .build();

        dispatcher.getRoot().addChild(conpoNode);
        conpoNode.addChild(conpoColorNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        String color = context.getArgument("color", String.class);
        ItemStack conpow = new ItemStack(getConpow(color));

        try {
            PlayerEntity player = context.getSource().getPlayer();
            PlayerInventory inventory = player.getInventory();

            // Remove redundant wools
            while (inventory.contains(conpow))
                inventory.removeStack(inventory.indexOf(conpow));

            // set main hand to wool
            player.setStackInHand(Hand.MAIN_HAND, conpow);

            return 1;
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            context.getSource().sendError(new TranslatableText("Command syntax error."));
            return 0;
        }
    }

    // return the corresponding concrete powder of color
    private static Item getConpow(String color) {
       return switch (color) {
            case ("white"), ("wh"), ("w") -> Items.WHITE_CONCRETE_POWDER;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_CONCRETE_POWDER;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_CONCRETE_POWDER;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_CONCRETE_POWDER;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_CONCRETE_POWDER;
            case ("lime"), ("lm"), ("l") -> Items.LIME_CONCRETE_POWDER;
            case ("pink"), ("pi"), ("p") -> Items.PINK_CONCRETE_POWDER;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_CONCRETE_POWDER;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_CONCRETE_POWDER;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_CONCRETE_POWDER;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_CONCRETE_POWDER;
            case ("blue"), ("bu") -> Items.BLUE_CONCRETE_POWDER;
            case ("brown"), ("br") -> Items.BROWN_CONCRETE_POWDER;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_CONCRETE_POWDER;
            case ("red"), ("r") -> Items.RED_CONCRETE_POWDER;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_CONCRETE_POWDER;
            default -> throw new IllegalArgumentException();
        };
    }
}
