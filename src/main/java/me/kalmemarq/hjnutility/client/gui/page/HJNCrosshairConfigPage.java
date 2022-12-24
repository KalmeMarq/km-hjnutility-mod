package me.kalmemarq.hjnutility.client.gui.page;

import me.kalmemarq.hjnutility.HJNConfig;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNContentToggle;
import me.kalmemarq.hjnutility.client.gui.widget.ToggleGroupManager;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
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

    public HJNCrosshairConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(client, textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.crosshairs.title"));
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
        if (this.crosshairToggles == null)
            this.crosshairToggles = new ArrayList<>();
        if (this.modifierToggles == null)
            this.modifierToggles = new ArrayList<>();
        this.crosshairToggles.clear();
        this.modifierToggles.clear();

        GridWidget chGrid = new GridWidget(getX() + 2, getY() + 20);
        chGrid.getMainPositioner().marginX(1).marginY(1);
        GridWidget.Adder chAdder = chGrid.createAdder(14);

        ToggleGroupManager crosshairManager = new ToggleGroupManager();

        HJNContentToggle defaultToggle = HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.crosshairIndex == 0, button -> {
        }).width(250).setContent(new HJNContentToggle.TextToggleContent(Text.translatable("kmhjnutility.config.crosshairs.default"))).build();
        this.crosshairToggles.add(chAdder.add(defaultToggle, 14));

        crosshairManager.add(0, defaultToggle);

        for (int i = 1; i < HJNCrosshair.CROSSHAIRS.size(); i++) {
            HJNContentToggle.Builder builder = HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.crosshairIndex == i, widget -> {
            }).width(19);

            HJNCrosshair crosshair = HJNCrosshair.CROSSHAIRS.get(i);
            builder.setContent(new HJNContentToggle.IconToggleContent(crosshair.getU(), crosshair.getV(), crosshair.getWidth(), crosshair.getHeight()));

            HJNContentToggle toggle = builder.build();
            crosshairManager.add(i, toggle);

            this.crosshairToggles.add(chAdder.add(toggle));
        }

        chGrid.recalculateDimensions();
        this.add(chGrid);

        GridWidget mdGrid = new GridWidget(getX() + 2, getY() + 98);
        mdGrid.getMainPositioner().marginX(1).marginY(1);
        GridWidget.Adder mdAdder = mdGrid.createAdder(4);

        crosshairManager.selectToggleGroupIndex(HJNUtilityMod.config.crosshairs.crosshairIndex);
        crosshairManager.setSelectionConsumer(((toggleGroupIndex, toggle) -> {
            HJNUtilityMod.config.crosshairs.crosshairIndex = toggleGroupIndex;
        }));

        ToggleGroupManager modifierManager = new ToggleGroupManager();

        var modifiers = HJNConfig.CrosshairModifier.values();
        for (int i = 0; i < modifiers.length; i++) {
            HJNContentToggle toggle = HJNContentToggle.builder(HJNUtilityMod.config.crosshairs.modifier == modifiers[i], widget -> {
            }).setContent(new HJNContentToggle.TextToggleContent(MODIFIER_TEXTS[i], modifiers[i] == HJNConfig.CrosshairModifier.Chroma)).size(71, 17).build();

            modifierManager.add(i, toggle);

            this.modifierToggles.add(mdAdder.add(toggle));
        }

        modifierManager.selectToggleGroupIndex(HJNUtilityMod.config.crosshairs.modifier.ordinal());
        modifierManager.setSelectionConsumer(((toggleGroupIndex, toggle) -> {
            HJNUtilityMod.config.crosshairs.modifier = HJNConfig.CrosshairModifier.values()[toggleGroupIndex];
        }));

        mdGrid.recalculateDimensions();
        this.add(mdGrid);
    }

    @Override
    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderPage(matrices, mouseX, mouseY, delta);

        RenderUtil.drawCenteredText(matrices, textRenderer, MODIFIER, getX() + getWidth() / 2, getY() + 88, 0xFF_FFFFFF);
    }
}
