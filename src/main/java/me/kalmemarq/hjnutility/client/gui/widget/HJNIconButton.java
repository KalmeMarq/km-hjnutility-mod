package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HJNIconButton extends ButtonWidget implements HJNWidget {
    private int iconU;
    private int iconV;
    private int iconW;
    private int iconH;

    public HJNIconButton(int x, int y, int width, int height, int iconU, int iconV, int iconW, int iconH, PressAction onPress) {
        super(x, y, width, height, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.iconU = iconU;
        this.iconV = iconV;
        this.iconW = iconW;
        this.iconH = iconH;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF000000);
        if (this.isHovered()) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 16, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        drawTexture(matrices, this.getX() + (this.getWidth() - iconW) / 2, this.getY() + (this.getHeight() - iconH)/ 2, (float) this.iconU, (float) this.iconV, this.iconW, this.iconH, 256, 256);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
