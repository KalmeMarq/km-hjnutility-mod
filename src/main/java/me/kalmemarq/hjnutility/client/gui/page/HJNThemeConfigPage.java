package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.*;
import me.kalmemarq.hjnutility.util.Anchor;
import me.kalmemarq.hjnutility.util.HJNUtil;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class HJNThemeConfigPage extends HJNConfigPage {
    private static final Text INFO = Text.translatable("kmhjnutility.config.themes.info");

    public HJNThemeConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(client, textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.themes.title"));
    }

    @Override
    protected void init() {
        HJNStackWidget stack = new HJNStackWidget();
        stack.setColumnGap(2);

        HJNToggle defaultTgl = stack.add(HJNThemeToggle.builder(HJNConfig.Theme.Default, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default, widget -> {}).build());
        HJNToggle customTgl = stack.add(HJNThemeToggle.builder(HJNConfig.Theme.Custom, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Custom, widget -> {}).build());
        HJNToggle bedrockTgl = stack.add(HJNThemeToggle.builder(HJNConfig.Theme.Bedrock, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Bedrock, widget -> {}).build());

        stack.recalculateDimensions();
        HJNUtil.setAnchoredPos(stack, getX(), getY() + 43, getX() + getWidth(), getY() + getHeight(), Anchor.TOP_MIDDLE, Anchor.TOP_MIDDLE);
        this.add(stack);

        ToggleGroupManager themeManager = new ToggleGroupManager();
        HJNConfig.Theme[] themes = HJNConfig.Theme.values();

        themeManager.add(0, defaultTgl);
        themeManager.add(1, customTgl);
        themeManager.add(2, bedrockTgl);

        themeManager.selectToggleGroupIndex(HJNUtilityMod.config.themes.theme.ordinal());
        themeManager.setSelectionConsumer(((toggleGroupIndex, toggle) -> {
            HJNUtilityMod.config.themes.theme = themes[toggleGroupIndex];
        }));

        this.add(HJNCheckbox.builder(
                Text.translatable("kmhjnutility.config.themes.hide_screen_background"),
                HJNUtilityMod.config.themes.hideScreenBackground,
                newValue -> HJNUtilityMod.config.themes.hideScreenBackground = newValue)
                .pos(getX() + 3, getY() + 35 + 80).build());
    }

    @Override
    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderPage(matrices, mouseX, mouseY, delta);

        RenderUtil.drawCenteredText(matrices, textRenderer, INFO, getX() + getWidth() / 2, getY() + 26, 0xFF_FFFFFF);
    }
}
