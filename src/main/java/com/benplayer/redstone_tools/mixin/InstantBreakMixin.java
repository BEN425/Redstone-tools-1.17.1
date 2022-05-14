package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientPlayerInteractionManager.class)
public class InstantBreakMixin {

    @ModifyConstant(
        method = "updateBlockBreakingProgress",
        constant = @Constant(intValue = 5)
    )
    private int injected1(int value) {
        return Redstone_toolsClient.instantBreak ? 0 : value;
    }
}
