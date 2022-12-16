package me.kalmemarq.hjnutility.util;

import me.kalmemarq.hjnutility.HJNConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HJNCrosshair {
    public static final Map<HJNConfig.CrosshairModifier, Integer> CROSSHAIR_COLOR = new HashMap<>();
    public static final List<HJNCrosshair> CROSSHAIRS = new ArrayList<>();

    private int width;
    private int height;
    private int u;
    private int v;

    private HJNCrosshair(int width, int height, int u, int v) {
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    static {
        CROSSHAIRS.clear();
        CROSSHAIRS.add(new HJNCrosshair(15, 15, 0, 0)); // Dummy for the Default Crosshair
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 242, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 227, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 212, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 197, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 182, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 167, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 152, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 137, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 122, 242));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 242, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 227, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 212, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 197, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 182, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 167, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 152, 227));
        CROSSHAIRS.add(new HJNCrosshair(13, 13, 137, 227));
        CROSSHAIRS.add(new HJNCrosshair(14, 14, 241, 207));
        CROSSHAIRS.add(new HJNCrosshair(14, 14, 225, 207));
        CROSSHAIRS.add(new HJNCrosshair(14, 14, 209, 207));
        CROSSHAIR_COLOR.clear();
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.White, 0xFF_FFFFFF);
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.Red, 0xFF_FF5555);
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.Green, 0xFF_55FF55);
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.Blue, 0xFF_5555FF);
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.Yellow, 0xFF_FFAA00);
        CROSSHAIR_COLOR.put(HJNConfig.CrosshairModifier.Aqua, 0xFF_55FFFF);
    }
}
