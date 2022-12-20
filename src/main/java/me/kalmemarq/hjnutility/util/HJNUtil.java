package me.kalmemarq.hjnutility.util;

import me.kalmemarq.hjnutility.client.gui.widget.HJNLabelWidget;
import me.kalmemarq.hjnutility.client.gui.widget.HJNStackWidget;
import me.kalmemarq.hjnutility.client.gui.widget.HJNWidget;
import me.kalmemarq.hjnutility.util.nonsense.Length;
import me.kalmemarq.hjnutility.util.nonsense.LengthCollector;
import net.minecraft.client.gui.widget.ClickableWidget;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HJNUtil {
    public static void setPos(ClickableWidget widget, int left, int top, int right, int bottom) {
        setPos(widget, left, top, right, bottom, 0.5f, Anchor.CENTER);
    }

    public static void setPos(ClickableWidget widget, int left, int top, int right, int bottom, float relative, Anchor anchor) {
        setPos(widget, left, top, right, bottom, relative, relative, anchor);
    }

    public static void setPos(ClickableWidget widget, int left, int top, int right, int bottom, float relativeX, float relativeY, Anchor anchor) {
        setPos(widget, left, top, right, bottom, relativeX, relativeY, anchor, anchor);
    }

    public static void setPos(ClickableWidget widget, int left, int top, int right, int bottom, float relativeX, float relativeY, Anchor anchorFrom, Anchor anchorTo) {
        int elW = widget.getWidth();
        int elH = widget.getHeight();

        int parX = left;
        int parY = right;
        int parW = right - left;
        int parH = bottom - top;
    }

    public static void setAnchoredPos(ClickableWidget widget, int left, int top, int right, int bottom, Anchor anchorFrom, Anchor anchorTo) {
        int elW = widget.getWidth();
        int elH = widget.getHeight();

        int parX = left;
        int parY = top;
        int parW = right - left;
        int parH = bottom - top;

        int x = Anchor.getX(elW, parX, parW, anchorFrom, anchorTo);
        int y = Anchor.getY(elH, parY, parH, anchorFrom, anchorTo);

        widget.setPos(x, y);
    }

    public static class HJNElementWrapper<T extends ClickableWidget & HJNWidget> {
        private final T widget;
        private Vector2i offset = new Vector2i();
        private LengthCollector offsetX = new LengthCollector();
        private LengthCollector offsetY = new LengthCollector();
        private LengthCollector sizeWidth = new LengthCollector();
        private LengthCollector sizeHeight = new LengthCollector();
        private Anchor anchorFrom = Anchor.CENTER;
        private Anchor anchorTo = Anchor.CENTER;

        public HJNElementWrapper(T widget) {
            this.widget = widget;

            if (widget instanceof HJNLabelWidget) {
                this.setSizeWidth(LengthCollector.of(Length.lenDefault()));
                this.setSizeHeight(LengthCollector.of(Length.pixel(9)));
            }
        }

        public HJNElementWrapper<T> setX(int parX, int parW) {
            int x = parX;

            if (anchorFrom.isVMiddle()) {
                x += parW / 2;
            } else if (anchorFrom.isRight()) {
                x = parW;
            }

            if (anchorTo.isVMiddle()) {
                x -= widget.getWidth() / 2;
            } else if (anchorTo.isRight()) {
                x -= widget.getWidth();
            }

            x += offset.get(0);

            this.widget.setX(x + this.offsetX.recalculateLength(parW, widget.getDefaultSize()));
            return this;
        }

        public HJNElementWrapper<T> setY(int parY, int parH) {
            int y = parY;

            if (anchorFrom.isHMiddle()) {
                y += parH / 2;
            } else if (anchorFrom.isBottom) {
                y = parH;
            }

            if (anchorTo.isHMiddle()) {
                y -= widget.getHeight() / 2;
            } else if (anchorTo.isBottom) {
                y -= widget.getHeight();
            }

            this.widget.setY(y + offset.get(1) + this.offsetY.recalculateLength(parH, widget.getDefaultSize()));
            return this;
        }

        public HJNElementWrapper<T> setWidth(int parW) {
            this.widget.setWidth(this.sizeWidth.recalculateLength(parW, widget.getDefaultSize()));
            return this;
        }

        public HJNElementWrapper<T> setHeight(int parH) {
            this.widget.setHeight(this.sizeHeight.recalculateLength(parH, widget.getDefaultSize()));
            return this;
        }

        public HJNElementWrapper<T> setAnchorFrom(Anchor anchorFrom) {
            this.anchorFrom = anchorFrom;
            return this;
        }

        public HJNElementWrapper<T> setAnchorTo(Anchor anchorTo) {
            this.anchorTo = anchorTo;
            return this;
        }

        public HJNElementWrapper<T> setAnchor(Anchor anchorFrom, Anchor anchorTo) {
            this.anchorFrom = anchorFrom;
            this.anchorTo = anchorTo;
            return this;
        }

        public HJNElementWrapper<T> setOffset(int x, int y) {
            this.offset.set(x, y);
            return this;
        }

        public HJNElementWrapper<T> setOffsetX(LengthCollector offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public HJNElementWrapper<T> setOffsetY(LengthCollector offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public HJNElementWrapper<T> setSizeWidth(LengthCollector sizeWidth) {
            this.sizeWidth = sizeWidth;
            return this;
        }

        public HJNElementWrapper<T> setSizeHeight(LengthCollector sizeHeight) {
            this.sizeHeight = sizeHeight;
            return this;
        }

        public Anchor getAnchorFrom() {
            return anchorFrom;
        }

        public Anchor getAnchorTo() {
            return anchorTo;
        }

        public Vector2i getOffset() {
            return offset;
        }

        public T getWidget() {
            return widget;
        }

        public int getWidth() {
            return widget.getWidth();
        }

        public int getHeight() {
            return widget.getHeight();
        }
    }

    public static class Rect {
        private int left;
        private int top;
        private int right;
        private int bottom;

        public Rect setX(int x) {
            this.left = x;
            return this;
        }

        public Rect setY(int y) {
            this.top = y;
            return this;
        }

        public Rect setWidth(int width) {
            return this.setRight(this.left + width);
        }

        public Rect setHeight(int height) {
            return this.setBottom(this.top + height);
        }

        public Rect setLeft(int left) {
            this.left = left;
            return this;
        }

        public Rect setTop(int top) {
            this.top = top;
            return this;
        }

        public Rect setRight(int right) {
            this.right = right;
            return this;
        }

        public Rect setBottom(int bottom) {
            this.bottom = bottom;
            return this;
        }

        public int getX() {
            return this.left;
        }

        public int getY() {
            return this.top;
        }

        public int getWidth() {
            return this.right - this.left;
        }

        public int getHeight() {
            return this.bottom - this.top;
        }
    }
}
