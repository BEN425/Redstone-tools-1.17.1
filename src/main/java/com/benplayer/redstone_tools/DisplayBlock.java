package com.benplayer.redstone_tools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class DisplayBlock {
    public static void display(MatrixStack matrices, MinecraftClient client, float tickDelta, final int BLOCK_Y)  {
        if (client.getCameraEntity() == null || client.getWindow() == null) return;

        int width = client.getWindow().getScaledWidth() - 5;
        TextRenderer renderer = client.textRenderer;
        World world = client.world;

        HitResult result = client.getCameraEntity().raycast(20, tickDelta, false);
        if (result.getType() != HitResult.Type.BLOCK) return;

        BlockPos pos = ((BlockHitResult) result).getBlockPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Coordinate
        String coor = String.format("(%d, %d, %d)", pos.getX(), pos.getY(), pos.getZ());
        renderer.drawWithShadow(matrices, coor,
            width-renderer.getWidth(coor), BLOCK_Y, -1
        );
        // Block name
        renderer.drawWithShadow(matrices, block.getName(),
            width-renderer.getWidth(block.getName()), BLOCK_Y+10, -1
        );
        // Block ID
        renderer.draw(matrices, Registry.BLOCK.getId(block).toString(),
            width-renderer.getWidth(Registry.BLOCK.getId(block).toString()), BLOCK_Y+20, 0xFFBEBEBE
        );

        // Hardness
        String hardness = String.valueOf(block.getHardness());
        renderer.drawWithShadow(matrices, "Hardness : ",
            width-renderer.getWidth("Hardness : ")-renderer.getWidth(hardness), BLOCK_Y+30, -1);
        renderer.drawWithShadow(matrices, hardness,
            width-renderer.getWidth(hardness), BLOCK_Y+30, 0xFF008000);

        // Blast resistance
        String blastRes = String.valueOf(block.getBlastResistance());
        renderer.drawWithShadow(matrices, "Blast Res : ",
            width-renderer.getWidth("Blast Res : ")-renderer.getWidth(blastRes), BLOCK_Y+40, -1);
        renderer.drawWithShadow(matrices, blastRes,
            width-renderer.getWidth(blastRes), BLOCK_Y+40, 0xFF008000);

        int yOffset = 50;

        // Redstone signal
        if (state.contains(Properties.POWER)) {
            String signal = String.valueOf(state.get(Properties.POWER));
            renderer.drawWithShadow(matrices, signal,
                width-renderer.getWidth(signal), BLOCK_Y+yOffset, 0xFFAA0000
            );
            renderer.drawWithShadow(matrices, "Signal : ",
                width-renderer.getWidth("Signal : ")-renderer.getWidth(signal), BLOCK_Y+yOffset, -1
            );

            yOffset += 10;
        }
        else if (block == Blocks.COMPARATOR) {
            String signal = String.valueOf(state.getComparatorOutput(world, pos));
            renderer.drawWithShadow(matrices, signal,
                width-renderer.getWidth(signal), BLOCK_Y+yOffset, 0xFFAA0000
            );
            renderer.drawWithShadow(matrices, "Signal : ",
                width-renderer.getWidth("Signal : ")-renderer.getWidth(signal), BLOCK_Y+yOffset, -1
            );

            yOffset += 10;
        }

        // Redstone ON/OFF


        // yOffset += 10;
    }
}
