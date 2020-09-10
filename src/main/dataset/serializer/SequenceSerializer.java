package main.dataset.serializer;

import main.dataset.Dataset;
import main.pattern.Itemset;
import main.pattern.Sequence;

public abstract class SequenceSerializer<S extends Sequence, T extends Comparable<? super T>> extends Serializer<S>{
    /**
     * General constructor.
     *
     * @param path the path of the data file.
     */
    protected SequenceSerializer(String path) {
        super(path);
    }

    public abstract String itemsetToString(Itemset<T> itemset);
}
