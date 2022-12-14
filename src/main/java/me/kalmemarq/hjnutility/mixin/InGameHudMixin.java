package me.kalmemarq.hjnutility.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.gui.RenderUtil;
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
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.Target;
import java.rmi.registry.Registry;

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
                int y = scaledHeight - 2 - 14;
                if (HJNUtilityMod.config.modules.showItemID) {
                    y -= 20;
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

            if (!mainHand.isEmpty()) {
                Item item = mainHand.getItem();

                if (HJNUtilityMod.config.modules.showItemID) {
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

    @Inject(method = "render", at = @At("TAIL"))
    private void kmhjnutility$renderPaperDollArmorHud(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        // TODO
    }
}
