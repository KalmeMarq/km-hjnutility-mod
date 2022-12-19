package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.util.RenderUtil;
import me.kalmemarq.hjnutility.client.gui.widget.HJNCheckbox;
import me.kalmemarq.hjnutility.client.gui.widget.HJNThemeToggle;
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

    private HJNThemeToggle themeDefaultTgl;
    private HJNThemeToggle themeCustomTgl;
    private HJNThemeToggle themeBedrockTgl;

    @Override
    protected void init() {
        GridWidget grid = new GridWidget(getX() + 2, getY() + 35);
        grid.getMainPositioner().marginX(1);
        GridWidget.Adder adder = grid.createAdder(3);

        this.themeDefaultTgl = adder.add(HJNThemeToggle.builder(HJNConfig.Theme.Default, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default, widget -> {
            selectTheme(HJNConfig.Theme.Default);
        }).build());

        this.themeCustomTgl = adder.add(HJNThemeToggle.builder(HJNConfig.Theme.Custom, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Custom, widget -> {
            selectTheme(HJNConfig.Theme.Custom);
        }).build());

        this.themeBedrockTgl = adder.add(HJNThemeToggle.builder(HJNConfig.Theme.Bedrock, HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Bedrock, widget -> {
            selectTheme(HJNConfig.Theme.Bedrock);
        }).build());

        grid.recalculateDimensions();
        this.add(grid);

        this.add(HJNCheckbox.builder(
                Text.translatable("kmhjnutility.config.themes.hide_screen_background"),
                HJNUtilityMod.config.themes.hideScreenBackground,
                newValue -> HJNUtilityMod.config.themes.hideScreenBackground = newValue)
                .pos(getX() + 3, getY() + 35 + 80).build());
    }

    private void selectTheme(HJNConfig.Theme theme) {
        HJNUtilityMod.config.themes.theme = theme;
        this.themeDefaultTgl.setToggled(theme == HJNConfig.Theme.Default);
        this.themeCustomTgl.setToggled(theme == HJNConfig.Theme.Custom);
        this.themeBedrockTgl.setToggled(theme == HJNConfig.Theme.Bedrock);
    }

    @Override
    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderPage(matrices, mouseX, mouseY, delta);

        RenderUtil.drawCenteredText(matrices, textRenderer, INFO, getX() + getWidth() / 2, getY() + 21, 0xFF_FFFFFF);
    }
}
