package me.kalmemarq.hjnutility.util;

public enum Anchor {
    TOP_LEFT(true, false, true, false),
    TOP_MIDDLE(false, false, true, false),
    TOP_RIGHT(false, true, true, false),
    LEFT_MIDDLE(true, false, false, false),
    CENTER(false, false, false, false),
    RIGHT_MIDDLE(false, true, false, false),
    BOTTOM_LEFT(true, false, false, true),
    BOTTOM_MIDDLE(false, false, false, true),
    BOTTOM_RIGHT(false, true, false, true);

    final boolean isLeft;
    final boolean isRight;
    final boolean isTop;
    final boolean isBottom;

    Anchor(boolean isLeft, boolean isRight, boolean isTop, boolean isBottom) {
        this.isLeft = isLeft;
        this.isRight = isRight;
        this.isTop = isTop;
        this.isBottom = isBottom;
    }

    public boolean isVMiddle() {
        return !isLeft && !isRight;
    }

    public boolean isHMiddle() {
        return !isTop && !isBottom;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public boolean isRight() {
        return isRight;
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