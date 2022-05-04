package com.benplayer.redstone_tools.client;

import com.benplayer.redstone_tools.keybindings.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Redstone_toolsClient implements ClientModInitializer {
    // Default high speed
    public static final float defFlySpeed = 0.4f;

    public static boolean noClip = false;
    public static boolean breakDelay = true;
    public static boolean highSpeed = false;
    public static float flSpeed = defFlySpeed;
    public static boolean placeRedstone = false;

    @Override
    public void onInitializeClient() {
        /* Register keyBindings */

        LazyKey.register();
        ConfigKey.register();
        NoClipKey.register();
        SpeedChangeKey.register();
        NightVisionKey.register();
        PlaceRedstoneKey.regsiter();
        InstantKillKey.register();

    }
}
