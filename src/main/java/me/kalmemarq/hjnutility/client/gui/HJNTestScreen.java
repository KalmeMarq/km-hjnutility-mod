package me.kalmemarq.hjnutility.client.gui;

import me.kalmemarq.hjnutility.client.gui.widget.HJNButton;
import me.kalmemarq.hjnutility.client.gui.widget.HJNImageWidget;
import me.kalmemarq.hjnutility.client.gui.widget.HJNLabelWidget;
import me.kalmemarq.hjnutility.client.gui.widget.HJNPanelWidget;
import me.kalmemarq.hjnutility.util.Anchor;
import me.kalmemarq.hjnutility.util.HJNUtil;
import me.kalmemarq.hjnutility.util.nonsense.Length;
import me.kalmemarq.hjnutility.util.nonsense.LengthCollector;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class HJNTestScreen extends Screen {
    private final Screen parentScreen;

    public HJNTestScreen(Screen parentScreen) {
        super(Text.empty());
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        HJNPanelWidget panel = new HJNPanelWidget();

        HJNButton buttonTest = new HJNButton(0, 0, 100, 20, Text.literal("Offset[20, 20] Anchor[top_left] Size[100% - 40, 20]"), button -> {
            this.client.setScreen(this.parentScreen);
        });

        panel.add(new HJNUtil.HJNElementWrapper<>(buttonTest)
                .setOffsetX(LengthCollector.of(Length.pixel(20)))
                .setOffsetY(LengthCollector.of(Length.pixel(20)))
                .setSizeWidth(LengthCollector.of(Length.relative(1.0f), Length.pixel(-40)))
                .setSizeHeight(LengthCollector.of(Length.pixel(20)))
                .setAnchor(Anchor.TOP_LEFT, Anchor.TOP_LEFT)
        );

        HJNLabelWidget labelTest = new HJNLabelWidget(textRenderer, Text.literal("Offset[0, 50%] Size[100%, 9] TextAlign[center] Anchor[top_left]"));
        labelTest.setAlignment(HJNLabelWidget.TextAlignment.CENTER);

        panel.add(new HJNUtil.HJNElementWrapper<>(labelTest)
            .setOffsetY(LengthCollector.of(Length.relative(0.5f)))
            .setSizeWidth(LengthCollector.of(Length.relative(1.0f)))
            .setAnchor(Anchor.TOP_LEFT, Anchor.TOP_LEFT)
        );

        HJNImageWidget imgTest = new HJNImageWidget();
        imgTest.setInfo(new HJNImageWidget.ImageInfo(new Identifier("textures/gui/widgets.png")));
        imgTest.setTintColor(0xFF_00_00);

        panel.add(new HJNUtil.HJNElementWrapper<>(imgTest)
            .setSizeWidth(LengthCollector.of(Length.pixel(128)))
            .setSizeHeight(LengthCollector.of(Length.pixel(128)))
            .setOffsetY(LengthCollector.of(Length.pixel(20)))
            .setAnchor(Anchor.CENTER, Anchor.TOP_MIDDLE)
        );

        panel.recalculateDimensions(this.width, this.height);
        this.addDrawableChild(panel);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
