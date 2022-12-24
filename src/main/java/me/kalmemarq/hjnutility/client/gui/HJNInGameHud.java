package me.kalmemarq.hjnutility.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class HJNInGameHud {
    private static final Identifier WIDGETS_TEXTURE = new Identifier("textures/gui/widgets.png");
    private static final Identifier GUI_ICONS_TEXTURE = new Identifier("textures/gui/icons.png");

    private static final ItemStack COMPASS = new ItemStack(Items.COMPASS);

    // TODO: Localize status texts
    private static final Text STATUS_SPECTATING = Text.literal("Spectating");
    private static final Text STATUS_JUMPING = Text.literal("Jumping");
    private static final Text STATUS_SNEAKING = Text.literal("Sneaking");
    private static final Text STATUS_WALKING = Text.literal("Walking");
    private static final Text STATUS_SWIMMING = Text.literal("Swimming");
    private static final Text STATUS_SPRINTING = Text.literal("Sprinting");
    private static final Text STATUS_CRAWLING = Text.literal("Crawling");
    private static final Text STATUS_DRIVING = Text.literal("Driving");
    private static final Text[] STATUS_TEXTS = new Text[]{ STATUS_WALKING, STATUS_SNEAKING, STATUS_JUMPING, STATUS_SWIMMING, STATUS_CRAWLING, STATUS_SPECTATING, STATUS_DRIVING };

    public static void render$(InGameHud hud, int ticks, int width, int height, @Nullable PlayerEntity player, MatrixStack matrices, float tickDelta, ItemRenderer itemRenderer) {
        renderStatus(player, matrices);
        renderItemIdCompass(height, tickDelta, player, matrices, itemRenderer);
        renderMainHand(player, width, height, matrices, itemRenderer, tickDelta);
        renderArmorHud(player, width, height, matrices, itemRenderer, tickDelta);
    }

    private static void renderArmorHud(@Nullable PlayerEntity player, int width, int height, MatrixStack matrices, ItemRenderer itemRenderer, float tickDelta) {
        if (player == null) return;

        if (HJNUtilityMod.config.general.armorHud) {
            Iterator<ItemStack> armors = player.getArmorItems().iterator();

            boolean hasArmor = false;
            while (armors.hasNext()) {
                if (!armors.next().isEmpty()) {
                    hasArmor = true;
                    break;
                }
            }

            if (hasArmor) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                int x = width / 2 + 91 + 10 - 2;
                int y = height - 22 - 4 - (18 * 4) - 4 - 2;
                int w = 16 + 4;
                int h = (18 * 4) + 4 + 2;

                if (!HJNUtilityMod.config.general.mainHandSlot || player.getMainHandStack().isEmpty()) {
                    y += 24;
                }

                RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
                RenderUtil.drawColoredNinesliceTexture(matrices, x, y, w, h, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xAA_000000);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                armors = player.getArmorItems().iterator();

                int i = -1;
                while (armors.hasNext()) {
                    ItemStack stack = armors.next();
                    ++i;
                    if (stack.isEmpty()) continue;
                    renderItem(x + 2, y + 4 + ((3 - i) * 18), itemRenderer, tickDelta, player, stack, 13 + i);
                }
            }
        }
    }

    private static void renderMainHand(@Nullable PlayerEntity player, int width, int height, MatrixStack matrices, ItemRenderer itemRenderer, float tickDelta) {
        if (player == null) return;

        if (HJNUtilityMod.config.general.mainHandSlot) {
            Arm arm = player.getMainArm();

            int xm = width / 2;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

            int ax = arm == Arm.LEFT ? xm - 91 - 9 : xm + 91;
            int au = arm == Arm.LEFT ? 24 : 53;
            RenderUtil.drawTexture(matrices, ax, height - 23, -90, 29, 24, au, 22, 29, 24, 256, 256);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ItemStack mainHandStack = player.getMainHandStack();

            if (!mainHandStack.isEmpty()) {
                int iy = height - 16 - 3;

                if (arm == Arm.LEFT) {
                    renderItem(xm - 91 - 26, iy, itemRenderer, tickDelta, player, mainHandStack, 11);
                } else {
                    renderItem(xm + 91 + 10, iy, itemRenderer, tickDelta, player, mainHandStack, 1);
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
            }
        }
    }

    private static void renderItemIdCompass(int height, float tickDelta, @Nullable PlayerEntity player, MatrixStack matrices, ItemRenderer itemRenderer) {
        if (player == null) return;

        if (HJNUtilityMod.config.modules.showCompass) {
            int y = height - 2 - 14 - 2 - 2;
            if (HJNUtilityMod.config.modules.showItemId) {
                y -= 16;
            }

            RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
            RenderUtil.drawColoredNinesliceTexture(matrices, 2, y, 20, 18, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xAA_000000);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            renderItem(4, y + 1, itemRenderer, tickDelta, player, COMPASS, 12);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }

        if (HJNUtilityMod.config.modules.showItemId) {
            ItemStack mainHandStack = player.getMainHandStack();

            if (!mainHandStack.isEmpty()) {
                Identifier itemID = Registries.ITEM.getId(mainHandStack.getItem());

                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

                String text = itemID.toString();
                int w = textRenderer.getWidth(text);

                RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);


                RenderUtil.drawColoredNinesliceTexture(matrices, 2, height - 2 - 14, w + 4, 14, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xAA_000000);
                RenderUtil.drawText(matrices, textRenderer, Text.literal(text), 4, height - 2 - 11, 0xFF_FFFFFF);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
            }
        }
    }

    private static void renderStatus(@Nullable PlayerEntity player, MatrixStack matrices) {
        if (player == null || !HJNUtilityMod.config.general.statusHud) return;

        boolean isSneaking = player.isSneaking();
        boolean isWalking = false;
        boolean isSwimming = player.isSwimming();
        boolean isSprinting = player.isSprinting();
        boolean isCrawling = player.isCrawling();
        boolean isSpectator = player.isSpectator();
        boolean isJumping = !player.isOnGround() && !player.isFallFlying() && !isSpectator;
        boolean isDriving = player.getVehicle() != null;
        boolean[] bools = new boolean[]{ isWalking || isSprinting, isSneaking, isJumping, isSwimming, isCrawling, isSpectator, isDriving };

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxWidth = 0;

        for (int i = 0; i < bools.length; i++) {
            int w = textRenderer.getWidth(i == 0 && isSprinting ? STATUS_SPRINTING : STATUS_TEXTS[i]) + 8;
            if (w > maxWidth) {
                maxWidth = w;
            }
        }

        int j = -1;
        for (int i = 0; i < bools.length; i++) {
            if (!bools[i]) {
                continue;
            }
            ++j;

            RenderUtil.fill(matrices, 1, 1 + (j * 10), maxWidth, 10, 0xAA_000000);
            RenderUtil.drawCenteredText(matrices, textRenderer, i == 0 && isSprinting ? STATUS_SPRINTING : STATUS_TEXTS[i], 1 + maxWidth / 2, 2 + (j * 10), 0xFF_FFFFFF);
        }
    }

    private static void renderPaperDoll() {

    }

    public static void renderCrosshair$(InGameHud hud, int ticks, int width, int height, MatrixStack matrices) {
        int crosshairIndex = HJNUtilityMod.config.crosshairs.crosshairIndex;
        HJNCrosshair crosshair = HJNCrosshair.CROSSHAIRS.get(crosshairIndex);
        HJNConfig.CrosshairModifier modifier = HJNUtilityMod.config.crosshairs.modifier;

        int x = (width - crosshair.getWidth()) / 2;
        int y = (height - crosshair.getHeight()) / 2;

        if (modifier == HJNConfig.CrosshairModifier.Inverted) {
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
            RenderUtil.drawTexture(matrices, x, y, crosshair.getWidth(), crosshair.getHeight(), crosshair.getU(), crosshair.getV());
        } else {
            RenderSystem.defaultBlendFunc();

            int color;
            if (modifier == HJNConfig.CrosshairModifier.Chroma) {
                color = RenderUtil.hslToRgb((float)ticks * 6.0f, 0.7f, 0.6f);
            } else {
                color = HJNCrosshair.CROSSHAIR_COLOR.getOrDefault(modifier, 0xFF_FFFFFF);
            }

            RenderSystem.setShaderTexture(0, crosshairIndex == 0 ? GUI_ICONS_TEXTURE : HJNUtilityMod.HJN_TEXTURE);
            RenderUtil.drawColoredTexture(matrices, x, y, 0, crosshair.getWidth(), crosshair.getHeight(), crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight(), 256, 256, color);
        }

        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
    }

    private static void renderItem(int x, int y, ItemRenderer itemRenderer, float tickDelta, PlayerEntity player, ItemStack stack, int seed) {
        if (!stack.isEmpty()) {
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            float f = (float)stack.getBobbingAnimationTime() - tickDelta;
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                matrixStack.push();
                matrixStack.translate((float)(x + 8), (float)(y + 12), 0.0F);
                matrixStack.scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
                RenderSystem.applyModelViewMatrix();
            }

            itemRenderer.renderInGuiWithOverrides(player, stack, x, y, seed);
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            if (f > 0.0F) {
                matrixStack.pop();
                RenderSystem.applyModelViewMatrix();
            }

            itemRenderer.renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, stack, x, y);
        }
    }
}
