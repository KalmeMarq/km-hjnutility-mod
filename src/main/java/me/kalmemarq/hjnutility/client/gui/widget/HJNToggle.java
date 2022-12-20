package me.kalmemarq.hjnutility.client.gui.widget;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class HJNToggle extends PressableWidget implements HJNWidget {
    protected boolean toggled;
    @Nullable
    private PressAction onPress;

    protected HJNToggle(int x, int y, int width, int height, Text message, @Nullable PressAction onPress) {
        super(x, y, width, height, message);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        if (this.onPress != null) {
            this.onPress.onPress(this);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    public void setOnPress(@Nullable PressAction onPress) {
        this.onPress = onPress;
    }

    public HJNToggle setToggled(boolean toggled) {
        this.toggled = toggled;
        return this;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    @FunctionalInterface
    public interface PressAction {
        void onPress(HJNToggle toggle);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
