package main.algorithm.spade;

import main.algorithm.measure.Frequency;
import main.algorithm.measure.IMeasure;
import main.dataset.Dataset;
import main.algorithm.spade.deserializer.SequenceSPMFDeserializer;
import main.dataset.IDataset;
import main.pattern.Item;
import main.pattern.Itemset;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SPADE<T extends Comparable<? super T>> {
    Double minSup;
    public Dataset<Sequence<T>> entryDataset;
    private Dataset<Sequence<T>> outputDataset;
    private EquivalenceClass root = new EquivalenceClass();
    private IMeasure<Double> measure;
    private CandidateGenerator<T> candidateGenerator = new CandidateGenerator<>();
    private List<Sequence<T>> oneSequences = new ArrayList<>();
    private List<Sequence<T>> twoSequences = new ArrayList<>();


    public SPADE(Double minSup, Dataset<Sequence<T>> entryDataset, Dataset<Sequence<T>> outputDataset) {
        this.minSup = minSup;
        this.entryDataset = entryDataset;
        this.outputDataset = outputDataset;
        this.measure = new Frequency(entryDataset.size());
    }

    public SPADE(Double minSup, Dataset<Sequence<T>> entryDataset) {
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
            if (keepSeq(candidateEqC)){
                this.root.addMember(candidateEqC);
                this.oneSequences.add(s);
            }
        }
    }

    private void computeTwoSequences(){
        Sequence<T> s1, s2, cand;
        EquivalenceClass candidateEqC;
        for (int i = 0; i < oneSequences.size(); i++){
            // <s1> <s1>;
            s1 = oneSequences.get(i);
            cand = candidateGenerator.temporalJoin(s1, s1);
            candidateEqC = new EquivalenceClass(cand);
            if (keepSeq(candidateEqC)){
                this.root.getMembers().get(i).addMember(candidateEqC);
                this.twoSequences.add(cand);
            }

            for (int j = i+1; j < oneSequences.size(); j++){
                s2 = oneSequences.get(j);

                // <s1, s2>
                cand = candidateGenerator.equalityJoin(s1, s2);
                candidateEqC = new EquivalenceClass(cand);
                if (keepSeq(candidateEqC)){
                    this.root.getMembers().get(i).addMember(candidateEqC);
                    this.twoSequences.add(cand);
                }

                // <s1>, <s2>
                cand = candidateGenerator.temporalJoin(s1, s2);
                candidateEqC = new EquivalenceClass(cand);
                if (keepSeq(candidateEqC)){
                    this.root.getMembers().get(i).addMember(candidateEqC);
                    this.twoSequences.add(cand);
                }

                // <s2>, <s2>
                cand = candidateGenerator.temporalJoin(s2, s1);
                candidateEqC = new EquivalenceClass(cand);
                if (keepSeq(candidateEqC, true)){
                    this.root.getMembers().get(j).addMember(candidateEqC);
                    this.twoSequences.add(cand);
                }
            }
        }
    }

    public Double getMinSup() {
        return minSup;
    }

    private boolean keepSeq(EquivalenceClass equivalenceClass, boolean verbose){
        Double sup = this.measure.computePotentialValue(equivalenceClass);
        if (verbose){
            System.out.println(equivalenceClass.sequence + " " + sup);
        }
        return sup >= getMinSup();
    }

    private boolean keepSeq(EquivalenceClass equivalenceClass){
        return this.keepSeq(equivalenceClass, false);
    }

    private void printEqCl(EquivalenceClass equivalenceClass){
        if (!equivalenceClass.getMembers().isEmpty()){
            for (EquivalenceClass e : equivalenceClass.getMembers()){
                printEqCl(e);
            }
        }
    }

    public static void main(String[] args) {
        Dataset<Sequence<String>> dataset = (Dataset<Sequence<String>>) new SequenceSPMFDeserializer<String>("data/spadeTest1.txt").deserialize();
        SPADE<String> spade = new SPADE<String>(0.5, dataset);
        spade.computeOneSequences();
        spade.computeTwoSequences();
        spade.printEqCl(spade.root);
    }
}
