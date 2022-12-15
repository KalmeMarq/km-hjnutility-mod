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
import net.minecraft.util.math.MathHelper;

public class HJNToggleButton extends ButtonWidget {
    protected static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    private boolean toggled;
    protected int iconU;
    protected int iconV;

    protected HJNToggleButton(int x, int y, int width, int height, Text text, int iconU, int iconV, PressAction onPress) {
        super(x, y, width, height, text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.iconU = iconU;
        this.iconV = iconV;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.isHovered() ? 0xAA_025602 : 0xAA_000000);

        if (this.toggled) {
            fill(matrices, this.getX(), this.getY() + this.getHeight() - 1, this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xFF_FFFFFF);
        };

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        drawTexture(matrices, this.getX() + this.getWidth() / 2 - 8, this.getY() + this.getHeight() / 2 - 8 - 6, (float) this.iconU, (float) this.iconV, 16, 16, 256, 256);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2 + 11, 0xFF_FFFFFF);
    }
}
