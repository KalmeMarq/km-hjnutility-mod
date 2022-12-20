package me.kalmemarq.hjnutility.util.nonsense;

import me.kalmemarq.hjnutility.util.HJNUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LengthCollector {
    private final List<Length> lengths;

    public static LengthCollector of(Length...lengths) {
        return new LengthCollector(lengths);
    }

    public LengthCollector(Length...lengths) {
        this.lengths = new ArrayList<>();
        this.lengths.addAll(Arrays.asList(lengths));
    }

    public int recalculateLength(int parSize, int elDefaultSize) {
        float len = 0;

        Iterator<Length> iter = lengths.iterator();

        while (iter.hasNext()) {
            Length it = iter.next();

            if (it.isPixel()) {
                len += it.getValue();
            } else if (it.isRelative()) {
                len += it.getValue() * parSize;
            } else if (it.isDefault()) {
                len += elDefaultSize;
            }
        }

        return (int)len;
    }
}
