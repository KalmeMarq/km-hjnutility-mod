package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNShortcutToggle;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;

public class HJNGeneralConfigPage extends HJNConfigPage {
    public HJNGeneralConfigPage(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.general.title"));
    }

    @Override
    protected void init() {
        GridWidget grid = new GridWidget(getX() + 2, getY() + 20);
        grid.getMainPositioner().marginX(1).marginY(1);
        GridWidget.Adder adder = grid.createAdder(3);

        adder.add(HJNShortcutToggle.builder(
                HJNShortcutToggle.Icon.create(208, 32),
                Text.translatable("kmhjnutility.config.general.shiny_potions"),
                HJNUtilityMod.config.general.shinyPotions,
                newValue -> HJNUtilityMod.config.general.shinyPotions = newValue).build());

        adder.add(HJNShortcutToggle.builder(
                HJNShortcutToggle.Icon.create(32, 16),
                Text.translatable("kmhjnutility.config.general.armor_hud"),
                HJNUtilityMod.config.general.armorHud,
                newValue -> HJNUtilityMod.config.general.armorHud = newValue).build());

        adder.add(HJNShortcutToggle.builder(
                HJNShortcutToggle.Icon.create(16, 32),
                Text.translatable("kmhjnutility.config.general.mainhand_slot"),
                HJNUtilityMod.config.general.mainHandSlot,
                newValue -> HJNUtilityMod.config.general.mainHandSlot = newValue).build());

        adder.add(HJNShortcutToggle.builder(
                HJNShortcutToggle.Icon.create(144, 32),
                Text.translatable("kmhjnutility.config.general.offhand_slot"),
                HJNUtilityMod.config.general.offHandSlot,
                newValue -> HJNUtilityMod.config.general.offHandSlot = newValue).build());

        adder.add(HJNShortcutToggle.builder(
                HJNShortcutToggle.Icon.create(240, 32),
                Text.translatable("kmhjnutility.config.general.status_hud"),
                HJNUtilityMod.config.general.statusHud,
                newValue -> HJNUtilityMod.config.general.statusHud = newValue).build());

        grid.recalculateDimensions();
        this.add(grid);
    }
}
