package me.kalmemarq.hjnutility.client.gui.widget;

import me.kalmemarq.hjnutility.util.HJNUtil;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HJNStackWidget extends WrapperWidget {
    private final List<ClickableWidget> children;
    private StackOrientation orientation = StackOrientation.HORIZONTAL;
    private final List<HJNStackElement<?>> elements;
    private int gapColumn;
    private int gapRow;

    public HJNStackWidget() {
        this(0, 0);
    }

    public HJNStackWidget(int x, int y) {
        super(x, y, 0, 0, Text.empty());
        this.children = new ArrayList<>();
        this.elements = new ArrayList<>();
    }

    public <T extends ClickableWidget> T add(T widget) {
        this.children.add(widget);
        this.elements.add(new HJNStackElement<>(widget));
        return widget;
    }

    public <T extends ClickableWidget> HJNUtil.HJNElementWrapper<T> addElement(HJNStackElement<T> element) {
        this.children.add(element.getWidget());
        this.elements.add(element);
        return element;
    }

    @Nullable
    public HJNUtil.HJNElementWrapper<?> getElement(int index) {
        return this.elements.get(index);
    }

    public void setOrientation(StackOrientation orientation) {
        this.orientation = orientation;
    }

    public void setRowGap(int width) {
        this.gapRow = width;
    }

    public void setColumnGap(int width) {
        this.gapColumn = width;
    }

    public void setGap(int width) {
        setGap(width, width);
    }

    public void setGap(int row, int column) {
        this.gapRow = row;
        this.gapColumn = column;
    }

    public void recalculateDimensions() {
        int w = 0;
        int h = 0;

        if (this.orientation == StackOrientation.HORIZONTAL) {
            for (int i = 0; i < this.elements.size(); i++) {
                int elH = this.elements.get(i).getHeight();

                if (elH > h) h = elH;
            }

            for (int i = 0; i < this.elements.size(); i++) {
                int elW = this.elements.get(i).getWidth();

                this.elements.get(i).setInternalX(w);
                this.elements.get(i).setInternalY(0);

                w += elW;

                if (i + 1 < this.elements.size()) {
                    w += gapColumn;
                }
            }
        } else {
            for (int i = 0; i < this.elements.size(); i++) {
                int elW = this.elements.get(i).getWidth();

                if (elW > w) w = elW;
            }

            for (int i = 0; i < this.elements.size(); i++) {
                int elH = this.elements.get(i).getWidth();

                this.elements.get(i).setInternalX(0);
                this.elements.get(i).setInternalY(this.getY() + h);

                h += elH;

                if (i + 1 < this.elements.size()) {
                    h += gapRow;
                }
            }
        }

        this.width = w;
        this.height = h;
    }

    @Override
    public void setPos(int x, int y) {
        super.setPos(x, y);

        Iterator<HJNStackElement<?>> iterator = this.elements.iterator();
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

    enum StackOrientation {
        VERTICAL,
        HORIZONTAL
    }

    static class HJNStackElement<T extends ClickableWidget> extends HJNUtil.HJNElementWrapper<T> {
        private int internalX;
        private int internalY;

        public HJNStackElement(T widget) {
            super(widget);
        }

        public void setInternalX(int internalX) {
            this.internalX = internalX;
        }

        public void setInternalY(int internalY) {
            this.internalY = internalY;
        }

        @Override
        public HJNUtil.HJNElementWrapper<T> setX(int parX, int parW) {
            this.getWidget().setX(parX + internalX);
            return this;
        }

        @Override
        public HJNUtil.HJNElementWrapper<T> setY(int parY, int parH) {
            this.getWidget().setY(parY + internalY);
            return this;
        }
    }
}
