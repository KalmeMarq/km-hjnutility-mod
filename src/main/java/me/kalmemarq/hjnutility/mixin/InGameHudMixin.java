package me.kalmemarq.hjnutility.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.client.gui.HJNInGameHud;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
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
    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledHeight;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int scaledWidth;

    @Shadow private int ticks;

    @Shadow @Final private ItemRenderer itemRenderer;

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
    private void kmhjnutility$render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        HJNInGameHud.render$((InGameHud)(Object)this, this.ticks, this.scaledWidth, this.scaledHeight, this.getCameraPlayer(), matrices, tickDelta, this.itemRenderer);
    }

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0))
    private void kmhjnutility$renderCrosshair(InGameHud instance, MatrixStack matrixStack, int i0, int i1, int i2, int i3, int i4, int i5) {
        if (HJNUtilityMod.config.crosshairs.modifier == HJNConfig.CrosshairModifier.Inverted && HJNUtilityMod.config.crosshairs.crosshairIndex == 0) {
            instance.drawTexture(matrixStack, i0, i1, i2, i3, i4, i5);
        } else {
            HJNInGameHud.renderCrosshair$((InGameHud) (Object)this, this.ticks, this.scaledWidth, this.scaledHeight, matrixStack);
        }
    }
}
