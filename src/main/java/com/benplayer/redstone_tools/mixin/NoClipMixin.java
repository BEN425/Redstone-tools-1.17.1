package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 *  Mixin injected function
 *  Make player able to noClip
 */
@Mixin(PlayerEntity.class)
public class NoClipMixin {
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;updateWaterSubmersionState()Z",
            shift = At.Shift.BEFORE
        )
    )
    private void injected(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSpectator() || Redstone_toolsClient.noClip) {
            player.noClip = true;
            player.setOnGround(false);
        }
    }

    @ModifyVariable(
        method = "updatePose",
        at = @At("STORE"),
        ordinal = 1
    )
    private EntityPose injected2(EntityPose pose) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        return (player.isCreative() &&
            Redstone_toolsClient.noClip &&
            pose == EntityPose.SWIMMING) ? EntityPose.STANDING : pose;
    }
}