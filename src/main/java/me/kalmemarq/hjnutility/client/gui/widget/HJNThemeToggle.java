package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HJNThemeToggle extends HJNCheckbox {
    private HJNConfig.Theme theme;

    public static HJNThemeToggle.Builder builder(HJNConfig.Theme theme, boolean defaultValue, PressAction onPress) {
        return new Builder(theme, defaultValue, onPress);
    }

    protected HJNThemeToggle(int x, int y, int width, int height, HJNConfig.Theme theme, boolean defaultValue, @Nullable PressAction onPress) {
        super(x, y, width, height, Text.empty(), onPress, defaultValue);
        this.theme = theme;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF000000);

        if (this.isHovered() || this.toggled) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 48, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO);
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int u = 0;
        int v = 0;
        if (theme == HJNConfig.Theme.Custom) {
            u = 388;
        } else if (theme == HJNConfig.Theme.Bedrock) {
            v = 288;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_THEME_TEXTURE);
        drawTexture(matrices, this.getX() + 3, this.getY() + 3, this.getWidth() - 6, this.getHeight() - 6, u, v, 387, 287, 1024, 1024);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + (this.getHeight() - 8) / 2 + 20, 0xFF_FFFFFF);
    }

    public static class Builder {
        private final HJNConfig.Theme theme;
        private int x;
        private int y;
        private int width = 82;
        private int height = 66;
        private final boolean defaultValue;
        private final PressAction onPress;

        public Builder(HJNConfig.Theme theme, boolean defaultValue, PressAction onPress) {
            this.theme = theme;
            this.defaultValue = defaultValue;
            this.onPress = onPress;
        }

        public HJNThemeToggle.Builder width(int width) {
            this.width = width;
            return this;
        }

        public HJNThemeToggle.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public HJNThemeToggle build() {
            HJNThemeToggle widget = new HJNThemeToggle(this.x, this.y, this.width, this.height, this.theme, defaultValue, onPress);
            return widget;
        }
    }
}
