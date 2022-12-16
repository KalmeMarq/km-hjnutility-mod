package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNCheckbox;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class HJNModulesConfigPage extends HJNConfigPage {
    public HJNModulesConfigPage(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.modules.title"));
    }

    @Override
    protected void init() {
        GridWidget grid = new GridWidget(getX() + 2, getY() + 20);
        grid.getMainPositioner().marginBottom(2);
        GridWidget.Adder adder = grid.createAdder(2);

        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_compass"), HJNUtilityMod.config.modules.showCompass, newValue -> HJNUtilityMod.config.modules.showCompass = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_mobeffects"), HJNUtilityMod.config.modules.hideMobEffects, newValue -> HJNUtilityMod.config.modules.hideMobEffects = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_item_id"), HJNUtilityMod.config.modules.showItemID, newValue -> HJNUtilityMod.config.modules.showItemID = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_bossbars"), HJNUtilityMod.config.modules.hideBossBars, newValue -> HJNUtilityMod.config.modules.hideBossBars = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.hide_vignette"), HJNUtilityMod.config.modules.hideVignette, newValue -> HJNUtilityMod.config.modules.hideVignette = newValue).build());
        adder.add(HJNCheckbox.builder(Text.translatable("kmhjnutility.config.modules.show_paperdoll"), HJNUtilityMod.config.modules.showPaperdoll, newValue -> HJNUtilityMod.config.modules.showPaperdoll = newValue).build());

        grid.recalculateDimensions();
        this.add(grid);
    }
}
