package main.dataset;

import main.pattern.IPattern;

import java.util.HashMap;

public class DatasetWithLabels<P extends IPattern, N extends Number> extends Dataset<P> {
    private HashMap<P, N> labels = new HashMap<>();

    public boolean add(P p, N label) {
        labels.putIfAbsent(p, label);
        return super.add(p);
    }
}
