package main.algorithm.spade;

import main.algorithm.measure.IMeasure;
import main.dataset.Dataset;
import main.pattern.Item;
import main.pattern.Itemset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPADE<T extends Comparable<? super T>> {
    float minSup;
    public Dataset<Sequence<T>> entryDataset;
    Dataset<Sequence<T>> outputDataset;
    EquivalenceClass root;
    IMeasure<Double> measure;
    List<Sequence<T>> oneSequences = new ArrayList<>();
    List<Sequence<T>> twoSequences = new ArrayList<>();


    public SPADE(float minSup, Dataset<Sequence<T>> entryDataset, Dataset<Sequence<T>> outputDataset) {
        this.minSup = minSup;
        this.entryDataset = entryDataset;
        this.outputDataset = outputDataset;
    }

    public SPADE(float minSup, Dataset<Sequence<T>> entryDataset) {
        this(minSup, entryDataset, new Dataset<>());
    }

    private void computeOneSequences(){
        HashMap<Item<T>, Sequence<T>> res = new HashMap<>();

        for (int sid = 0 ; sid < this.entryDataset.size(); sid++){

            Sequence<T> sequence = this.entryDataset.get(sid);

            for (int eid = 0; eid < sequence.size(); eid++){

                Itemset<T> itemset = sequence.get(eid);
                for (Item<T> item : itemset){
                    res.putIfAbsent(item, new Sequence<>(item));
                    res.get(item).getIdList().add(sid, eid);
                }
            }
        }
        for (Item<T> key : res.keySet()){
            Sequence<T> s = res.get(key);
            EquivalenceClass candidateEqC = new EquivalenceClass(s);
            if (this.measure.computePotentialValue(candidateEqC) >= minSup){
                this.root.addMember(new EquivalenceClass(s));
                this.oneSequences.add(s);
            }
        }
    }

    public float getMinSup() {
        return minSup;
    }
}
