package me.kalmemarq.hjnutility.client.gui;

import me.kalmemarq.hjnutility.client.gui.widget.HJNButton;
import me.kalmemarq.hjnutility.client.gui.widget.HJNStackWidget;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HJNModuleSettingsScreen extends Screen {
    private final Screen parentScreen;

    protected HJNModuleSettingsScreen(Screen parentScreen) {
        super(Text.empty());
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new HJNButton(this.width / 2 - 50, this.height - 40, 100, 20, Text.translatable("gui.done"), button -> {
            this.client.setScreen(this.parentScreen);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderUtil.fill(matrices, 0, 0, width, height, 0x99_000000);
    }
}
