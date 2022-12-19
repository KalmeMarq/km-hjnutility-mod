package me.kalmemarq.hjnutility.client.gui.page;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class HJNConfigPage extends WrapperWidget {
    protected MinecraftClient client;
    protected TextRenderer textRenderer;
    private final List<ClickableWidget> children;
    protected Text title;
    protected int titleWidth;

    public HJNConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height, MutableText title) {
        super(x, y, width, height, Text.empty());
        this.textRenderer = textRenderer;
        this.title = title.formatted(Formatting.UNDERLINE);
        this.titleWidth = textRenderer.getWidth(title);
        this.children = new ArrayList<>();
        this.init();
    }

    public void tick() {}

    protected void init() {}

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.visible) return false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (HJNUtilityMod.config.themes.theme != HJNConfig.Theme.Bedrock) {
            int bgColor = HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default ? 0x99_0A0A0A : 0xFF_3E3E3F;
            fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), bgColor);
        }

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);

        int paddingX = 8;
        int paddingY = 2;
        RenderUtil.drawColoredNinesliceTexture(matrices, getX() + getWidth() / 2 - titleWidth / 2 - paddingX, getY() + 4, this.titleWidth + paddingX + paddingX, 9 + paddingY + paddingY, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF_000000);
        RenderUtil.drawCenteredText(matrices, textRenderer, this.title, getX() + getWidth() / 2, getY() + 7, 0xFF_FFFFFF);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderPage(matrices, mouseX, mouseY, delta);
        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected List<? extends ClickableWidget> wrappedWidgets() {
        return this.children;
    }

    public void add(ClickableWidget widget) {
        this.children.add(widget);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.active = visible;
    }
}
