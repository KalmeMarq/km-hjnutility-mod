package me.kalmemarq.hjnutility.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    private static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    private static final Identifier HJN_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    private static final ItemStack COMPASS = new ItemStack(Items.COMPASS);

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledHeight;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int scaledWidth;

    @Shadow @Final private static Identifier WIDGETS_TEXTURE;

    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

    @Shadow public abstract void tick(boolean paused);

    @Shadow private int ticks;

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void kmhjnutility$hideShowMobEffectsOverlay(MatrixStack matrices, CallbackInfo ci) {
        if (HJNUtilityMod.config != null && HJNUtilityMod.config.modules.hideMobEffects) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void kmhjnutility$hideShowVignetteOverlay(Entity entity, CallbackInfo ci) {
        if (HJNUtilityMod.config != null && HJNUtilityMod.config.modules.hideVignette) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void kmhjnutility$renderMainHandItemIDAndCompass(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();

        if (player != null && HJNUtilityMod.config != null) {
            if (HJNUtilityMod.config.modules.showCompass) {
                int y = scaledHeight - 2 - 14 - 2;
                if (HJNUtilityMod.config.modules.showItemId) {
                    y -= 18;
                }

                RenderSystem.setShaderTexture(0, HJN_TEXTURE);
                RenderUtil.drawColoredNinesliceTexture(matrices, 2, y, 20, 18, 0, 0, 16, 16, BG_NS_INFO, 0xAA_000000);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                renderHotbarItem(4, y, tickDelta, player, COMPASS, 12);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            ItemStack mainHand = player.getMainHandStack();

            boolean showID = !mainHand.isEmpty();
            if (showID) {
                Item item = mainHand.getItem();

                showID =  HJNUtilityMod.config.modules.showItemId;
                if (showID) {
                    Identifier itemID = Registries.ITEM.getId(item);

                    String text = itemID.toString();
                    int w = getTextRenderer().getWidth(text);

                    RenderSystem.setShaderTexture(0, HJN_TEXTURE);
                    RenderUtil.drawColoredNinesliceTexture(matrices, 2, this.scaledHeight - 2 - 14, w + 4, 14, 0, 0, 16, 16, BG_NS_INFO, 0xAA_000000);

                    RenderUtil.drawText(matrices, getTextRenderer(), Text.literal(text), 4, scaledHeight - 2 - 11, 0xFF_FFFFFF);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                }


                if (HJNUtilityMod.config.general.mainHandSlot) {
                    Arm arm = player.getMainArm();
                    int i = scaledWidth / 2;

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                    RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

                    int j = this.getZOffset();
                    this.setZOffset(-90);

                    if (arm == Arm.LEFT) {
                        this.drawTexture(matrices, i - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                    } else {
                        this.drawTexture(matrices, i + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                    }

                    setZOffset(j);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();

                    if (!mainHand.isEmpty()) {
                        int n = this.scaledHeight - 16 - 3;
                        if (arm == Arm.LEFT) {
                            renderHotbarItem(i - 91 - 26, n, tickDelta, player, mainHand, 11);
                        } else {
                            renderHotbarItem(i + 91 + 10, n, tickDelta, player, mainHand, 1);
                        }

                        RenderSystem.enableBlend();
                        RenderSystem.defaultBlendFunc();
                    }
                }
            }
        }
    }

    // HJNUtilityMod.config.general.offHandSlot && !instance.isEmpty()
    @Redirect(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean kmhjnutility$hideShowOffHandSlot(ItemStack instance) {
        return HJNUtilityMod.config == null ? instance.isEmpty() : !(HJNUtilityMod.config.general.offHandSlot && !instance.isEmpty());
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/BossBarHud;render(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    private void kmhjnutility$hideShowBossBarHud(BossBarHud instance, MatrixStack matrices) {
        if (HJNUtilityMod.config == null || !HJNUtilityMod.config.modules.hideBossBars) {
            instance.render(matrices);
        }
    }

    private static final Text STATUS_JUMPING = Text.literal("Jumping");
    private static final Text STATUS_SNEAKING = Text.literal("Sneaking");
    private static final Text STATUS_WALKING = Text.literal("Walking");
    private static final Text STATUS_SWIMMING = Text.literal("Swimming");
    private static final Text STATUS_SPRINTING = Text.literal("Sprinting");
    private static final Text[] STATUS_TEXTS = new Text[]{ STATUS_WALKING, STATUS_SNEAKING, STATUS_JUMPING, STATUS_SWIMMING };

    @Inject(method = "render", at = @At("TAIL"))
    private void kmhjnutility$renderPaperDollArmorHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();

        if (player != null && HJNUtilityMod.config != null && HJNUtilityMod.config.general.statusHud) {
            boolean isJumping = !player.isOnGround() && !player.isFallFlying();
            boolean isSneaking = player.isSneaking();
            boolean isWalking = false;
            boolean isSwimming = player.isSwimming();
            boolean isSprinting = player.isSprinting();
            boolean[] bools = new boolean[]{ isWalking || isSprinting, isSneaking, isJumping, isSwimming };

            int maxWidth = 0;

            for (int i = 0; i < bools.length; i++) {
                int w = getTextRenderer().getWidth(i == 0 && isSprinting ? STATUS_SPRINTING : STATUS_TEXTS[i]) + 8;
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

                fill(matrices, 1, 1 + (j * 10), 1 + maxWidth, 1 + 10 + (j * 10), 0xAA_000000);

                RenderUtil.drawCenteredText(matrices, getTextRenderer(), i == 0 && isSprinting ? STATUS_SPRINTING : STATUS_TEXTS[i], 1 + maxWidth / 2, 2 + (j * 10), 0xFF_FFFFFF);
            }
        }

        if (player != null && HJNUtilityMod.config != null && HJNUtilityMod.config.general.armorHud) {
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

                int x = scaledWidth / 2 + 91 + 10;
                int y = scaledHeight - 22 - 4 - (18 * 4) - 4 - 2;
                int w = 16 + 4;
                int h = (18 * 4) + 4 + 2;

                RenderSystem.setShaderTexture(0, HJN_TEXTURE);
                RenderUtil.drawColoredNinesliceTexture(matrices, x, y, w, h, 0, 0, 16, 16, BG_NS_INFO, 0xAA_000000);

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                armors = player.getArmorItems().iterator();

                int i = -1;
                while (armors.hasNext()) {
                    ItemStack stack = armors.next();
                    ++i;
                    if (stack.isEmpty()) continue;
                    renderHotbarItem(x + 2, y + 4 + ((3 - i) * 18), tickDelta, player, stack, 13 + i);
                }
            }
        }
    }

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void kmhjnutility$renderCrosshair(InGameHud instance, MatrixStack matrixStack, int i0, int i1, int i2, int i3, int i4, int i5) {
        if (HJNUtilityMod.config.crosshairs.modifier == HJNConfig.CrosshairModifier.Inverted && HJNUtilityMod.config.crosshairs.crosshairIndex == 0) {
            instance.drawTexture(matrixStack, i0, i1, i2, i3, i4, i5);
        } else {
            int crosshairIndex = HJNUtilityMod.config.crosshairs.crosshairIndex;
            HJNCrosshair crosshair = HJNCrosshair.CROSSHAIRS.get(crosshairIndex);
            HJNConfig.CrosshairModifier modifier = HJNUtilityMod.config.crosshairs.modifier;

            int x = (this.scaledWidth - crosshair.getWidth()) / 2;
            int y = (this.scaledHeight - crosshair.getHeight()) / 2;

            if (modifier == HJNConfig.CrosshairModifier.Inverted) {
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
                RenderSystem.setShaderTexture(0, HJN_TEXTURE);
                drawTexture(matrixStack, x, y, crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight());
            } else {
                RenderSystem.defaultBlendFunc();

                int color;
                if (modifier == HJNConfig.CrosshairModifier.Chroma) {
                    color = RenderUtil.hslToRgb((float)this.ticks * 6.0f, 0.7f, 0.6f);
                } else {
                    color = HJNCrosshair.CROSSHAIR_COLOR.getOrDefault(modifier, 0xFF_FFFFFF);
                }

                RenderSystem.setShaderTexture(0, crosshairIndex == 0 ? GUI_ICONS_TEXTURE : HJN_TEXTURE);
                RenderUtil.drawColoredTexture(matrixStack, x, y, getZOffset(), crosshair.getWidth(), crosshair.getHeight(), crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight(), 256, 256, color);
            }

            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
        }
    }
}
