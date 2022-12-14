package me.kalmemarq.hjnutility.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HJNButton extends ButtonWidget {
    protected static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    protected static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    protected int iconU;
    protected int iconV;

    protected HJNButton(int x, int y, int width, int iconU, int iconV, PressAction onPress) {
        this(x, y, width, iconU, iconV, Text.empty(), onPress);
    }

    protected HJNButton(int x, int y, int width, int iconU, int iconV, Text text, PressAction onPress) {
        super(x, y, width, 18, text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.iconU = iconU;
        this.iconV = iconV;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, BG_NS_INFO, 0xFF000000);
        if (this.isHovered()) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 16, 0, 16, 16, BG_NS_INFO);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        drawTexture(matrices, this.getX() + this.getWidth() / 2 - 8, this.getY() + this.getHeight() / 2 - 8, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256);
    }
}
