package me.kalmemarq.hjnutility.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.shedaniel.clothconfig2.api.TickableWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HJNButton extends ButtonWidget implements TickableWidget {
    protected static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    protected static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    protected int iconU;
    protected int iconV;
    protected int iconW;
    protected int iconH;

    private boolean useRainbowText;
    private int ticks = 0;
    private boolean useText;
    private boolean toggled;

    protected HJNButton(int x, int y, int width, int height, PressAction onPress) {
        super(x, y, width, height, Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    protected HJNButton(int x, int y, int width, int iconU, int iconV, PressAction onPress) {
        this(x, y, width, iconU, iconV, Text.empty(), onPress);
    }

    protected HJNButton(int x, int y, int width, int iconU, int iconV, Text text, PressAction onPress) {
        super(x, y, width, 18, text, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.iconU = iconU;
        this.iconV = iconV;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setIcon(int u, int v, int w, int h) {
        this.useText = false;
        this.iconU = u;
        this.iconV = v;
        this.iconW = w;
        this.iconH = h;
    }

    public void setUseRainbowText(boolean useRainbowText) {
        this.useRainbowText = useRainbowText;
    }

    public void setButtonText(Text text) {
        this.setMessage(text);
        useText = true;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, BG_NS_INFO, 0xFF000000);
        if (this.isHovered() || this.toggled) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, this.useText ? 48 : 16, 0, 16, 16, BG_NS_INFO);
        }

        if (!this.useText) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, BG_TEXTURE);
            drawTexture(matrices, this.getX() + (this.getWidth() - iconW) / 2, this.getY() + (this.getHeight() - iconH)/ 2, (float) this.iconU, (float) this.iconV, this.iconW, this.iconH, 256, 256);
        } else {
            int color = 0xFFFFFF;

            if (this.useRainbowText) {
                color = RenderUtil.hslToRgb((float)this.ticks * 2.0f, 0.7f, 0.6f);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2, color | (255 << 24));
        }
    }

    @Override
    public void tick() {
        if (this.useText && this.useRainbowText) {
            ++this.ticks;
        }
    }
}
