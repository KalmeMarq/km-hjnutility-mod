package me.kalmemarq.hjnutility.client.gui.widget;

import me.kalmemarq.hjnutility.util.HJNUtil;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HJNPanelWidget extends WrapperWidget {
    private List<ClickableWidget> children;
    private List<HJNUtil.HJNElementWrapper<?>> elements;

    public HJNPanelWidget() {
        super(0, 0, 0, 0, Text.empty());
        this.elements = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public <T extends ClickableWidget & HJNWidget> T add(T widget) {
        this.children.add(widget);
        this.elements.add(new HJNUtil.HJNElementWrapper<>(widget));
        return widget;
    }

    public HJNUtil.HJNElementWrapper<?> add(HJNUtil.HJNElementWrapper<?> element) {
        this.children.add(element.getWidget());
        this.elements.add(element);
        return element;
    }

    public void recalculateDimensions(int width, int height) {
        this.width = width;
        this.height = height;

        Iterator<HJNUtil.HJNElementWrapper<?>> iter = elements.iterator();
        while (iter.hasNext()) {
            HJNUtil.HJNElementWrapper<?> it = iter.next();
            it.setX(getX(), getWidth());
            it.setY(getY(), getHeight());
            it.setWidth(getWidth());
            it.setHeight(getHeight());
        }
    }

    @Override
    public void setPos(int x, int y) {
        super.setPos(x, y);

        Iterator<HJNUtil.HJNElementWrapper<?>> iterator = this.elements.iterator();
        while (iterator.hasNext()) {
            HJNUtil.HJNElementWrapper<?> element = iterator.next();

            element.setX(this.getX(), this.getWidth());
            element.setY(this.getY(), this.getHeight());
        }
    }

    @Override
    protected List<? extends ClickableWidget> wrappedWidgets() {
        return this.children;
    }
}
