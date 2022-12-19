package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNCheckbox;
import me.kalmemarq.hjnutility.util.Anchor;
import me.kalmemarq.hjnutility.util.HJNUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class HJNModulesConfigPage extends HJNConfigPage {
    public HJNModulesConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(client, textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.modules.title"));
    }

    @Override
    protected void init() {
        GridWidget grid = new GridWidget();
        grid.getMainPositioner().marginBottom(2);
        GridWidget.Adder adder = grid.createAdder(2);

        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_compass"), HJNUtilityMod.config.modules.showCompass, newValue -> HJNUtilityMod.config.modules.showCompass = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_mobeffects"), HJNUtilityMod.config.modules.hideMobEffects, newValue -> HJNUtilityMod.config.modules.hideMobEffects = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_item_id"), HJNUtilityMod.config.modules.showItemId, newValue -> HJNUtilityMod.config.modules.showItemId = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_bossbars"), HJNUtilityMod.config.modules.hideBossBars, newValue -> HJNUtilityMod.config.modules.hideBossBars = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_vignette"), HJNUtilityMod.config.modules.hideVignette, newValue -> HJNUtilityMod.config.modules.hideVignette = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_paperdoll"), HJNUtilityMod.config.modules.showPaperdoll, newValue -> HJNUtilityMod.config.modules.showPaperdoll = newValue).build());

        grid.recalculateDimensions();
        HJNUtil.setAnchoredPos(grid, getX(), getY() + 25, getX() + getWidth(), getY() + getHeight(), Anchor.TOP_MIDDLE, Anchor.TOP_MIDDLE);

        this.add(grid);
    }
}
