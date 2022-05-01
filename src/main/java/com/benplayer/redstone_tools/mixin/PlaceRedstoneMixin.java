package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *  Mixin injected function
 *  Make an extra redstone dust automatically placed
 *  while the player is placing a solid block
 */
@Mixin(Block.class)
public class PlaceRedstoneMixin {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    private void injected(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack, CallbackInfo info) {
        if (placer == null || !placer.isPlayer()) return;

        PlayerEntity player = (PlayerEntity) placer;
        // Player is in creative mode
        // Toggle placing redstone
        // The placed block is a solid block
        // The pos above the block is air
        if (!player.isCreative() ||
            !Redstone_toolsClient.placeRedstone ||
            !state.isSolidBlock(world, pos) ||
            !world.getBlockState(pos.add(0, 1, 0)).isAir())
            return;

        // Place a redstone dust
        world.setBlockState(
            pos.add(0, 1, 0),
            Blocks.REDSTONE_WIRE.getDefaultState());
    }
}
