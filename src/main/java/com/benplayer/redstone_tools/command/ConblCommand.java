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

public class ConblCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> conblNode = CommandManager
            .literal("conbl")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> conblColorNode = CommandManager
            .argument("color", StringArgumentType.string())
            .executes(ConblCommand::execute)
            .build();

        dispatcher.getRoot().addChild(conblNode);
        conblNode.addChild(conblColorNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        try {
            ServerPlayerEntity player = source.getPlayer().getServer().getPlayerManager().getPlayer(source.getPlayer().getEntityName());
            PlayerInventory inventory = player.getInventory();
            String color = context.getArgument("color", String.class);
            ItemStack conbl = new ItemStack(getConbl(color));

            // Remove redundant wools
            while (inventory.contains(conbl))
                inventory.removeStack(inventory.indexOf(conbl));

            // set main hand to wool
            player.setStackInHand(Hand.MAIN_HAND, conbl);

            return 1;
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            context.getSource().sendError(new TranslatableText("Command syntax error."));
            return 0;
        } catch (NullPointerException e) {return 0;}
    }

    // return the corresponding concrete of color
    private static Item getConbl(String color) {
        return switch (color) {
            case ("white"), ("wh"), ("w") -> Items.WHITE_CONCRETE;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_CONCRETE;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_CONCRETE;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_CONCRETE;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_CONCRETE;
            case ("lime"), ("lm"), ("l") -> Items.LIME_CONCRETE;
            case ("pink"), ("pi"), ("p") -> Items.PINK_CONCRETE;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_CONCRETE;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_CONCRETE;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_CONCRETE;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_CONCRETE;
            case ("blue"), ("bu") -> Items.BLUE_CONCRETE;
            case ("brown"), ("br") -> Items.BROWN_CONCRETE;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_CONCRETE;
            case ("red"), ("r") -> Items.RED_CONCRETE;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_CONCRETE;
            default -> throw new IllegalArgumentException();
        };
    }
}
