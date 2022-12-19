package me.kalmemarq.hjnutility.util;

import net.minecraft.client.gui.widget.ClickableWidget;
import org.joml.Vector2i;

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

    public static class HJNElementWrapper<T extends ClickableWidget> {
        private T widget;
        private Vector2i offset = new Vector2i();
        private Anchor anchorFrom = Anchor.CENTER;
        private Anchor anchorTo = Anchor.CENTER;

        public HJNElementWrapper(T widget) {
            this.widget = widget;
        }

        public HJNElementWrapper setX(int parX, int parW) {
            return setX(parX, parW, true);
        }

        public HJNElementWrapper setX(int parX, int parW, boolean allowOffset) {
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

            if (allowOffset) {
                x += offset.get(0);
            }

            this.widget.setX(x);
            return this;
        }

        public HJNElementWrapper setY(int parY, int parH) {
            return setY(parY, parH, true);
        }

        public HJNElementWrapper setY(int parY, int parH, boolean allowOffset) {
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

            this.widget.setY(y + offset.get(1));
            return this;
        }

        public HJNElementWrapper setAnchorFrom(Anchor anchorFrom) {
            this.anchorFrom = anchorFrom;
            return this;
        }

        public HJNElementWrapper setAnchorTo(Anchor anchorTo) {
            this.anchorTo = anchorTo;
            return this;
        }

        public HJNElementWrapper setAnchor(Anchor anchorFrom, Anchor anchorTo) {
            this.anchorFrom = anchorFrom;
            this.anchorTo = anchorTo;
            return this;
        }

        public HJNElementWrapper setOffset(int x, int y) {
            this.offset.set(x, y);
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
