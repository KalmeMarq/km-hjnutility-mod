package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNContentToggle;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HJNCrosshairConfigPage extends HJNConfigPage {
    private static final Text[] MODIFIER_TEXTS = {
        Text.translatable("kmhjnutility.config.crosshairs.modifier.inverted").formatted(Formatting.GRAY),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.chroma"),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.white"),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.red").formatted(Formatting.RED),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.green").formatted(Formatting.GREEN),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.blue").formatted(Formatting.BLUE),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.yellow").formatted(Formatting.YELLOW),
        Text.translatable("kmhjnutility.config.crosshairs.modifier.aqua").formatted(Formatting.AQUA)
    };
    private static final Text MODIFIER = Text.translatable("kmhjnutility.config.crosshairs.modifier");
    private List<HJNContentToggle> crosshairToggles;
    private List<HJNContentToggle> modifierToggles;

    public HJNCrosshairConfigPage(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.crosshairs.title"));
    }

    @Override
    public void tick() {
        super.tick();

        if (modifierToggles != null) {
            Iterator<HJNContentToggle> iterator = modifierToggles.iterator();
            while (iterator.hasNext()) {
                iterator.next().tick();
            }
        }
    }

    @Override
    protected void init() {
        if (this.crosshairToggles == null) this.crosshairToggles = new ArrayList<>();
        if (this.modifierToggles == null) this.modifierToggles = new ArrayList<>();
        this.crosshairToggles.clear();
        this.modifierToggles.clear();

        GridWidget chGrid = new GridWidget(getX() + 2, getY() + 20);
        chGrid.getMainPositioner().marginX(1).marginY(1);
        GridWidget.Adder chAdder = chGrid.createAdder(12);

        this.crosshairToggles.add(chAdder.add(HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.crosshairIndex == 0, button -> {
            selectCrosshair(0);
        }).width(250).setContent(new HJNContentToggle.TextToggleContent(Text.translatable("kmhjnutility.config.crosshairs.default"))).build(), 12));

        for (int i = 1; i < HJNCrosshair.CROSSHAIRS.size(); i++) {
            int crosshairIndex = i;
            HJNContentToggle.Builder builder = HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.crosshairIndex == i, widget -> {
                selectCrosshair(crosshairIndex);
            }).width(19);

            HJNCrosshair crosshair = HJNCrosshair.CROSSHAIRS.get(i);
            builder.setContent(new HJNContentToggle.IconToggleContent(crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight()));

            this.crosshairToggles.add(chAdder.add(builder.build()));
        }

        chGrid.recalculateDimensions();
        this.add(chGrid);

        GridWidget mdGrid = new GridWidget(getX() + 2, getY() + 98);
        mdGrid.getMainPositioner().marginX(1).marginY(1);
        GridWidget.Adder mdAdder = mdGrid.createAdder(4);

        var modifiers = HJNConfig.CrosshairModifier.values();
        for (int i = 0; i < modifiers.length; i++) {
            int modifierIndex = i;
            this.modifierToggles.add(mdAdder.add(
                HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.modifier == modifiers[i], widget -> {
                    selectModifier(modifiers[modifierIndex]);
                }).setContent(new HJNContentToggle.TextToggleContent(MODIFIER_TEXTS[i], modifiers[i] == HJNConfig.CrosshairModifier.Chroma)).size(61, 17).build()
            ));
        }

        mdGrid.recalculateDimensions();
        this.add(mdGrid);

        this.selectCrosshair(HJNUtilityMod.config.crosshairs.crosshairIndex);
        this.selectModifier(HJNUtilityMod.config.crosshairs.modifier);
    }

    private void selectCrosshair(int index) {
        HJNUtilityMod.config.crosshairs.crosshairIndex = index;
        for (int i = 0; i < this.crosshairToggles.size(); i++) {
            this.crosshairToggles.get(i).setToggled(index == i);
        }
    }

    private void selectModifier(HJNConfig.CrosshairModifier modifier) {
        HJNUtilityMod.config.crosshairs.modifier = modifier;
        for (int i = 0; i < this.modifierToggles.size(); i++) {
            this.modifierToggles.get(i).active = modifier.ordinal() != i;
        }
    }

    @Override
    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderPage(matrices, mouseX, mouseY, delta);

        RenderUtil.drawCenteredText(matrices, textRenderer, MODIFIER, getX() + getWidth() / 2, getY() + 88, 0xFF_FFFFFF);
    }
}
