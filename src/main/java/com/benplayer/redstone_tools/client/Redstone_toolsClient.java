package com.benplayer.redstone_tools.client;

import com.benplayer.redstone_tools.keybindings.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Redstone_toolsClient implements ClientModInitializer {
    public static Boolean noClip = false;
    public static Boolean breakDelay = true;
    public static Boolean highSpeed = false;
    public static Boolean placeRedstone = false;

    @Override
    public void onInitializeClient() {
        /* Register keyBindings */

        LazyKey.register();
        ConfigKey.register();
        NoClipKey.register();
        SpeedChangeKey.register();
        NightVisionKey.register();
        PlaceRedstoneKey.regsiter();

    }
}
