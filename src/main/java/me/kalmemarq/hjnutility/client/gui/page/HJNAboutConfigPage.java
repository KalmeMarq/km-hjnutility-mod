package me.kalmemarq.hjnutility.client.gui.page;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;

public class HJNAboutConfigPage extends HJNConfigPage {
    public HJNAboutConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(client, textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.about.title"));
    }
}
