package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *  Mixin injected function
 *  Make player able to change walking speed
 */
@Mixin(PlayerEntity.class)
public class WalkSpeedMixin {

    // Change walking speed
    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void injected(@NotNull CallbackInfoReturnable<Float> info) {
        if (Redstone_toolsClient.highSpeed) {
            info.setReturnValue(0.6f);
            info.cancel();
        }
    }
}
