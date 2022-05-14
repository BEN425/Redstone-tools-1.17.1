package com.benplayer.redstone_tools.mixin;

import com.benplayer.redstone_tools.DisplayBlock;
import com.benplayer.redstone_tools.client.Redstone_toolsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    private final int LEFT_X = 5;
    private final int COOR_Y = 5;
    private final int LIGHT_Y = COOR_Y + 30;
    private final int BIOME_Y = LIGHT_Y + 10;
    private final int ITEM_Y = BIOME_Y + 20;
    private final int BLOCK_Y = 25;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!Redstone_toolsClient.inGameHud) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null)
            return;

        PlayerEntity player = client.player;
        TextRenderer renderer = client.textRenderer;

        displayItem(matrices, renderer, player);
        displayCoordinate(matrices, renderer, player);
        displayLight(matrices, client);
        displayBiomes(matrices, client, player);
        displayFps(matrices, client);
        DisplayBlock.display(matrices, client, tickDelta, BLOCK_Y);
    }

    // Display player's coordinate
    private void displayCoordinate(MatrixStack matrices, TextRenderer renderer, PlayerEntity player) {
        renderer.drawWithShadow(matrices, "X : ", LEFT_X, COOR_Y, 0xFFFF0000);
        renderer.drawWithShadow(matrices,
            String.format("%.5f", player.getX()),
            LEFT_X+15, COOR_Y, -1
        );

        renderer.drawWithShadow(matrices, "Y : ", LEFT_X, COOR_Y+10, 0xFF00FF00);
        renderer.drawWithShadow(matrices,
            String.format("%.5f", player.getY()),
            LEFT_X+15, COOR_Y+10, -1
        );

        renderer.drawWithShadow(matrices, "Z : ", LEFT_X, COOR_Y+20, 0xFF0000FF);
        renderer.drawWithShadow(matrices,
            String.format("%.5f", player.getZ()),
            LEFT_X+15, COOR_Y+20, -1
        );
    }

    // Display player's biomes
    private void displayBiomes(MatrixStack matrices, MinecraftClient client, PlayerEntity player) {
        TextRenderer renderer = client.textRenderer;
        var biome = client.world.getBiomeKey(player.getBlockPos());
        if (biome.isEmpty()) return;
        String id = biome.get().getValue().toString();

        renderer.drawWithShadow(matrices, "Biome : ", LEFT_X, BIOME_Y, 0xFF00AA00);
        renderer.drawWithShadow(matrices, id, LEFT_X+35, BIOME_Y, -1);
    }

    // Display light level
    private void displayLight(MatrixStack matrices, MinecraftClient client) {
        TextRenderer renderer = client.textRenderer;

        renderer.drawWithShadow(matrices, "Light : ",
            LEFT_X, LIGHT_Y, 0xFFFFD700);
        renderer.drawWithShadow(matrices,
            String.valueOf(client.world.getLightLevel(LightType.BLOCK, client.player.getBlockPos())),
            LEFT_X+35, LIGHT_Y, -1);
    }

    // Display fps
    private void displayFps(MatrixStack matrices, MinecraftClient client) {
        TextRenderer renderer = client.textRenderer;

        try {
            int fps = Integer.parseInt(client.fpsDebugString.split("fps")[0].strip());
            // red : yellow : green
            int color = (fps < 30) ? 0xFFFF5555 :
                (fps < 60) ? 0xFFFFFF55 : 0xFF55FF55;
            String text = String.valueOf(fps);

            renderer.drawWithShadow(matrices, text,
                client.getWindow().getScaledWidth()-renderer.getWidth(text)-5, 5, color
            );
        } catch (NumberFormatException e) {}
    }

    // Display information of main hand item
    private void displayItem(MatrixStack matrices, TextRenderer renderer, PlayerEntity player) {
        ItemStack holdItem = player.getMainHandStack().copy();
        if (holdItem == ItemStack.EMPTY)
            return;

        NbtCompound nbt = holdItem.getNbt();

        // Name, ID
        renderer.drawWithShadow(matrices, holdItem.getName(), LEFT_X, ITEM_Y, -1);
        renderer.draw(matrices, holdItem.getItem().toString(), LEFT_X, ITEM_Y+10, 0xFFBEBEBE);

        if (nbt == null) return;
        int yOffset = 20;

        // Enchantment
        var enchList = EnchantmentHelper.fromNbt(holdItem.getEnchantments());
        if (!enchList.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> ench : enchList.entrySet()) {
                TranslatableText enchName = new TranslatableText(ench.getKey().getTranslationKey());

                renderer.drawWithShadow(matrices, enchName, LEFT_X, ITEM_Y+yOffset, 0xFFFF55FF);
                renderer.drawWithShadow(matrices, String.valueOf(ench.getValue()),
                    LEFT_X+renderer.getWidth(enchName)+5, ITEM_Y+yOffset, -1);

                yOffset += 10;
            }
        }

        // Stored enchantment
        var storeList = EnchantmentHelper.fromNbt(nbt.getList("StoredEnchantments", NbtElement.COMPOUND_TYPE));
        if (!storeList.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> ench : storeList.entrySet()) {
                TranslatableText enchName = new TranslatableText(ench.getKey().getTranslationKey());

                renderer.drawWithShadow(matrices, enchName, LEFT_X, ITEM_Y+yOffset, 0xFF55FFFF);
                renderer.drawWithShadow(matrices, String.valueOf(ench.getValue()),
                    LEFT_X+renderer.getWidth(enchName)+5, ITEM_Y+yOffset, -1);

                yOffset += 10;
            }
        }

        // Potion effect
        var effectList = nbt.getList("CustomPotionEffects", NbtElement.COMPOUND_TYPE);
        if (!effectList.isEmpty()) {
            for (NbtElement eff : effectList) {
                StatusEffectInstance effect = StatusEffectInstance.fromNbt((NbtCompound) eff);
                if (effect == null) return;

                TranslatableText name = new TranslatableText(effect.getTranslationKey());
                String lvl = String.valueOf(effect.getAmplifier()+1); // level = amplifier + 1

                renderer.drawWithShadow(matrices, name, LEFT_X, ITEM_Y+yOffset, 0xFF0000AA);
                renderer.drawWithShadow(matrices, lvl,
                    LEFT_X+renderer.getWidth(name)+5, ITEM_Y+yOffset, -1
                );
                renderer.drawWithShadow(matrices,
                    String.format("%.2fs", effect.getDuration() / 20.0),
                    LEFT_X+renderer.getWidth(name)+renderer.getWidth(lvl)+10, ITEM_Y+yOffset, 0xFF555555
                );

                yOffset += 10;
            }
        }

        // Attribute
        var attrList = nbt.getList("AttributeModifiers", NbtElement.COMPOUND_TYPE);
        if (!attrList.isEmpty()) {
            for (NbtElement attrNbt : attrList) {
                EntityAttributeModifier attr = EntityAttributeModifier.fromNbt((NbtCompound) attrNbt);
                if (attr == null) continue;

                String prefix = attr.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? "* " :
                                attr.getValue() >= 0 ? "+ " : " ";
                String postfix = attr.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE ? " %" : "";
                double value = attr.getValue() * (attr.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE ? 100 : 1);
                int color = (value >= 0) ? 0xFF5555FF : 0xFFFF5555;

                renderer.drawWithShadow(matrices, new TranslatableText(attr.getName()),
                    LEFT_X, ITEM_Y+yOffset, 0xFFFFAA00
                );
                renderer.drawWithShadow(matrices,
                    prefix + value + postfix,
                    LEFT_X+renderer.getWidth(attr.getName())+5, ITEM_Y+yOffset, color
                );

                yOffset += 10;
            }
        }
    }
}
