package com.benplayer.redstone_tools;

import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

// Config GUI

public class ConfigGui extends LightweightGuiDescription {
    public ConfigGui() {
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        if (server == null || MinecraftClient.getInstance().player == null) return;
        PlayerEntity player = server.getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid());
        if (player == null) return;

        // Tooltips

        TranslatableText noClipTooltip = new TranslatableText(
            "gui.redstone_tools.noclip_tooltip"
        );
        TranslatableText highSpeedTooltip = new TranslatableText(
            "gui.redstone_tools.high_speed_tooltip"
        );
        TranslatableText nightVisionTooltip = new TranslatableText(
            "gui.redstone_tools.night_vision_tooltip"
        );
        TranslatableText placeRedstoneTooltip1 = new TranslatableText(
            "gui.redstone_tools.place_redstone_tooltip1"
        );
        TranslatableText placeRedstoneTooltip2 = new TranslatableText(
            "gui.redstone_tools.place_redstone_tooltip2"
        );
        TranslatableText instantKillTooltip = new TranslatableText(
            "gui.redstone_tools.instant_kill_tooltip"
        );

        // Root

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 50);
        root.setInsets(Insets.ROOT_PANEL);

        // Title label

        WLabel title = new WLabel(new TranslatableText(
            "gui.redstone_tools.config"
        ));
        root.add(title, 0, 0, 4, 2);

        // No Clip button

        WToggleButton noClipButton = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.noclip"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(noClipTooltip);
            }
        };

        noClipButton.setToggle(Redstone_toolsClient.noClip);
        noClipButton.setOnToggle(on ->
            Redstone_toolsClient.noClip = !Redstone_toolsClient.noClip
        );

        root.add(noClipButton, 0, 1, 4, 2);

        // High Speed button

        WToggleButton highSpeedButton = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.high_speed"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(highSpeedTooltip);
            }
        };

        highSpeedButton.setToggle(Redstone_toolsClient.highSpeed);
        highSpeedButton.setOnToggle(on ->
            Redstone_toolsClient.highSpeed = !Redstone_toolsClient.highSpeed
        );

        root.add(highSpeedButton, 0, 2, 4, 2);

        // Night Vision button

        WToggleButton nightVisionButton = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.night_vision"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(nightVisionTooltip);
            }
        };

        nightVisionButton.setToggle(player.hasStatusEffect(StatusEffects.NIGHT_VISION));
        nightVisionButton.setOnToggle(on -> {
            if (on)
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false, false
                ));
            else
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
        });

        root.add(nightVisionButton, 0, 3, 4, 2);

        // Place redstone key

        WToggleButton placeRedstoneButton = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.place_redstone"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(placeRedstoneTooltip1);
                tooltip.add(placeRedstoneTooltip2);
            }
        };

        placeRedstoneButton.setToggle(Redstone_toolsClient.placeRedstone);
        placeRedstoneButton.setOnToggle(on ->
            Redstone_toolsClient.placeRedstone = !Redstone_toolsClient.placeRedstone
        );

        root.add(placeRedstoneButton, 0, 4, 4, 2);

        // Set speed text

        WTextField setSpeedText = new WTextField(Text.of(String.valueOf(Redstone_toolsClient.defFlySpeed)));
        setSpeedText.setText(String.valueOf(Redstone_toolsClient.flSpeed));
        setSpeedText.setChangedListener(str -> { // Change flying speed
            try {
                if (!Redstone_toolsClient.highSpeed) return;

                float speed = Float.parseFloat(str);
                if (speed > 0 && speed < 5)
                    Redstone_toolsClient.flSpeed = speed;
                else
                    Redstone_toolsClient.flSpeed = Redstone_toolsClient.defFlySpeed;
            } catch (Exception e) {
                Redstone_toolsClient.flSpeed = Redstone_toolsClient.defFlySpeed;
            }
        });
        root.add(setSpeedText, 6, 2, 4, 2);

        // Instant kill
        WToggleButton instantKill = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.instant_kill"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(instantKillTooltip);
            }
        };

        instantKill.setToggle(player.hasStatusEffect(StatusEffects.STRENGTH));
        instantKill.setOnToggle(on -> {
            if (on)
                player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.STRENGTH, Integer.MAX_VALUE, Short.MAX_VALUE, false, false, false
                ));
            else
                player.removeStatusEffect(StatusEffects.STRENGTH);
        });

        root.add(instantKill, 0, 5, 4, 2);

        root.validate(this);
    }
}