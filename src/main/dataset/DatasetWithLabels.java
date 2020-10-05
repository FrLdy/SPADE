package main.dataset;

import main.pattern.IPattern;

import java.util.HashMap;

public class DatasetWithLabels<P extends IPattern, N extends Number> extends Dataset<P> {
    private HashMap<Integer, N> labels = new HashMap<>();

    public boolean add(P p, Integer id, N label) {
        labels.putIfAbsent(id, label);
        return super.add(p);
    }
}
