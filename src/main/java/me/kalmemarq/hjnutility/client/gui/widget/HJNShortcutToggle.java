package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HJNShortcutToggle extends HJNCheckbox {
    private Icon icon;

    public static HJNShortcutToggle.Builder builder(Icon icon, Text message, boolean defaultValue, SaveConsumer saveConsumer) {
        return new HJNShortcutToggle.Builder(icon, message, defaultValue, saveConsumer);
    }

    protected HJNShortcutToggle(int x, int y, int width, int height, Icon icon, Text message, boolean defaultValue) {
        super(x, y, width, height, message, defaultValue);
        this.icon = icon;
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
        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);

        drawTexture(matrices, this.getX() + (this.getWidth() - this.icon.getWidth()) / 2, this.getY() + 4, (float) this.icon.getU(), (float) this.icon.getV(), this.icon.getWidth(), this.icon.getHeight(), 256, 256);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawCenteredText(matrices, textRenderer, this.getMessage(), this.getX() + this.getWidth() / 2, this.getY() + this.getHeight() - 8 - 6, 0xFF_FFFFFF);
    }

    public static class Icon {
        private int u;
        private int v;
        private int width;
        private int height;

        public static Icon create(int u, int v) {
            return create(u, v, 16, 16);
        }

        public static Icon create(int u, int v, int width, int height) {
            return new Icon(u, v, width, height);
        }

        Icon(int u, int v, int width, int height) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
        }

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static class Builder {
        private final Icon icon;
        private final Text message;
        private int x;
        private int y;
        private int width = 82;
        private int height = 38;
        private final boolean defaultValue;
        @Nullable
        private final SaveConsumer saveConsumer;

        public Builder(Icon icon, Text message, boolean defaultValue, @Nullable SaveConsumer saveConsumer) {
            this.icon = icon;
            this.message = message;
            this.defaultValue = defaultValue;
            this.saveConsumer = saveConsumer;
        }

        public HJNShortcutToggle.Builder width(int width) {
            this.width = width;
            return this;
        }

        public HJNShortcutToggle.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public HJNShortcutToggle build() {
            HJNShortcutToggle widget = new HJNShortcutToggle(this.x, this.y, this.width, this.height, this.icon, this.message, defaultValue);
            widget.setSaveConsumer(this.saveConsumer);
            return widget;
        }
    }
}
