package me.kalmemarq.hjnutility.client.gui.page;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class HJNAboutConfigPage extends HJNConfigPage {
    public HJNAboutConfigPage(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.about.title"));
    }
}
