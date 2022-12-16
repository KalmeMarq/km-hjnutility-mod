package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HJNCheckbox extends PressableWidget {
    protected boolean toggled;
    @Nullable
    private SaveConsumer saveConsumer;

    public static HJNCheckbox.Builder builder(Text message, boolean defaultValue, SaveConsumer saveConsumer) {
        return new HJNCheckbox.Builder(message, defaultValue, saveConsumer);
    }

    protected HJNCheckbox(int x, int y, int width, int height, Text message, boolean defaultValue) {
        super(x, y, width, height, message);
        this.toggled = defaultValue;
    }

    public void setSaveConsumer(@Nullable SaveConsumer saveConsumer) {
        this.saveConsumer = saveConsumer;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (this.saveConsumer != null) this.saveConsumer.save(toggled);
    }

    @Override
    public void onPress() {
        this.setToggled(!this.toggled);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int boxColor = isHovered() || this.toggled ? 0xFF_FFFFFF : 0xFF000000;
        RenderUtil.drawColoredNinesliceTexture(matrices, this.getX(), this.getY(), 15, 15, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, boxColor);
        if (this.toggled) {
            RenderUtil.drawCenteredText(matrices, textRenderer, Text.literal("+"), (int)(this.getX() + 15 / 2.0f) + 1, this.getY() + (this.getHeight() - 8) / 2 + 1, 0xFF_000000);
        }

        RenderUtil.drawText(matrices, textRenderer, this.getMessage(), this.getX() + 15 + 6, this.getY() + (this.getHeight() - 8) / 2, 0xFF_FFFFFF);
    }

    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @FunctionalInterface
    public interface SaveConsumer {
        void save(boolean newValue);
    }

    public static class Builder {
        private final Text message;
        private int x;
        private int y;
        private int width = 126;
        private int height = 15;
        private final boolean defaultValue;
        @Nullable
        private final SaveConsumer saveConsumer;

        public Builder(Text message, boolean defaultValue, @Nullable SaveConsumer saveConsumer) {
            this.message = message;
            this.defaultValue = defaultValue;
            this.saveConsumer = saveConsumer;
        }

        public HJNCheckbox.Builder width(int width) {
            this.width = width;
            return this;
        }

        public HJNCheckbox.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public HJNCheckbox.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public HJNCheckbox build() {
            HJNCheckbox widget = new HJNCheckbox(this.x, this.y, this.width, this.height, this.message, defaultValue);
            widget.setSaveConsumer(this.saveConsumer);
            return widget;
        }
    }
}
