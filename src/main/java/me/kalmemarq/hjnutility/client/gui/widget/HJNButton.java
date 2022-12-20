package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HJNButton extends ButtonWidget implements HJNWidget {
    public HJNButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 80, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF000000);
        if (this.isHovered()) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 16, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, 0xFF_FFFFFF);
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }
}
