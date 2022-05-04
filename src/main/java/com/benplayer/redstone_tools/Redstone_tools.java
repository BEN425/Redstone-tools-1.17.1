package com.benplayer.redstone_tools;

import com.benplayer.redstone_tools.command.*;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypeFilter;

public class Redstone_tools implements ModInitializer {
    @Override
    public void onInitialize() {
        /* Register commands */

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            // killitem

            LiteralCommandNode<ServerCommandSource> killItemNode = CommandManager
                .literal("killitem")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(ctx -> {
                    int n = 0;

                    // Remove items
                    var items = ctx.getSource().getWorld().getEntitiesByType(
                        TypeFilter.instanceOf(ItemEntity.class), it -> true
                    );
                    for (Entity entity : items) {
                            entity.kill();
                            n++;
                        }

                    // Remove xp orbs
                    var orbs = ctx.getSource().getWorld().getEntitiesByType(
                        TypeFilter.instanceOf(ExperienceOrbEntity.class), it -> true
                    );
                    for (Entity entity : orbs) {
                            entity.kill();
                            n++;
                        }

                    ctx.getSource().sendFeedback(Text.of(String.format("%d entities killed.", n)), false);
                    return n;
                })
                .build();

            // slab

            LiteralCommandNode<ServerCommandSource> slabNode = CommandManager
                .literal("slab")
                .requires(source -> source.hasPermissionLevel(4))
                .executes(ctx -> {
                    ctx.getSource().getPlayer().setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.SMOOTH_STONE_SLAB));
                    return 1;
                })
                .build();

            // Register

            // gives the player a block of the selected color
            // usage: /wool [color]
            WoolCommand.register(dispatcher);
            // usage: /glass [color]
            GlassCommand.register(dispatcher);
            // usage: /conbl [color]
            ConblCommand.register(dispatcher);
            // usage: /conpow [color]
            ConpowCommand.register(dispatcher);
            // usage: /terra [color]
            TerraCommand.register(dispatcher);
            // usage: /glaterra
            GlaTerraCommand.register(dispatcher);
            // give the player a block of selected signal
            // usage: /bar [int 0..15]
            BarrelCommand.register(dispatcher);
            // usage: /shulker [int 0..15]
            ShulkerCommand.register(dispatcher);
            // usage: /binCount [int 1..63] [bool] [int >0] [direction]
            BinCountCommand.register(dispatcher);
            // usage: /hexCount [int 1..16] [bool] [int >0] [direction]
            hexCountCommand.register(dispatcher);
            // usage: /killitem
            // kills all item entities
            dispatcher.getRoot().addChild(killItemNode);
            // usage: /slab
            // gives the player a slab
            dispatcher.getRoot().addChild(slabNode);
        }));

    }
}
