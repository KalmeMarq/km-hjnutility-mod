package me.kalmemarq.hjnutility.gui;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNCrosshair;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.AxisGridWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
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
    private static final RenderUtil.NinesliceInfo BG_BEDROCK_NS_INFO = new RenderUtil.NinesliceInfo(6, 6, 6, 6, 256, 256);
    private static final Identifier BG_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
    private final int bgW = 260;
    private final int bgH = 172;
    private int bgX;
    private int bgY;
    private int selectedTab = 0;

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
    private final List<HJNPage> pages = new ArrayList<>();

    private final Screen parentScreen;

    public HJNUtilityScreen(Screen parentScreen) {
        super(Text.empty());
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        bgX = this.width / 2 - bgW / 2;
        bgY = this.height / 2 - bgH / 2;

        HJNButton closeButton = new HJNButton(bgX + 3, bgY + 3, 20, 18, button -> {
            client.setScreen(this.parentScreen);
            HJNUtilityMod.configHolder.save();
        });
        closeButton.setIcon(48, 16, 16, 16);

        var hideMsgBtn = new HJNButton(bgX + 25, bgY + 3, 20, HJNUtilityMod.config.general.hideMsg ? 80 : 48, 32, button -> {
            HJNUtilityMod.config.general.hideMsg = !HJNUtilityMod.config.general.hideMsg;
            ((HJNButton)button).iconU = HJNUtilityMod.config.general.hideMsg ? 80 : 48;
            button.setTooltip(Tooltip.of(Text.translatable(HJNUtilityMod.config.general.hideMsg ? "kmhjnutility.config.hideMsg.hide" : "kmhjnutility.config.hideMsg.show")));
        });

        hideMsgBtn.setTooltip(Tooltip.of(Text.translatable(HJNUtilityMod.config.general.hideMsg ? "kmhjnutility.config.hideMsg.hide" : "kmhjnutility.config.hideMsg.show")));
//        this.addDrawableChild(hideMsgBtn);

        AxisGridWidget axisGrid = new AxisGridWidget(bgX + bgW - 3 - ((20 * 5) + (2 * 4)), bgY + 3, (20 * 5) + (2 * 4), 18, AxisGridWidget.DisplayAxis.HORIZONTAL);

        this.generalTab = this.createTab(0, Text.translatable("kmhjnutility.config.general.tooltip"), 32, 32);
        this.modulesTab = this.createTab(1, Text.translatable("kmhjnutility.config.modules.tooltip"), 192, 32);
        this.crosshairTab = this.createTab(2, Text.translatable("kmhjnutility.config.crosshairs.tooltip"), 112, 16);
        this.themesTab = this.createTab(3, Text.translatable("kmhjnutility.config.themes.tooltip"), 16, 48);
        this.aboutTab = this.createTab(4, Text.translatable("kmhjnutility.config.about.tooltip"), 240, 16);

        this.addDrawableChild(closeButton);
        axisGrid.add(this.generalTab);
        axisGrid.add(this.modulesTab);
        axisGrid.add(this.crosshairTab);
        axisGrid.add(this.themesTab);
        axisGrid.add(this.aboutTab);
        axisGrid.recalculateDimensions();

        this.addDrawableChild(axisGrid);

        this.pages.clear();
        this.pages.add(new HJNGeneralPage(this, this.textRenderer, bgX, bgY, bgW, bgH, this::addDrawableChild));
        this.pages.add(new HJNModulesPage(this, this.textRenderer, bgX, bgY, bgW, bgH, this::addDrawableChild));
        this.pages.add(new HJNCrosshairsPage(this, this.textRenderer, bgX, bgY, bgW, bgH, this::addDrawableChild));
        this.pages.add(new HJNThemesPage(this, this.textRenderer, bgX, bgY, bgW, bgH, this::addDrawableChild));
        this.pages.add(new HJNAboutPage(this, this.textRenderer, bgX, bgY, bgW, bgH, this::addDrawableChild));

        this.selectTab(this.selectedTab);
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
        this.currentPage = this.pages.get(this.selectedTab);

        this.generalTab.setSelected(selectedTab == 0);
        this.modulesTab.setSelected(selectedTab == 1);
        this.crosshairTab.setSelected(selectedTab == 2);
        this.themesTab.setSelected(selectedTab == 3);
        this.aboutTab.setSelected(selectedTab == 4);

        for (int i = 0; i < this.pages.size(); i++) {
            this.pages.get(i).setVisible(i == this.selectedTab);
        }
    }
    
    private boolean isPregame() {
        return this.client.world == null;
    }

    @Override
    public void close() {
        super.close();
        HJNUtilityMod.configHolder.save();
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

        RenderSystem.setShaderTexture(0, BG_TEXTURE);
        if (HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Bedrock) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderUtil.drawNinesliceTexture(matrices, bgX - 6, bgY - 6, bgW + 12, bgH + 12, 0, 224, 14, 14, BG_BEDROCK_NS_INFO);
        } else {
            int bgColor = HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default ? 0x8C000000 : 0xFF_FFFFFF;
            RenderUtil.drawColoredNinesliceTexture(matrices, bgX, bgY, bgW, bgH, 0, 0, 16, 16,  BG_NS_INFO, bgColor);
        }

        if (this.currentPage != null) {
            this.currentPage.renderPageBg(matrices, bgX, bgY, bgW, bgH);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }

    static class HJNGeneralPage extends HJNPage {
        HJNGeneralPage(Screen parentScreen, TextRenderer textRenderer, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.general.title"), pageX, pageY, pageWidth, pageHeight, addWidgetAction);
        }

        private HJNToggleButton shinyPotionsToggle;
        private HJNToggleButton armorHudToggle;
        private HJNToggleButton mainHandToggle;
        private HJNToggleButton offHandToggle;
        private HJNToggleButton statusHudToggle;

        @Override
        protected void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
            pageY += 24 + 16;
            pageX += 3;
            pageWidth -= 6;
            pageHeight -= 24 - 3;

            shinyPotionsToggle = new HJNToggleButton(pageX + 2, pageY + 2, 82, 38, Text.translatable("kmhjnutility.config.general.shiny_potions"), 208, 32, button -> {
                boolean newValue = !((HJNToggleButton)button).isToggled();
                ((HJNToggleButton)button).setToggled(newValue);
                HJNUtilityMod.config.general.shinyPotions = newValue;
            });
            shinyPotionsToggle.setToggled(HJNUtilityMod.config.general.shinyPotions);

            armorHudToggle = new HJNToggleButton(pageX + 2 + 82 + 2, pageY + 2, 82, 38, Text.translatable("kmhjnutility.config.general.armor_hud"), 32, 16, button -> {
                boolean newValue = !((HJNToggleButton)button).isToggled();
                ((HJNToggleButton)button).setToggled(newValue);
                HJNUtilityMod.config.general.armorHud = newValue;
            });
            armorHudToggle.setToggled(HJNUtilityMod.config.general.armorHud);

            mainHandToggle = new HJNToggleButton(pageX + 2 + 82 + 2 + 82 + 2, pageY + 2, 82, 38, Text.translatable("kmhjnutility.config.general.mainhand_slot"), 16, 32, button -> {
                boolean newValue = !((HJNToggleButton)button).isToggled();
                ((HJNToggleButton)button).setToggled(newValue);
                HJNUtilityMod.config.general.mainHandSlot = newValue;
            });
            mainHandToggle.setToggled(HJNUtilityMod.config.general.mainHandSlot);

            offHandToggle = new HJNToggleButton(pageX + 2, pageY + 2 + 40, 82, 38, Text.translatable("kmhjnutility.config.general.offhand_slot"), 144, 32, button -> {
                boolean newValue = !((HJNToggleButton)button).isToggled();
                ((HJNToggleButton)button).setToggled(newValue);
                HJNUtilityMod.config.general.offHandSlot = newValue;
            });
            offHandToggle.setToggled(HJNUtilityMod.config.general.offHandSlot);

            statusHudToggle = new HJNToggleButton(pageX + 2 + 82 + 2, pageY + 2 + 40, 82, 38, Text.translatable("kmhjnutility.config.general.status_hud"), 240, 32, button -> {
                boolean newValue = !((HJNToggleButton)button).isToggled();
                ((HJNToggleButton)button).setToggled(newValue);
                HJNUtilityMod.config.general.statusHud = newValue;
            });
            statusHudToggle.setToggled(HJNUtilityMod.config.general.statusHud);

            this.addWidgetAction.add(shinyPotionsToggle);
            this.addWidgetAction.add(armorHudToggle);
            this.addWidgetAction.add(mainHandToggle);
            this.addWidgetAction.add(offHandToggle);
            this.addWidgetAction.add(statusHudToggle);

            this.setVisible(false);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            shinyPotionsToggle.visible = visible;
            armorHudToggle.visible = visible;
            mainHandToggle.visible = visible;
            offHandToggle.visible = visible;
            statusHudToggle.visible = visible;
        }
    }

    static class HJNModulesPage extends HJNPage {
        HJNModulesPage(Screen parentScreen, TextRenderer textRenderer, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.modules.title"), pageX, pageY, pageWidth, pageHeight, addWidgetAction);
        }

        private HJNToggle showPaperdollToggle;
        private HJNToggle hideMobEffectsToggle;
        private HJNToggle showItemIDToggle;
        private HJNToggle showCompassToggle;
        private HJNToggle hideBossBarsToggle;
        private HJNToggle hideVignetteToggle;

        @Override
        protected void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
            pageY += 24 + 16;
            pageX += 3;
            pageWidth -= 6;
            pageHeight -= 24 - 3;

            showCompassToggle = new HJNToggle(pageX, pageY + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.show_compass"), button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.modules.showCompass = newValue;
            });
            showCompassToggle.setToggled(HJNUtilityMod.config.modules.showCompass);

            hideMobEffectsToggle = new HJNToggle(pageX + 126, pageY + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.hide_mobeffects"), button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.modules.hideMobEffects = newValue;
            });
            hideMobEffectsToggle.setToggled(HJNUtilityMod.config.modules.hideMobEffects);

            showItemIDToggle = new HJNToggle(pageX, pageY + 2 + 15 + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.show_item_id"), button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.modules.showItemID = newValue;
            });
            showItemIDToggle.setToggled(HJNUtilityMod.config.modules.showItemID);

            hideBossBarsToggle = new HJNToggle(pageX + 126, pageY + 2 + 15 + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.hide_bossbars"), button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.modules.hideBossBars = newValue;
            });
            hideBossBarsToggle.setToggled(HJNUtilityMod.config.modules.hideBossBars);

            hideVignetteToggle = new HJNToggle(pageX, pageY + 2 + 15 + 2 + 15 + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.hide_vignette"), button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.modules.hideVignette = newValue;
            });
            hideVignetteToggle.setToggled(HJNUtilityMod.config.modules.hideVignette);

//            showPaperdollToggle = new HJNToggle(pageX + 126, pageY + 2 + 15 + 2 + 15 + 2, 126, 15, Text.translatable("kmhjnutility.config.modules.show_paperdoll"), button -> {
//                boolean newValue = !((HJNToggle)button).isToggled();
//                ((HJNToggle)button).setToggled(newValue);
//                HJNUtilityMod.config.modules.showPaperdoll = newValue;
//            });
//            showPaperdollToggle.setToggled(HJNUtilityMod.config.modules.showPaperdoll);

            this.addWidgetAction.add(showCompassToggle);
            this.addWidgetAction.add(hideMobEffectsToggle);
            this.addWidgetAction.add(showItemIDToggle);
            this.addWidgetAction.add(hideBossBarsToggle);
            this.addWidgetAction.add(hideVignetteToggle);
//            this.addWidgetAction.add(showPaperdollToggle);

            setVisible(false);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
//            showPaperdollToggle.visible = visible;
            hideMobEffectsToggle.visible = visible;
            showItemIDToggle.visible = visible;
            showCompassToggle.visible = visible;
            hideBossBarsToggle.visible = visible;
            hideVignetteToggle.visible = visible;
        }
    }

    static class HJNCrosshairsPage extends HJNPage {
        private static final Text MODIFIER = Text.translatable("kmhjnutility.config.crosshairs.modifier");

        private List<HJNButton> crosshairs;
        HJNCrosshairsPage(Screen parentScreen, TextRenderer textRenderer, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.crosshairs.title"), pageX, pageY, pageWidth, pageHeight, addWidgetAction);

        }

        private boolean showModifiers = true;
        private HJNButton crosshairDefault;

        // Modifier
        private HJNButton modifierInvertedBtn;
        private HJNButton modifierChromaBtn;
        private HJNButton modifierWhiteBtn;
        private HJNButton modifierRedBtn;
        private HJNButton modifierGreenBtn;
        private HJNButton modifierBlueBtn;
        private HJNButton modifierYellowBtn;
        private HJNButton modifierAquaBtn;

        @Override
        protected void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
            pageY += 24 + 16;
            pageX += 3;
            pageWidth -= 6;
            pageHeight -= 24 - 3;

            if (this.crosshairs == null) {
                this.crosshairs = new ArrayList<>();
            }

            crosshairDefault = new HJNButton(pageX + 3 + 3 + 6, pageY + 2, pageWidth - 4 - 6 - 12, 20, button -> {
                setCrosshair(0);
            });
            crosshairDefault.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.default"));
            this.addWidgetAction.add(crosshairDefault);

            this.crosshairs.clear();

            for (int i = 0; i < HJNCrosshair.CROSSHAIRS.size() - 1; i++) {
                int x = pageX + 3 + 3 + 6 + 1;
                int y = pageY + 2 + 22;

                if (i > 10) {
                    y += 22;
                }

                int finalI = i;
                HJNButton b = new HJNButton(x + (i % 11) * 21, y, 19, 20, button -> {
                   setCrosshair(finalI + 1);
                });
                HJNCrosshair crosshair = HJNCrosshair.CROSSHAIRS.get(i + 1);
                b.setIcon(crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight());

                this.crosshairs.add(b);
                this.addWidgetAction.add(b);
            }

            modifierInvertedBtn = new HJNButton(pageX + 3, pageY + 88, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Inverted);
            });
            modifierInvertedBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.inverted").formatted(Formatting.GRAY));

            modifierChromaBtn = new HJNButton(pageX + 3 + 61 + 2, pageY + 88, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Chroma);
            });
            modifierChromaBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.chroma"));
            modifierChromaBtn.setUseRainbowText(true);

            modifierWhiteBtn = new HJNButton(pageX + 3 + 61 + 2 + 61 + 2, pageY + 88, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.White);
            });
            modifierWhiteBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.white"));

            modifierRedBtn = new HJNButton(pageX + 3 + 61 + 2 + 61 + 2 + 61 + 2, pageY + 88, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Red);
            });
            modifierRedBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.red").formatted(Formatting.RED));

            modifierGreenBtn = new HJNButton(pageX + 3, pageY + 88 + 17 + 2, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.White);
            });
            modifierGreenBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.green").formatted(Formatting.GREEN));

            modifierBlueBtn = new HJNButton(pageX + 3 + 61 + 2, pageY + 88 + 17 + 2, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Blue);
            });
            modifierBlueBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.blue").formatted(Formatting.BLUE));

            modifierYellowBtn = new HJNButton(pageX + 3 + 61 + 2 + 61 + 2, pageY + 88 + 17 + 2, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Yellow);
            });
            modifierYellowBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.yellow").formatted(Formatting.YELLOW));

            modifierAquaBtn = new HJNButton(pageX + 3 + 61 + 2 + 61 + 2 + 61 + 2, pageY + 88 + 17 + 2, 61, 17, button -> {
                setModifier(HJNConfig.CrosshairModifier.Aqua);
            });
            modifierAquaBtn.setButtonText(Text.translatable("kmhjnutility.config.crosshairs.modifier.aqua").formatted(Formatting.AQUA));

            this.addWidgetAction.add(modifierInvertedBtn);
            this.addWidgetAction.add(modifierChromaBtn);
            this.addWidgetAction.add(modifierWhiteBtn);
            this.addWidgetAction.add(modifierRedBtn);
            this.addWidgetAction.add(modifierGreenBtn);
            this.addWidgetAction.add(modifierBlueBtn);
            this.addWidgetAction.add(modifierYellowBtn);
            this.addWidgetAction.add(modifierAquaBtn);

            updateCrosshairButtons(HJNUtilityMod.config.crosshairs.crosshairIndex);
            updateModifiersButtons(HJNUtilityMod.config.crosshairs.modifier);
            setVisible(false);
        }

        private void updateCrosshairButtons(int index) {
            crosshairDefault.setToggled(index == 0);

            for (int i = 0; i < crosshairs.size(); i++) {
                crosshairs.get(i).setToggled(index - 1 == i);
            }
        }

        private void updateModifiersButtons(HJNConfig.CrosshairModifier modifier) {
            modifierInvertedBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Inverted);
            modifierChromaBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Chroma);
            modifierWhiteBtn.setToggled(modifier == HJNConfig.CrosshairModifier.White);
            modifierRedBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Red);
            modifierGreenBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Green);
            modifierBlueBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Blue);
            modifierYellowBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Yellow);
            modifierAquaBtn.setToggled(modifier == HJNConfig.CrosshairModifier.Aqua);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            crosshairDefault.visible = visible;
            for (int i = 0; i < crosshairs.size(); i++) {
                crosshairs.get(i).visible = visible;
            }
//            showModifiers = HJNUtilityMod.config.crosshairs.crosshairIndex > 0 && visible;
            setModifierPanelVisible(visible);
        }

        private void setCrosshair(int index) {
            HJNUtilityMod.config.crosshairs.crosshairIndex = index;
            updateCrosshairButtons(index);
            setModifierPanelVisible(showModifiers);
        }

        private void setModifier(HJNConfig.CrosshairModifier modifier) {
            HJNUtilityMod.config.crosshairs.modifier = modifier;
            updateModifiersButtons(modifier);
        }

        private void setModifierPanelVisible(boolean visible) {
            modifierInvertedBtn.visible = visible;
            modifierChromaBtn.visible = visible;
            modifierWhiteBtn.visible = visible;
            modifierRedBtn.visible = visible;
            modifierGreenBtn.visible = visible;
            modifierBlueBtn.visible = visible;
            modifierYellowBtn.visible = visible;
            modifierAquaBtn.visible = visible;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.showModifiers) {
                this.modifierChromaBtn.tick();
            }
        }

        @Override
        public void renderPageBg(MatrixStack matrices, int pageX, int pageY, int pageWidth, int pageHeight) {
            super.renderPageBg(matrices, pageX, pageY, pageWidth, pageHeight);

            if (this.visible) {
                RenderUtil.drawCenteredText(matrices, textRenderer, MODIFIER, pageX + pageWidth / 2, pageY + 112, 0xFF_FFFFFF);
            }
        }
    }

    static class HJNThemesPage extends HJNPage {
        private static final Text INFO = Text.translatable("kmhjnutility.config.themes.info");

        HJNThemesPage(Screen parentScreen, TextRenderer textRenderer, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.themes.title"), pageX, pageY, pageWidth, pageHeight, addWidgetAction);
        }

        private HJNThemeToggleButton themeDefaultBtn;
        private HJNThemeToggleButton themeCustomBtn;
        private HJNThemeToggleButton themeBedrockBtn;
        private HJNToggle screenBgToggle;

        @Override
        protected void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
            pageY += 24 + 16;
            pageX += 3;
            pageWidth -= 6;
            pageHeight -= 24 - 3;

            themeDefaultBtn = new HJNThemeToggleButton(pageX + 3, pageY + 2 + 21, Text.translatable("kmhjnutility.config.themes.theme.default"), 0, 0, 0, 576, button -> {
                themeDefaultBtn.setToggled(true);
                themeCustomBtn.setToggled(false);
                themeBedrockBtn.setToggled(false);
                HJNUtilityMod.config.themes.theme = HJNConfig.Theme.Default;
            });
            themeDefaultBtn.setToggled(HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default);

            themeCustomBtn = new HJNThemeToggleButton(pageX + 3 + 82 + 2, pageY + 2 + 21, Text.translatable("kmhjnutility.config.themes.theme.custom"), 0, 288, 61, 576, button -> {
                themeDefaultBtn.setToggled(false);
                themeCustomBtn.setToggled(true);
                themeBedrockBtn.setToggled(false);
                HJNUtilityMod.config.themes.theme = HJNConfig.Theme.Custom;
            });
            themeCustomBtn.setToggled(HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Custom);

            themeBedrockBtn = new HJNThemeToggleButton(pageX + 3 + 82 + 2 + 82 + 2, pageY + 2 + 21, Text.translatable("kmhjnutility.config.themes.theme.bedrock"), 387, 0, 122, 576, button -> {
                themeDefaultBtn.setToggled(false);
                themeCustomBtn.setToggled(false);
                themeBedrockBtn.setToggled(true);
                HJNUtilityMod.config.themes.theme = HJNConfig.Theme.Bedrock;
            });
            themeBedrockBtn.setToggled(HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Bedrock);

            Text hideScreenBgText = Text.translatable("kmhjnutility.config.themes.hide_screen_background");
            screenBgToggle = new HJNToggle(pageX + pageWidth / 2 - textRenderer.getWidth(hideScreenBgText) / 2, pageY + 2 + 21 + 78, 126, 15, hideScreenBgText, button -> {
                boolean newValue = !((HJNToggle)button).isToggled();
                ((HJNToggle)button).setToggled(newValue);
                HJNUtilityMod.config.themes.hideScreenBackground = newValue;
            });
            screenBgToggle.setToggled(HJNUtilityMod.config.themes.hideScreenBackground);

            this.addWidgetAction.add(themeDefaultBtn);
            this.addWidgetAction.add(themeCustomBtn);
            this.addWidgetAction.add(themeBedrockBtn);
            this.addWidgetAction.add(screenBgToggle);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            screenBgToggle.visible = visible;
            themeDefaultBtn.visible = visible;
            themeCustomBtn.visible = visible;
            themeBedrockBtn.visible = visible;
        }

        @Override
        public void renderPageBg(MatrixStack matrices, int pageX, int pageY, int pageWidth, int pageHeight) {
            super.renderPageBg(matrices, pageX, pageY, pageWidth, pageHeight);

            if (this.visible) {
                RenderUtil.drawCenteredText(matrices, textRenderer, INFO, pageX + pageWidth / 2, pageY + 24 + 16 + 4, 0xFF_FFFFFF);
            }
        }
    }

    static class HJNAboutPage extends HJNPage {
        HJNAboutPage(Screen parentScreen, TextRenderer textRenderer, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            super(parentScreen, textRenderer, Text.translatable("kmhjnutility.config.about.title"), pageX, pageY, pageWidth, pageHeight, addWidgetAction);
        }

        @Override
        public void renderPageBg(MatrixStack matrices, int pageX, int pageY, int pageWidth, int pageHeight) {
            super.renderPageBg(matrices, pageX, pageY, pageWidth, pageHeight);

            if (this.visible) {
            }
        }
    }

    static class HJNPage {
        protected boolean visible = false;
        protected Screen parentScreen;
        protected TextRenderer textRenderer;

        private final Text title;
        private final int titleWidth;
        protected AddWidgetAction addWidgetAction;

        HJNPage(Screen parentScreen, TextRenderer textRenderer, MutableText title, int pageX, int pageY, int pageWidth, int pageHeight, AddWidgetAction addWidgetAction) {
            this.parentScreen = parentScreen;
            this.textRenderer = textRenderer;
            this.title = title.formatted(Formatting.UNDERLINE);
            this.titleWidth = textRenderer.getWidth(this.title);
            this.addWidgetAction = addWidgetAction;
            this.buildGUI(pageX, pageY, pageWidth, pageHeight);
        }

        protected void buildGUI(int pageX, int pageY, int pageWidth, int pageHeight) {
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void tick() {}

        public void renderPageBg(MatrixStack matrices, int pageX, int pageY, int pageWidth, int pageHeight) {
            if (this.visible) {
                int paddingTB = 2;
                int paddingLR = 8;
                int x = pageX + pageWidth / 2 - (this.titleWidth / 2) - paddingLR;
                int y = pageY + 29 - paddingTB;
                int w = this.titleWidth + paddingLR + paddingLR;
                int h = 9 + paddingTB + paddingTB;

                if (HJNUtilityMod.config.themes.theme != HJNConfig.Theme.Bedrock) {
                    int bgColor = HJNUtilityMod.config.themes.theme == HJNConfig.Theme.Default ? 0xAA_0A0A0A : 0xFF_3E3E3F;
                    fill(matrices, pageX + 3, pageY + 24, pageX + pageWidth - 3, pageY + pageHeight - 3, bgColor);
                }

                RenderSystem.setShaderTexture(0, BG_TEXTURE);
                RenderUtil.drawColoredNinesliceTexture(matrices, x, y, w, h, 0, 0, 16, 16, BG_NS_INFO, 0xFF_000000);
                RenderUtil.drawCenteredText(matrices, textRenderer, this.title, pageX + pageWidth / 2, pageY + 29, 0xFF_FFFFFF);
            }
        }

        interface AddWidgetAction {
            void add(ClickableWidget widget);
        }
    }
}
