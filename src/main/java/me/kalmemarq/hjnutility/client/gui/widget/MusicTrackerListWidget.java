package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.MusicSound;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MusicTrackerListWidget extends ElementListWidget<MusicTrackerListWidget.MusicTrackerEntry> {
    public boolean visible = true;

    public MusicTrackerListWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
    }

    public void add(Text name, MusicSound musicSound) {
        this.addEntry(MusicTrackerEntry.create(client, client.textRenderer, name, musicSound));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            double d = this.client.getWindow().getScaleFactor();
            RenderSystem.enableScissor((int)((double)this.getRowLeft() * d), (int)((double)(this.height - this.bottom) * d), (int)((double)(this.getScrollbarPositionX() + 6) * d), (int)((double)(this.height - (this.height - this.bottom) - this.top) * d));
            super.render(matrices, mouseX, mouseY, delta);
            RenderSystem.disableScissor();
        }
    }

    @Override
    protected void renderDecorations(MatrixStack matrices, int mouseX, int mouseY) {
        super.renderDecorations(matrices, mouseX, mouseY);
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.width / 2 + 105;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.visible) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.visible) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.visible) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.visible) {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.visible) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public int getRowWidth() {
        return 190;
    }

    protected static class MusicTrackerEntry extends ElementListWidget.Entry<MusicTrackerEntry> {
        private final TextRenderer textRenderer;
        private final List<HJNIconButton> buttons;
        private final Text name;
        private final MusicSound musicSound;
        private final HJNIconButton playButton;

        private MusicTrackerEntry(MinecraftClient client, TextRenderer textRenderer, Text name, MusicSound musicSound) {
            this.buttons = new ArrayList<>();
            this.textRenderer = textRenderer;
            this.name = name;
            this.musicSound = musicSound;
            this.playButton = new HJNIconButton(0, 0, 20, 20, 160, 32, 16, 16, button -> {
                client.getMusicTracker().play(this.musicSound);
            });
            this.buttons.add(this.playButton);
        }

        public static MusicTrackerEntry create(MinecraftClient client, TextRenderer textRenderer, Text name, MusicSound musicSound) {
            return new MusicTrackerEntry(client, textRenderer, name, musicSound);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.buttons;
        }

        @Override
        public List<? extends Element> children() {
            return this.buttons;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);

            RenderUtil.drawColoredNinesliceTexture(matrices, x, y, entryWidth, 24, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF_303030);

            RenderUtil.drawText(matrices, textRenderer, name, x + 4, y + (entryHeight - 2 - 8) / 2 + 2, 0xFF_FFFFFF);

            this.playButton.setY(y + (entryHeight - 2 - 20) / 2 + 2);
            this.playButton.setX(x + entryWidth - 22);
            this.playButton.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
