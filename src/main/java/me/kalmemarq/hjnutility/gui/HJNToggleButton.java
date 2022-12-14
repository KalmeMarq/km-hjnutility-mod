package me.kalmemarq.hjnutility.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class HJNToggleButton extends HJNButton {
    private boolean toggled;

    protected HJNToggleButton(int x, int y, int width, int iconU, int iconV, PressAction onPress) {
        super(x, y, width, iconU, iconV, onPress);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        if (this.isHovered() || this.toggled) {
            fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF_000000);
        } else {
            fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF_000000);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        drawTexture(matrices, this.getX() + this.getWidth() / 2 - 8, this.getY() + this.getHeight() / 2 - 8, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256);


    }
}
