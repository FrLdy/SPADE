package main.algorithm.spade;

import main.algorithm.spade.measure.Frequency;
import main.algorithm.spade.measure.Measure;
import main.algorithm.spade.structure.EquivalenceClass;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;
import main.pattern.Item;
import main.pattern.Itemset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cSPADE<T extends Comparable<? super T>> {
    private boolean cMinGap, cMaxGap;
    private int mingap, maxgap;
    private Double minSup;
    private Dataset<Sequence<T>> entryDataset;
    private Dataset<Sequence<T>> resultDataset;
    private EquivalenceClass<T> root = new EquivalenceClass<>();
    private Measure<Double> measure;
    private CandidateGenerator<T> candidateGenerator;
    private List<Sequence<T>> oneSequences = new ArrayList<>();
    private List<Sequence<T>> twoSequences = new ArrayList<>();
    private HashMap<Item<T>, ArrayList<EquivalenceClass<T>>> twoSeqsIndexedByPrefix = new HashMap<>();
    private boolean dfs;

    public cSPADE(Double minSup, Dataset<Sequence<T>> entryDataset, Dataset<Sequence<T>> resultDataset, boolean dfs,
                  Integer mingap, Integer maxgap) {
        this.minSup = minSup;
        this.entryDataset = entryDataset;
        this.resultDataset = resultDataset;
        this.measure = new Frequency(entryDataset.size());
        this.dfs = dfs;
        this.mingap = mingap;
        this.maxgap = maxgap;
        this.cMinGap = mingap > -1;
        this.cMaxGap = maxgap > -1;
        this.candidateGenerator = new CandidateGenerator<>(cMinGap, cMaxGap, mingap, maxgap);
    }

    public cSPADE(Double minSup, Dataset<Sequence<T>> entryDataset, boolean dfs) {
        this(minSup, entryDataset, new Dataset<>(), dfs, -1, -1);
    }

    public void setcMinGap(boolean cMinGap) {
        this.cMinGap = cMinGap;
        this.candidateGenerator.setcMinGap(cMinGap);
    }

    public void setcMaxGap(boolean cMaxGap) {
        this.cMaxGap = cMaxGap;
        this.candidateGenerator.setcMaxGap(cMaxGap);
    }

    public void setMingap(int mingap) {
        this.mingap = mingap;
        this.candidateGenerator.setMingap(mingap);
    }

    public void setMaxgap(int maxgap) {
        this.maxgap = maxgap;
        this.candidateGenerator.setMaxgap(maxgap);
    }

    void computeOneSequences(){
        HashMap<Item<T>, Sequence<T>> res = new HashMap<>();

        for (int sid = 0 ; sid < this.entryDataset.size(); sid++){

            Sequence<T> sequence = this.entryDataset.get(sid);

            for (int eid = 0; eid < sequence.size(); eid++){

                Itemset<T> itemset = sequence.get(eid);
                for (Item<T> item : itemset){
                    res.putIfAbsent(item, new Sequence<>(item));
                    res.get(item).getIdList().add(sequence.getId(), eid);
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
            if (manageNewSequence(cand, this.root.getMembers().get(i))){
                this.twoSequences.add(cand);
                if (cMaxGap){
                    Item<T> prefix = cand.getFirstItem();
                    twoSeqsIndexedByPrefix.putIfAbsent(prefix, new ArrayList<>());
                    twoSeqsIndexedByPrefix.get(prefix).add(new EquivalenceClass<>(cand));
                }
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
                    if (manageNewSequence(cand, parent)){
                        this.twoSequences.add(cand);
                        if (cMaxGap){
                            Item<T> prefix = cand.getFirstItem();
                            twoSeqsIndexedByPrefix.putIfAbsent(prefix, new ArrayList<>());
                            twoSeqsIndexedByPrefix.get(prefix).add(new EquivalenceClass<>(cand));
                        }
                    }
                }
            }
        }
    }

    public Double getMinSup() {
        return minSup;
    }

    public int getMaxgap() {
        return maxgap;
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
        if (!equivalenceClass.getMembers().isEmpty()){
            for (EquivalenceClass<T> e : equivalenceClass.getMembers()){
                printEqCl(e);
            }
        }
    }

    public void setMinSup(Double minSup) {
        this.minSup = minSup;
    }

    private boolean manageNewSequence(Sequence<T> sequence, EquivalenceClass<T> eqc1, EquivalenceClass<T> eqc2){
        EquivalenceClass<T> newEqC = new EquivalenceClass<>(sequence);

        Double sup = this.measure.computePotentialValue(newEqC);
        sequence.setSupport(sup);
        boolean keep = sup >= this.getMinSup();
//        System.out.println(sequence + " keep");
        if (keep){
            sequence.setSupport(sup);
            ((sequence.getPrefix().equals(eqc1.sequence)) ? eqc1 : eqc2).add(newEqC);
            resultDataset.add(sequence);
        }
        return keep;
    }

    private boolean manageNewSequence(Sequence<T> sequence, EquivalenceClass<T> eqc){
        return this.manageNewSequence(sequence, eqc, eqc);
    }

    public void enumerateSeq(EquivalenceClass<T> equivalenceClass, boolean remove){
        int size = equivalenceClass.size();
        ArrayList<Sequence<T>> candidates;

        for (int i = 0; i < size; i++){

            EquivalenceClass<T> eqC1 = equivalenceClass.get(i);
            List<EquivalenceClass<T>> eqC2s;
            if (cMaxGap){
                Item<T> suffix = eqC1.sequence.getLastItem();
                eqC2s = twoSeqsIndexedByPrefix.get(suffix);
            } else {
                eqC2s = equivalenceClass.subList(i, equivalenceClass.size());
            }

            for (EquivalenceClass<T> eqC2 : eqC2s){
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

    public HashMap<Item<T>, ArrayList<EquivalenceClass<T>>> getTwoSeqsIndexedByPrefix() {
        return twoSeqsIndexedByPrefix;
    }

    public CandidateGenerator<T> getCandidateGenerator() {
        return candidateGenerator;
    }

    public List<Sequence<T>> getOneSequences() {
        return oneSequences;
    }

    public void setEntryDataset(Dataset<Sequence<T>> entryDataset) {
        this.entryDataset = entryDataset;
    }

    public void setMeasure(Measure<Double> measure) {
        this.measure = measure;
    }

    public Dataset<Sequence<T>> getEntryDataset() {
        return entryDataset;
    }
}
