package me.kalmemarq.hjnutility.client.gui.widget;

public interface HJNWidget {
    void setHeight(int height);
    default int getDefaultSize() {
        return 0;
    }
}
