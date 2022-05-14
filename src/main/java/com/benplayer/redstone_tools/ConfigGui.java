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
    // Tooltips
    private final static TranslatableText noClipTooltip = new TranslatableText("gui.redstone_tools.noclip_tooltip");
    private final static TranslatableText highSpeedTooltip = new TranslatableText("gui.redstone_tools.high_speed_tooltip");
    private final static TranslatableText nightVisionTooltip = new TranslatableText("gui.redstone_tools.night_vision_tooltip");
    private final static TranslatableText placeRedstoneTooltip1 = new TranslatableText("gui.redstone_tools.place_redstone_tooltip1");
    private final static TranslatableText placeRedstoneTooltip2 = new TranslatableText("gui.redstone_tools.place_redstone_tooltip2");
    private final static TranslatableText instantKillTooltip = new TranslatableText("gui.redstone_tools.instant_kill_tooltip");
    private final static TranslatableText instantBreakTooltip = new TranslatableText("gui.redstone_tools.instant_break_tooltip");

    public ConfigGui() {
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        if (server == null || MinecraftClient.getInstance().player == null) return;
        PlayerEntity player = server.getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid());
        if (player == null) return;

        // Root
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 45);
        root.setInsets(Insets.ROOT_PANEL);

        // Title label
        WLabel title = new WLabel(new TranslatableText(
            "gui.redstone_tools.config"
        ));
        root.add(title, 0, 0, 4, 2);

        // Buttons
        addNoClipButton(root);
        addHighSpeedButton(root);
        addNightVisionButton(root, player);
        addRedstoneButton(root);
        addInstantKillButton(root, player);
        addInstantBreakButton(root);

        // Change speed text field
        setSpeedText(root);

        root.validate(this);
    }

    private void addNoClipButton(WGridPanel root) {
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
    }

    private void addHighSpeedButton(WGridPanel root) {
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
    }

    private void addNightVisionButton(WGridPanel root, PlayerEntity player) {
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
    }

    private void addRedstoneButton(WGridPanel root) {
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
    }

    private void addInstantKillButton(WGridPanel root, PlayerEntity player) {
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
    }

    private void addInstantBreakButton(WGridPanel root) {
        WToggleButton instantBreakButton = new WToggleButton(new TranslatableText(
            "gui.redstone_tools.instant_break"
        )) {
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(instantBreakTooltip);
            }
        };

        instantBreakButton.setToggle(Redstone_toolsClient.instantBreak);
        instantBreakButton.setOnToggle(on ->
            Redstone_toolsClient.instantBreak = !Redstone_toolsClient.instantBreak
        );

        root.add(instantBreakButton, 0, 6, 4, 2);
    }

    private void setSpeedText(WGridPanel root) {
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
    }
}