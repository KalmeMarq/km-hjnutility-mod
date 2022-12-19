package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HJNContentToggle extends HJNToggle {
    @Nullable
    private ToggleContent content;

    public static HJNContentToggle.Builder builder(boolean defaultValue, PressAction onPress) {
        return new HJNContentToggle.Builder(defaultValue, onPress);
    }

    protected HJNContentToggle(int x, int y, int width, int height, boolean defaultValue, @Nullable ToggleContent content, PressAction onPress) {
        super(x, y, width, height, Text.empty(), onPress);
        this.content = content;
        this.toggled = defaultValue;
    }

    public void tick() {
        if (this.content != null) {
            this.content.tick();
        }
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF000000);

        if (this.isHovered() || this.toggled) {
            RenderUtil.drawNinesliceTexture(matrices, this.getX(), this.getY(), this.width, this.height, 96, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO);
        }

        if (this.content != null) {
            this.content.render(matrices, this, mouseX, mouseY, delta);
        }
    }

    static abstract class ToggleContent extends DrawableHelper {
        void tick() {}

        abstract void render(MatrixStack matrices, HJNContentToggle toggle, int mouseX, int mouseY, float delta);
    }

    public static class IconToggleContent extends ToggleContent {
        private int iconU;
        private int iconV;
        private int iconWidth;
        private int iconHeight;

        public IconToggleContent(int iconU, int iconV, int iconWidth, int iconHeight) {
            this.iconU = iconU;
            this.iconV = iconV;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
        }

        @Override
        void render(MatrixStack matrices, HJNContentToggle toggle, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
            drawTexture(matrices, toggle.getX() + (toggle.getWidth() - this.iconWidth) / 2, toggle.getY() + (toggle.getHeight() - iconHeight)/ 2, (float) this.iconU, (float) this.iconV, this.iconWidth, this.iconHeight, 256, 256);
        }
    }

    public static class TextToggleContent extends ToggleContent {
        private final Text text;
        private final boolean rgb;
        private int ticks;

        public TextToggleContent(Text text) {
            this(text, false);
        }

        public TextToggleContent(Text text, boolean rgb) {
            this.text = text;
            this.rgb = rgb;
        }

        @Override
        void tick() {
            if (this.rgb) {
                ++this.ticks;
            }
        }

        @Override
        void render(MatrixStack matrices, HJNContentToggle toggle, int mouseX, int mouseY, float delta) {
            int color = 0xFFFFFF;

            if (this.rgb) {
                color = RenderUtil.hslToRgb((float)this.ticks * 2.0f, 0.7f, 0.6f);
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
            RenderUtil.drawCenteredText(matrices, textRenderer, this.text, toggle.getX() + toggle.getWidth() / 2, toggle.getY() + (toggle.getHeight() - 8) / 2, color | (255 << 24));
        }
    }

    public static class Builder {
        private int x;
        private int y;
        private int width = 100;
        private int height = 20;
        private final boolean defaultValue;
        private final PressAction onPress;
        @Nullable
        private ToggleContent content;

        public Builder(boolean defaultValue, PressAction onPress) {
            this.defaultValue = defaultValue;
            this.onPress = onPress;
        }

        public HJNContentToggle.Builder width(int width) {
            this.width = width;
            return this;
        }

        public HJNContentToggle.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public HJNContentToggle.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public HJNContentToggle.Builder setContent(ToggleContent content) {
            this.content = content;
            return this;
        }

        public HJNContentToggle build() {
            HJNContentToggle widget = new HJNContentToggle(this.x, this.y, this.width, this.height, defaultValue, content, onPress);
            return widget;
        }
    }
}
