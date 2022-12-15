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

public class HJNThemeToggleButton extends ButtonWidget {
    protected static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    protected static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    protected static final Identifier THEME_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_themes.png");
    private boolean toggled;
    protected int bgU;
    protected int bgV;
    protected int iconU;
    protected int iconV;

    protected HJNThemeToggleButton(int x, int y, Text text, int bgU, int bgV, int iconU, int iconV, ButtonWidget.PressAction onPress) {
        super(x, y, 82, 66, text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.bgU = bgU;
        this.bgV = bgV;
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

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, BG_NS_INFO, 0xFF000000);

        if (this.isHovered() || this.toggled) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 48, 0, 16, 16, BG_NS_INFO);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, THEME_TEXTURE);
        drawTexture(matrices, this.getX() + 3, this.getY() + 3, this.getWidth() - 6, this.getHeight() - 6, this.bgU, this.bgV, 387, 287, 1024, 1024);
//        drawTexture(matrices, this.getX() + 3, this.getY() + 3, -1, -1, 16, 16, 60, 51, 1024, 1024);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2 + 20, 0xFF_FFFFFF);
    }
}
