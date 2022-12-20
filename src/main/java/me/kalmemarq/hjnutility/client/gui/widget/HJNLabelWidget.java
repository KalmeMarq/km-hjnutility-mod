package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class HJNLabelWidget extends ClickableWidget implements HJNWidget {
    private TextAlignment alignment = TextAlignment.CENTER;
    private int textWidth;

    public HJNLabelWidget(TextRenderer textRenderer, Text message) {
        super(0, 0, 0, 9, message);
        this.textWidth = textRenderer.getWidth(getMessage());
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void setHeight(int height) {
    }

    @Override
    public int getDefaultSize() {
        return textWidth;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int tx = getX();
        int ty = getY();

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        if (getWidth() != getDefaultSize()) {
            int tW = textRenderer.getWidth(getMessage());

            if (alignment == TextAlignment.CENTER) {
                tx += (getWidth() - tW) / 2;
            } else if (alignment == TextAlignment.RIGHT) {
                tx += getWidth() - tW;
            }
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        drawTextWithShadow(matrices, textRenderer, this.getMessage(), tx, ty, 0xFF_FFFFFF);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT
    }
}
