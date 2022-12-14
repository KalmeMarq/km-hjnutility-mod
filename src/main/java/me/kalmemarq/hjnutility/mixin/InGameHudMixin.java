package me.kalmemarq.hjnutility.mixin;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract PlayerEntity getCameraPlayer();

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
    private void kmhjnutility$renderMainHandItemID(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        PlayerEntity player = this.getCameraPlayer();

        if (player != null) {

        }
    }
}
