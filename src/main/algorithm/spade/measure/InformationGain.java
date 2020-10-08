package main.algorithm.spade.measure;

import main.algorithm.spade.structure.EquivalenceClass;
import main.algorithm.spade.structure.IdList;
import main.algorithm.spade.structure.Sequence;
import main.dataset.DatasetWithLabels;
import main.pattern.IPattern;

import java.util.*;

public class InformationGain<L> extends Measure<Double> {
    private HashMap<Integer, L> labels;
    private Integer datasetSize;


    public InformationGain(HashMap<Integer, L> labels, Integer datasetSize) {
        this.labels = labels;
        this.datasetSize = datasetSize;
    }

    public InformationGain(DatasetWithLabels<?, L> datasetWithLabels){
        this((HashMap<Integer, L>) datasetWithLabels.getLabels(), datasetWithLabels.size());
    }

    @Override
    public Double computeConcreteValue(EquivalenceClass equivalenceClass) {
        return null;
    }

    @Override
    public Double computePotentialValue(EquivalenceClass equivalenceClass) {
        return null;
    }

    private Double computeEntropy(double p){
        return -p*Math.log(p) - (1 - p)*Math.log(1 - p);
    }

    Double computeEntropy(int x, int y, Sequence sequence){
        int n = this.datasetSize;
        int oi_ = n - x;
        int oc = Collections.frequency(this.labels.values(), getSequenceClass(sequence));
        int oi_c = oc - y;
        return computeEntropy(oc/n)
                - x/n * computeEntropy(y/x)
                - oi_/n*computeEntropy(oi_c/oi_);
    }
    Double computeEntropy(Sequence sequence){
        int n = this.datasetSize;
        int x = sequence.getIdList().size();
        int y = Collections.frequency(sequence.getIdList().keySet(), getSequenceClass(sequence));
        return computeEntropy(x, y, sequence);
    }

    Double computeUpperBound(Sequence sequence){
        int n = this.datasetSize;
        int x = sequence.getIdList().size();
        int y = Collections.frequency(sequence.getIdList().keySet(), getSequenceClass(sequence));
        return Math.max(computeEntropy(y, y, sequence), computeEntropy(x-y, 0, sequence));
    }

    private L getSequenceClass(Sequence sequence){
        List<L> labels = new ArrayList<>();
        for (int sid : sequence.getIdList().keySet()){
            labels.add(this.labels.get(sid));
        }

        Set<L> distinct = new HashSet<>(labels);
        L label = null;
        int max = -1;

        for (L l: distinct) {
            int cnt = Collections.frequency(labels, l);
			if (cnt > max){
			    label = l;
			    max = cnt;
            }
		}

        return label;
    }
}
