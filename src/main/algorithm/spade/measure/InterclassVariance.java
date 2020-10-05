package main.algorithm.spade.measure;

import main.algorithm.spade.structure.EquivalenceClass;
import main.algorithm.spade.structure.IdList;
import main.algorithm.spade.structure.Sequence;
import main.dataset.IDataset;

import java.util.*;

public class InterclassVariance extends Measure<Double> {

    private final int datasetSize;
    private final HashMap<Integer, Double> labels;
    private final Double labelsSum;

    public InterclassVariance(int datasetSize, HashMap<Integer, Double> labels, Double labelsSum) {
        this.datasetSize = datasetSize;
        this.labels = labels;
        this.labelsSum = labelsSum;
    }

    /**
     *
     * @param x the number of database sequences which cover the candidate
     *          (is a double to skip the problem of division by 0 by dividing by 10^-n).
     * @param y the sum of the labels of the sequences which cover the candidate.
     * @return
     */
    private Double computeIntercalssVariance(double x, double y){
        Double p1 =  x*Math.pow((this.labelsAverage() - y/x), 2);
        Double p2 = (this.datasetSize - x) * Math.pow((labelsAverage() - (labelsSum-y)/(this.datasetSize-x)),2);
        return p1 + p2;
    }

    @Override
    public Double computeConcreteValue(EquivalenceClass equivalenceClass) {
        IdList eqClassIdList = equivalenceClass.getSequence().getIdList();
        return this.computeIntercalssVariance(eqClassIdList.size(), sequenceLabelsSum(eqClassIdList.keySet()));
    }

    @Override // UpperBound
    public Double computePotentialValue(EquivalenceClass equivalenceClass) {
        return computeUpperBound(equivalenceClass.sequence);
    }

    public Double computeUpperBound(Sequence sequence){
        List<Double> labels = this.sequenceLabels(sequence.getIdList().keySet());
        Collections.sort(labels);
        Double z = labels.get(0);

        double x1 = 0.0000001;
        double y1 = 0.0000001;
        double x2 = labels.size();
        double y2 = this.sum(labels);

        Double max = Math.max(this.computeIntercalssVariance(x1, y1), this.computeIntercalssVariance(x2, y2));
        for (int i = 1 ; i < labels.size(); i++){
            Double label = labels.get(i);
            x1++;
            y1 += labels.get(i-1);
            x2--;
            y2 -= labels.get(i-1);
            if (label > z){
                z = label;
                max = Math.max(Math.max(max, this.computeIntercalssVariance(x1, y1)), this.computeIntercalssVariance(x2, y2));
            }
        }
        return max;
    }

    private List<Double> sequenceLabels(Collection<Integer> sids){
        List<Double> res = new ArrayList<>();
        for (Integer sid : sids){
            res.add(this.labels.get(sid));
        }
        return res;
    }

    private Double sequenceLabelsSum(Collection<Integer> sids){
        Double res = 0.0;
        for (Integer i : sids){
            res += this.labels.get(i);
        }
        return res;
    }

    private Double sum(Collection<Double> numbers){
        double res = 0.0;
        for (Number number : numbers) {
            res += (double) number;
        }
        return res;
    }

    private Double labelsAverage(){
        return this.labelsSum / this.datasetSize;
    }
}
