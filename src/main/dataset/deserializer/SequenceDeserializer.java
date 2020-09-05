package main.dataset.deserializer;

import main.dataset.deserializer.Deserializer;
import main.pattern.Itemset;
import main.pattern.Sequence;

public abstract class SequenceDeserializer<S extends Sequence, T extends Comparable<? super T>> extends Deserializer<S> {
    public SequenceDeserializer(String path) {
        super(path);
    }

    public abstract Itemset<T> stringToItemset(String itemsetString);
}
