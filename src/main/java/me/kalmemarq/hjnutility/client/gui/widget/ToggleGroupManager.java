package me.kalmemarq.hjnutility.client.gui.widget;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ToggleGroupManager {
    private final List<ToggleGroupItem<?>> toggles;
    @Nullable
    private ToggleSelectChanged selectChanged;
    private int currentSelectedIndex = -1;

    public ToggleGroupManager() {
        this.toggles = new ArrayList<>();
    }

    public void setSelectionConsumer(@Nullable ToggleSelectChanged selectChanged) {
        this.selectChanged = selectChanged;
    }

    public void selectToggleGroupIndex(int groupIndex) {
        if (this.currentSelectedIndex == groupIndex) return;

        Iterator<ToggleGroupItem<?>> iter = this.toggles.iterator();

        while (iter.hasNext()) {
            ToggleGroupItem<?> item = iter.next();

            if (item.getGroupIndex() == groupIndex) {
                item.getToggle().setToggled(true);
                currentSelectedIndex = groupIndex;

                if (this.selectChanged != null) {
                    this.selectChanged.apply(item.getGroupIndex(), item.getToggle());
                }
            } else {
                item.getToggle().setToggled(false);
            }
        }
    }

    public int getCurrentSelectedIndex() {
        return currentSelectedIndex;
    }

    public void clearReset() {
        this.currentSelectedIndex = -1;
        this.toggles.clear();
    }

    public <T extends HJNToggle> T add(int groupIndex, T toggle) {
        toggle.setOnPress(widget -> {
            selectToggleGroupIndex(groupIndex);
        });
        this.toggles.add(new ToggleGroupItem<>(groupIndex, toggle));
        return toggle;
    }

    @FunctionalInterface
    public interface ToggleSelectChanged {
        void apply(int toggleGroupIndex, HJNToggle toggle);
    }

    public static class ToggleGroupItem<T extends HJNToggle> {
        private final int groupIndex;
        private final T toggle;

        ToggleGroupItem(int groupIndex, T toggle) {
            this.groupIndex = groupIndex;
            this.toggle = toggle;
        }

        public int getGroupIndex() {
            return groupIndex;
        }

        public T getToggle() {
            return toggle;
        }
    }
}
