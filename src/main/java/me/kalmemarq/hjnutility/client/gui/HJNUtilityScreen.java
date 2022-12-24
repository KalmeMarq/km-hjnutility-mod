package me.kalmemarq.hjnutility.client.gui;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.client.gui.widget.*;
import me.kalmemarq.hjnutility.client.gui.page.*;
import me.kalmemarq.hjnutility.util.Anchor;
import me.kalmemarq.hjnutility.util.HJNUtil;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.option.ChatVisibility;
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
    private final int bgW = 300;
    private final int bgH = 195;
    private int bgX;
    private int bgY;
    private int selectedTab = 0;

    private final ToggleGroupManager tabManager = new ToggleGroupManager();
    private final List<HJNTabButton> tabs = new ArrayList<>();

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

        HJNStackWidget buttonStack = new HJNStackWidget();
        buttonStack.setColumnGap(2);

        buttonStack.add(new HJNIconButton(0, 0, 20, 18, 48, 16, 16, 16, button -> {
            client.setScreen(this.parentScreen);
            this.saveConfig();
        }));

        buttonStack.add(new HJNIconButton(0, 0, 20, 18, 64, 32, 16, 16, button -> {
        }));

        buttonStack.recalculateDimensions();
        HJNUtil.setAnchoredPos(buttonStack, bgX + 5, bgY + 5, bgX + bgW, bgY + bgH, Anchor.TOP_LEFT, Anchor.TOP_LEFT);
        this.addDrawableChild(buttonStack);

        this.tabManager.clearReset();
        this.tabs.clear();

        this.tabs.add(this.createTab(0, Text.translatable("kmhjnutility.config.general.tooltip"), 32, 32));
        this.tabs.add(this.createTab(1, Text.translatable("kmhjnutility.config.modules.tooltip"), 192, 32));
        this.tabs.add(this.createTab(2, Text.translatable("kmhjnutility.config.crosshairs.tooltip"), 112, 16));
        this.tabs.add(this.createTab(3, Text.translatable("kmhjnutility.config.themes.tooltip"), 16, 48));
//        this.tabs.add(this.createTab(4, Text.translatable("kmhjnutility.config.music.tooltip"), 96, 32));
        this.tabs.add(this.createTab(5, Text.translatable("kmhjnutility.config.about.tooltip"), 240, 16));

        HJNStackWidget tabStack = new HJNStackWidget();
        tabStack.setColumnGap(2);
        tabs.forEach(tabStack::add);
        tabStack.recalculateDimensions();
        HJNUtil.setAnchoredPos(tabStack, bgX, bgY + 5, bgX + bgW - 5, bgY + bgH, Anchor.TOP_RIGHT, Anchor.TOP_RIGHT);
        this.addDrawableChild(tabStack);

        int pageX = bgX + 5;
        int pageY = bgY + 27;
        int pageW = 290;
        int pageH = 163;

        this.pages.clear();
        this.pages.add(new HJNGeneralConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNModulesConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNCrosshairConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNThemeConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNMusicConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.add(new HJNAboutConfigPage(client, textRenderer, pageX, pageY, pageW, pageH));
        this.pages.forEach(this::addDrawableChild);

        this.tabManager.setSelectionConsumer((toggleGroupIndex, toggle) -> {
            selectTab(toggleGroupIndex);
        });
        this.tabManager.selectToggleGroupIndex(this.selectedTab);
    }

    private HJNTabButton createTab(int groupIndex, Text tooltip, int iconU, int iconV) {
        HJNTabButton tab = new HJNTabButton(20, iconU, iconV);
        tab.setTooltip(Tooltip.of(tooltip));
        tabManager.add(groupIndex, tab);
        return tab;
    }

    private void selectTab(int index) {
        this.selectedTab = index;

        for (int i = 0; i < this.pages.size(); i++) {
            this.pages.get(i).setVisible(i == this.selectedTab);
        }

        this.currentPage = this.pages.get(index);
    }

    private void saveConfig() {
        HJNUtilityMod.config.save();
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
            RenderUtil.drawColoredNinesliceTexture(matrices, bgX, bgY, bgW, bgH, 64, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, bgColor);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
