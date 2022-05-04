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

public class GlassCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> glassNode = CommandManager
            .literal("glass")
            .requires(source -> source.hasPermissionLevel(4))
            .build();

        CommandNode<ServerCommandSource> glassColorNode = CommandManager
            .argument("color", StringArgumentType.string())
            .executes(GlassCommand::execute)
            .build();

        dispatcher.getRoot().addChild(glassNode);
        glassNode.addChild(glassColorNode);
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        try {
            ServerPlayerEntity player = source.getPlayer().getServer().getPlayerManager().getPlayer(source.getPlayer().getEntityName());
            PlayerInventory inventory = player.getInventory();
            String color = context.getArgument("color", String.class);
            ItemStack glass = new ItemStack(getGlass(color));

            // Remove redundant wools
            while (inventory.contains(glass))
                inventory.removeStack(inventory.indexOf(glass));

            // set main hand to wool
            player.setStackInHand(Hand.MAIN_HAND, glass);

            return 1;
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            context.getSource().sendError(new TranslatableText("Command syntax error."));
            return 0;
        } catch (NullPointerException e) {return 0;}
    }

    // return the corresponding glass of color
    private static Item getGlass(String color) {
        return switch (color) {
            case ("white"), ("wh"), ("w") -> Items.WHITE_STAINED_GLASS;
            case ("orange"), ("or"), ("o") -> Items.ORANGE_STAINED_GLASS;
            case ("magenta"), ("ma"), ("m") -> Items.MAGENTA_STAINED_GLASS;
            case ("light_blue"), ("libl"), ("lb") -> Items.LIGHT_BLUE_STAINED_GLASS;
            case ("yellow"), ("ye"), ("y") -> Items.YELLOW_STAINED_GLASS;
            case ("lime"), ("lm"), ("l") -> Items.LIME_STAINED_GLASS;
            case ("pink"), ("pi"), ("p") -> Items.PINK_STAINED_GLASS;
            case ("gray"), ("ga"), ("gy") -> Items.GRAY_STAINED_GLASS;
            case ("light_gray"), ("liga"), ("lg") -> Items.LIGHT_GRAY_STAINED_GLASS;
            case ("cyan"), ("cy"), ("c") -> Items.CYAN_STAINED_GLASS;
            case ("purple"), ("pu"), ("pr") -> Items.PURPLE_STAINED_GLASS;
            case ("blue"), ("bu") -> Items.BLUE_STAINED_GLASS;
            case ("brown"), ("br") -> Items.BROWN_STAINED_GLASS;
            case ("green"), ("ge"), ("gn") -> Items.GREEN_STAINED_GLASS;
            case ("red"), ("r") -> Items.RED_STAINED_GLASS;
            case ("black"), ("ba"), ("bk") -> Items.BLACK_STAINED_GLASS;
            default -> throw new IllegalArgumentException();
        };
    }
}
