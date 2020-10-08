package main.dataset;

import main.pattern.IPattern;

import java.util.HashMap;

public class DatasetWithLabels<P extends IPattern, L> extends Dataset<P> {
    private HashMap<Integer, L> labels = new HashMap<>();

    public boolean add(P p, Integer id, L label) {
        labels.putIfAbsent(id, label);
        return super.add(p);
    }

    public HashMap<Integer, L> getLabels() {
        return labels;
    }
}
