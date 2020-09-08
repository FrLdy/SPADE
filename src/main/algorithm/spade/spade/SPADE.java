package main.algorithm.spade.spade;

import main.algorithm.spade.measure.Frequency;
import main.algorithm.spade.measure.IMeasure;
import main.algorithm.spade.structure.EquivalenceClass;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;
import main.pattern.Item;
import main.pattern.Itemset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPADE<T extends Comparable<? super T>> {
    private Double minSup;
    private Dataset<Sequence<T>> entryDataset;
    private Dataset<Sequence<T>> resultDataset;
    private EquivalenceClass<T> root = new EquivalenceClass<>();
    private IMeasure<Double> measure;
    private CandidateGenerator<T> candidateGenerator = new CandidateGenerator<>();
    private List<Sequence<T>> oneSequences = new ArrayList<>();
    private List<Sequence<T>> twoSequences = new ArrayList<>();
    private boolean dfs;

    public SPADE(Double minSup, Dataset<Sequence<T>> entryDataset, Dataset<Sequence<T>> resultDataset, boolean dfs) {
        this.minSup = minSup;
        this.entryDataset = entryDataset;
        this.resultDataset = resultDataset;
        this.measure = new Frequency(entryDataset.size());
        this.dfs = dfs;
    }

    public SPADE(Double minSup, Dataset<Sequence<T>> entryDataset, boolean dfs) {
        this(minSup, entryDataset, new Dataset<>(), dfs);
    }

    void computeOneSequences(){
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
            if (manageNewSequence(s, root, root)){
                this.oneSequences.add(s);
            }
        }
    }

    void computeTwoSequences(){
        Sequence<T> s1;
        Sequence<T> s2;
        Sequence<T> cand;
        EquivalenceClass<T> candidateEqC;
        for (int i = 0; i < oneSequences.size(); i++){
            // <s1> <s1>;
            s1 = oneSequences.get(i);
            cand = candidateGenerator.temporalJoin(s1, s1);
            candidateEqC = new EquivalenceClass<>(cand);
            if (keepSeq(candidateEqC)){
                this.root.getMembers().get(i).addMember(candidateEqC);
                this.twoSequences.add(cand);
                this.resultDataset.add(cand);
            }
            for (int j = i+1; j < oneSequences.size(); j++){
                s2 = oneSequences.get(j);
                Sequence[] candidates = new Sequence[]{
                        candidateGenerator.equalityJoin(s1, s2),// <s1, s2>
                        candidateGenerator.temporalJoin(s1, s2),// <s1>, <s2>
                        candidateGenerator.temporalJoin(s2, s1)// <s2>, <s2>
                };
                int[] indices = new int[]{i, i, j};
                for (int k=0; k < candidates.length; k++){
                    cand = candidates[k];
                    EquivalenceClass<T> parent = this.root.getMembers().get(indices[k]);
                    if (manageNewSequence(cand, parent, parent)){
                        this.twoSequences.add(cand);
                    }
                }
            }
        }
    }

    public Double getMinSup() {
        return minSup;
    }

    private boolean keepSeq(EquivalenceClass<T> equivalenceClass, boolean verbose){
        Double sup = this.measure.computePotentialValue(equivalenceClass);
        if (verbose){
            System.out.println(equivalenceClass.sequence + " " + sup);
        }
        return sup >= getMinSup();
    }

    private boolean keepSeq(EquivalenceClass<T> equivalenceClass){
        return this.keepSeq(equivalenceClass, false);
    }

    private void printEqCl(EquivalenceClass<T> equivalenceClass){
        System.out.println(equivalenceClass);
        if (!equivalenceClass.getMembers().isEmpty()){
            for (EquivalenceClass<T> e : equivalenceClass.getMembers()){
                printEqCl(e);
            }
        }
    }

    private boolean manageNewSequence(Sequence<T> sequence, EquivalenceClass<T> eqc1, EquivalenceClass<T> eqc2){
        EquivalenceClass<T> newEqC = new EquivalenceClass<>(sequence);

        Double sup = this.measure.computePotentialValue(newEqC);
        boolean keep = sup >= this.getMinSup();
        if (keep){
            sequence.setSupport(sup);
            ((sequence.getPrefix().equals(eqc1.sequence)) ? eqc1 : eqc2).add(newEqC);
            resultDataset.add(sequence);
        }
        return keep;
    }

    public void enumerateSeq(EquivalenceClass<T> equivalenceClass, boolean remove){
        int size = equivalenceClass.size();
        ArrayList<Sequence<T>> candidates;

        for (int i = 0; i < size; i++){

            EquivalenceClass<T> eqC1 = equivalenceClass.get(i);

            for (int j = i; j < size; j++){
                EquivalenceClass<T> eqC2 = equivalenceClass.get(j);

                candidates = candidateGenerator.genCandidates(eqC1.sequence, eqC2.sequence);

                for (Sequence<T> cand : candidates){
                    manageNewSequence(cand, eqC1, eqC2);
                }
            }
            if (dfs){
                if (remove) equivalenceClass.remove(i);
                enumerateSeq(eqC1, remove);
            }
        }
        if (!dfs){
            for (int i = equivalenceClass.size() - 1; i >= 0; i--) {
                enumerateSeq(equivalenceClass.get(i), remove);
                if (remove){
                    equivalenceClass.remove(i);
                }
            }
        }
    }

    public void enumerateSeq(EquivalenceClass<T> equivalenceClass){
        this.enumerateSeq(equivalenceClass, false);
    }

    public void run(){
        this.computeOneSequences();
        this.computeTwoSequences();
        for (EquivalenceClass<T> eqC1seq : this.root){
            this.enumerateSeq(eqC1seq, true);
        }
    }

    public Dataset<Sequence<T>> getResultDataset() {
        return this.resultDataset;
    }
}