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

public class HJNToggle extends ButtonWidget {
    protected static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    protected static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    private boolean toggled;

    protected HJNToggle(int x, int y, int width, int height, Text text, ButtonWidget.PressAction onPress) {
        super(x, y, width, height, text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
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

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, BG_TEXTURE);

        int c = isHovered() || this.toggled ? 0xFF_FFFFFF : 0xFF000000;
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), 15, 15, 0, 0, 16, 16, BG_NS_INFO, c);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        if (this.toggled) RenderUtil.drawCenteredText(matrices, textRenderer, Text.literal("+"), (int)(this.getX() + 15 / 2.0f) + 1, this.getY() + (this.getHeight() - 8) / 2 + 1, 0xFF_000000);

        RenderUtil.drawText(matrices, textRenderer, this.getMessage(), this.getX() + 15 + 6, this.getY() + (this.getHeight() - 8) / 2, 0xFF_FFFFFF);
    }
}
