package main.algorithm.spade.cspade;

import main.algorithm.spade.spade.SPADE;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;

public class cSPADE<T extends Comparable<? super T>> extends SPADE<T> {
    boolean maxGapConst;
    int maxGap;

    public cSPADE(Double minSup, Dataset<Sequence<T>> entryDataset, Dataset<Sequence<T>> resultDataset, boolean dfs,
                  int maxGap) {
        super(minSup, entryDataset, resultDataset, dfs);
        this.maxGap = maxGap;
        this.maxGapConst = maxGap > -1;
    }

    public cSPADE(Double minSup, Dataset<Sequence<T>> entryDataset, boolean dfs) {
        super(minSup, entryDataset, dfs);
    }
}
