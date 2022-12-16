package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HJNTabButton extends ButtonWidget {
    private boolean selected;
    private final int iconU, iconV;

    public HJNTabButton(int width, int iconU, int iconV, PressAction onPress) {
        this(0, 0, width, iconU, iconV, onPress);
    }

    public HJNTabButton(int x, int y, int width, int iconU, int iconV, PressAction onPress) {
        super(x, y, width, 18, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.iconU = iconU;
        this.iconV = iconV;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF_000000);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        drawTexture(matrices, this.getX() + this.getWidth() / 2 - 8, this.getY() + this.getHeight() / 2 - 8, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256);

        if (this.isHovered()) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 32, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO);
        }

        if (this.selected && !this.isHovered()) {
            fill(matrices, this.getX(), this.getY() + this.getHeight() - 1, this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF_FFFFFF);
        }
    }
}
