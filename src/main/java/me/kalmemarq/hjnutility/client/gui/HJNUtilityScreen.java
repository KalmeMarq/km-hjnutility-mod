package me.kalmemarq.hjnutility.client.gui;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.client.gui.widget.HJNIconButton;
import me.kalmemarq.hjnutility.client.gui.widget.HJNTabButton;
import me.kalmemarq.hjnutility.client.gui.page.*;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HJNUtilityScreen extends Screen {
    private static final RenderUtil.NinesliceInfo BG_BEDROCK_NS_INFO = new RenderUtil.NinesliceInfo(6, 6, 6, 6, 256, 256);
    private final int bgW = 260;
    private final int bgH = 165;
    private int bgX;
    private int bgY;
    private int selectedTab = 0;

    private HJNTabButton generalTab;
    private HJNTabButton modulesTab;
    private HJNTabButton crosshairTab;
    private HJNTabButton themesTab;
    private HJNTabButton aboutTab;

    @Nullable
    private HJNConfigPage currentPage;
    private final List<HJNConfigPage> pages = new ArrayList<>();

    private final Screen parentScreen;

    public HJNUtilityScreen(Screen parentScreen) {
        super(Text.empty());
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        bgX = (this.width - bgW) / 2;
        bgY = (this.height - bgH) / 2;

        HJNIconButton closeButton = new HJNIconButton(bgX + 3, bgY + 3, 20, 18, 48, 16, 16, 16, button -> {
            client.setScreen(this.parentScreen);
            this.saveConfig();
        });
        this.addDrawableChild(closeButton);

        GridWidget grid = new GridWidget();
        grid.getMainPositioner().marginX(1);
        GridWidget.Adder adder = grid.createAdder(5);

        this.generalTab = adder.add(this.createTab(0, Text.translatable("kmhjnutility.config.general.tooltip"), 32, 32));
        this.modulesTab = adder.add(this.createTab(1, Text.translatable("kmhjnutility.config.modules.tooltip"), 192, 32));
        this.crosshairTab = adder.add(this.createTab(2, Text.translatable("kmhjnutility.config.crosshairs.tooltip"), 112, 16));
        this.themesTab = adder.add(this.createTab(3, Text.translatable("kmhjnutility.config.themes.tooltip"), 16, 48));
        this.aboutTab = adder.add(this.createTab(4, Text.translatable("kmhjnutility.config.about.tooltip"), 240, 16));

        grid.recalculateDimensions();
        grid.setPos(bgX + bgW - 2 - grid.getWidth(), bgY + 3);

        this.addDrawableChild(grid);

        int pageX = bgX + 2;
        int pageY = bgY + 24;
        int pageW = bgW - 4;
        int pageH = bgH - 26;

        this.pages.clear();
        this.pages.add(new HJNGeneralConfigPage(textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNModulesConfigPage(textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNCrosshairConfigPage(textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNThemeConfigPage(textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNAboutConfigPage(textRenderer, pageX, pageY, pageW, pageH));
        this.pages.forEach(this::addDrawableChild);

        this.selectTab(this.selectedTab);
    }

    private HJNTabButton createTab(int index, Text tooltip, int iconU, int iconV) {
        HJNTabButton tab = new HJNTabButton(20, iconU, iconV, button -> this.selectTab(index));
        tab.setTooltip(Tooltip.of(tooltip));
        return tab;
    }

    private void selectTab(int index) {
        this.selectedTab = index;

        this.generalTab.setSelected(selectedTab == 0);
        this.modulesTab.setSelected(selectedTab == 1);
        this.crosshairTab.setSelected(selectedTab == 2);
        this.themesTab.setSelected(selectedTab == 3);
        this.aboutTab.setSelected(selectedTab == 4);

        for (int i = 0; i < this.pages.size(); i++) {
            this.pages.get(i).setVisible(i == this.selectedTab);
        }

        this.currentPage = this.pages.get(index);
    }

    private void saveConfig() {
        HJNUtilityMod.configHolder.save();
    }
    
    private boolean isPregame() {
        return this.client.world == null;
    }

    @Override
    public void close() {
        super.close();
        this.saveConfig();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.currentPage != null) {
            this.currentPage.tick();
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isPregame() || !HJNUtilityMod.config.themes.hideScreenBackground) {
            this.renderBackground(matrices);
        }

        RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);
        if (HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Bedrock) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderUtil.drawNinesliceTexture(matrices, bgX - 6, bgY - 6, bgW + 12, bgH + 12, 0, 224, 14, 14, BG_BEDROCK_NS_INFO);
        } else {
            int bgColor = HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default ? 0x8C000000 : 0xFF_FFFFFF;
            RenderUtil.drawColoredNinesliceTexture(matrices, bgX, bgY, bgW, bgH, 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, bgColor);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
