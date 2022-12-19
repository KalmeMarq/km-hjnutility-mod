package me.kalmemarq.hjnutility.util;

public enum Anchor {
    TOP_LEFT(true, false, true, false, false),
    TOP_MIDDLE(false, false, true, false, false),
    TOP_RIGHT(false, true, true, false, false),
    LEFT_MIDDLE(true, false, false, false, true),
    CENTER(false, false, false, false, true),
    RIGHT_MIDDLE(false, true, false, false, true),
    BOTTOM_LEFT(true, false, false, true, false),
    BOTTOM_MIDDLE(false, false, false, true, false),
    BOTTOM_RIGHT(false, true, false, true, false);

    final boolean isLeft;
    final boolean isRight;
    final boolean isTop;
    final boolean isBottom;
    final boolean isMiddle;

    Anchor(boolean isLeft, boolean isRight, boolean isTop, boolean isBottom, boolean isMiddle) {
        this.isLeft = isLeft;
        this.isRight = isRight;
        this.isTop = isTop;
        this.isBottom = isBottom;
        this.isMiddle = isMiddle;
    }

    public boolean isVMiddle() {
        return !isLeft && !isRight;
    }

    public boolean isHMiddle() {
        return isMiddle;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public boolean isRight() {
        return isRight;
    }

    public boolean isTop() {
        return isTop;
    }

    public static int getX(int elW, int parX, int parW, Anchor anchorFrom, Anchor anchorTo) {
        int x = parX;

        if (anchorFrom.isVMiddle()) {
            x += parW / 2;
        } else if (anchorFrom.isRight()) {
            x += parW;
        }

        if (anchorTo.isVMiddle()) {
            x -= elW / 2;
        } else if (anchorTo.isRight()) {
            x -= elW;
        }

        return x;
    }

    public static int getY(int elH, int parY, int parH, Anchor anchorFrom, Anchor anchorTo) {
        int y = parY;

        if (anchorFrom.isHMiddle()) {
            y += parH / 2;
        } else if (anchorFrom.isBottom()) {
            y += parH;
        }

        if (anchorTo.isHMiddle()) {
            y -= elH / 2;
        } else if (anchorTo.isBottom()) {
            y -= elH;
        }

        return y;
    }
}