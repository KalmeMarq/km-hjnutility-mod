package me.kalmemarq.hjnutility.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HJNUtilityScreen extends Screen {
    private static final RenderUtil.NinesliceInfo BG_NS_INFO = new RenderUtil.NinesliceInfo(2, 2, 2, 2, 256, 256);
    private static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    private final int bgW = 260;
    private final int bgH = 165;
    private int bgX;
    private int bgY;
    private int selectedTab = 0;

    @Nullable
    private HJNButton closeButton;
    @Nullable
    private HJNTabButton generalTab;
    @Nullable
    private HJNTabButton modulesTab;
    @Nullable
    private HJNTabButton crosshairTab;
    @Nullable
    private HJNTabButton themesTab;
    @Nullable
    private HJNTabButton aboutTab;

    @Nullable
    private HJNPage currentPage;
    private List<HJNPage> pages = new ArrayList<>();

    private Screen parentScreen;

    public HJNUtilityScreen(Screen parentScreen) {
        super(Text.empty());
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        bgX = this.width / 2 - bgW / 2;
        bgY = this.height / 2 - bgH / 2;

        this.selectTab(0);
    }

    private void rebuildGUI() {
        this.clearChildren();

        this.closeButton = new HJNButton(bgX + 3, bgY + 3, 20, 48, 16, button -> {
            client.setScreen(this.parentScreen);
        });

        var hideMsgBtn = new HJNButton(bgX + 25, bgY + 3, 20, HJNUtilityMod.config.general.hideMsg ? 80 : 48, 32, button -> {
            HJNUtilityMod.config.general.hideMsg = !HJNUtilityMod.config.general.hideMsg;
            ((HJNButton)button).iconU = HJNUtilityMod.config.general.hideMsg ? 80 : 48;
            button.setTooltip(Tooltip.of(Text.translatable(HJNUtilityMod.config.general.hideMsg ? "kmhjnutility.config.hideMsg.hide" : "kmhjnutility.config.hideMsg.show")));
        });

        hideMsgBtn.setTooltip(Tooltip.of(Text.translatable(HJNUtilityMod.config.general.hideMsg ? "kmhjnutility.config.hideMsg.hide" : "kmhjnutility.config.hideMsg.show")));
        this.addDrawableChild(hideMsgBtn);

        AxisGridWidget axisGrid = new AxisGridWidget(bgX + bgW - 3 - ((20 * 5) + (2 * 4)), bgY + 3, (20 * 5) + (2 * 4), 18, AxisGridWidget.DisplayAxis.HORIZONTAL);

        this.generalTab = this.createTab(0, Text.translatable("kmhjnutility.config.general.tooltip"), 32, 32);
        this.modulesTab = this.createTab(1, Text.translatable("kmhjnutility.config.modules.tooltip"), 192, 32);
        this.crosshairTab = this.createTab(2, Text.translatable("kmhjnutility.config.crosshairs.tooltip"), 112, 16);
        this.themesTab = this.createTab(3, Text.translatable("kmhjnutility.config.themes.tooltip"), 16, 48);
        this.aboutTab = this.createTab(4, Text.translatable("kmhjnutility.config.about.tooltip"), 240, 16);

        this.addDrawableChild(this.closeButton);
        axisGrid.add(this.generalTab);
        axisGrid.add(this.modulesTab);
        axisGrid.add(this.crosshairTab);
        axisGrid.add(this.themesTab);
        axisGrid.add(this.aboutTab);
        axisGrid.recalculateDimensions();

        this.addDrawableChild(axisGrid);

        this.pages.clear();
        this.pages.add(new HJNGeneralPage(this, this.textRenderer));
        this.pages.add(new HJNModulesPage(this, this.textRenderer));
        this.pages.add(new HJNCrosshairsPage(this, this.textRenderer));
        this.pages.add(new HJNThemesPage(this, this.textRenderer));
        this.pages.add(new HJNAboutPage(this, this.textRenderer));

        this.currentPage = this.pages.get(this.selectedTab);
        this.currentPage.buildGUI(this.bgX, this.bgY, this.bgW, this.bgH);
    }

    private HJNTabButton createTab(int index, Text tooltip, int iconU, int iconV) {
        HJNTabButton tab = new HJNTabButton(20, iconU, iconV, button -> {
            this.selectTab(index);
        });
        tab.setTooltip(Tooltip.of(tooltip));
        return tab;
    }

    private void selectTab(int index) {
        this.selectedTab = index;
        this.rebuildGUI();
        this.generalTab.setSelected(selectedTab == 0);
        this.modulesTab.setSelected(selectedTab == 1);
        this.crosshairTab.setSelected(selectedTab == 2);
        this.themesTab.setSelected(selectedTab == 3);
        this.aboutTab.setSelected(selectedTab == 4);
    }
    
    private boolean isPregame() {
        return this.client.world != null;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isPregame() || !HJNUtilityMod.config.themes.hideScreenBackground) {
            this.renderBackground(matrices);
        }

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        RenderUtil.drawColoredNinesliceTexture(matrices, bgX, bgY, bgW, bgH, 0, 0, 16, 16,  BG_NS_INFO, 0x8C000000);

        super.render(matrices, mouseX, mouseY, delta);

        if (this.currentPage != null) {
            this.currentPage.renderPageBg(matrices, bgX, bgY, bgW, bgH);
        }
    }

    static class HJNGeneralPage extends HJNPage {
        HJNGeneralPage(Screen parentScreen, TextRenderer textRenderer) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.general.title"));
        }
    }

    static class HJNModulesPage extends HJNPage {
        HJNModulesPage(Screen parentScreen, TextRenderer textRenderer) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.modules.title"));
        }
    }

    static class HJNCrosshairsPage extends HJNPage {
        HJNCrosshairsPage(Screen parentScreen, TextRenderer textRenderer) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.crosshairs.title"));
        }
    }

    static class HJNThemesPage extends HJNPage {
        HJNThemesPage(Screen parentScreen, TextRenderer textRenderer) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.themes.title"));
        }
    }

    static class HJNAboutPage extends HJNPage {
        HJNAboutPage(Screen parentScreen, TextRenderer textRenderer) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.about.title"));
        }
    }

    static class HJNPage {
        protected Screen parentScreen;
        protected TextRenderer textRenderer;

        private Text title;
        private int titleWidth;

        HJNPage(Screen parentScreen, TextRenderer textRenderer, MutableText title) {
            this.parentScreen = parentScreen;
            this.textRenderer = textRenderer;
            this.title = title.formatted(Formatting.UNDERLINE);
            this.titleWidth = textRenderer.getWidth(this.title);
        }

        public void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
        }

        public void renderPageBg(MatrixStack matrices, int pageX, int pageY, int pageWidth, int pageHeight) {
            int paddingTB = 2;
            int paddingLR = 8;
            int x = pageX + pageWidth / 2 - (this.titleWidth / 2) - paddingLR;
            int y = pageY + 30 - paddingTB;
            int w = this.titleWidth + paddingLR + paddingLR;
            int h = 9 + paddingTB + paddingTB;

            fill(matrices, pageX + 3, pageY + 24, pageX + pageWidth - 3, pageY + pageHeight - 3, 0xAA_0A0A0A);

            RenderSystem.setShaderTexture(0, BG_TEXTURE);
            RenderUtil.drawColoredNinesliceTexture(matrices, x, y, w, h, 0, 0, 16, 16,  BG_NS_INFO, 0xFF_000000);
            RenderUtil.drawCenteredText(matrices, textRenderer, this.title, pageX + pageWidth / 2, pageY + 30, 0xFF_FFFFFF);
        }
    }
}
