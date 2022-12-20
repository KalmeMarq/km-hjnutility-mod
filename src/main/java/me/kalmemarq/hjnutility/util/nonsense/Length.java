package me.kalmemarq.hjnutility.util.nonsense;

public abstract class Length {
    private final float value;

    Length(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public boolean isDefault() {
        return false;
    }

    public boolean isPixel() {
        return false;
    }

    public boolean isRelative() {
        return false;
    }

    public static Length lenDefault() {
        return new Length(0) {
            @Override
            public boolean isDefault() {
                return true;
            }
        };
    }

    public static Length pixel(int value) {
        return new Length(value) {
            @Override
            public boolean isPixel() {
                return true;
            }
        };
    }

    public static Length relative(float value) {
        return new Length(value) {
            @Override
            public boolean isRelative() {
                return true;
            }
        };
    }
}
