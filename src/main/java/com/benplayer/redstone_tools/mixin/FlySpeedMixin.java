package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 *  Mixin injected function
 *  Make player able to change flying speed
 */
@Mixin(PlayerAbilities.class)
public class FlySpeedMixin {

    @Inject(method = "getFlySpeed", at = @At("HEAD"), cancellable = true)
    private void injected(CallbackInfoReturnable<Float> info) {
        if (Redstone_toolsClient.highSpeed) {
            info.setReturnValue(0.4f);
            info.cancel();
        }
    }
}
