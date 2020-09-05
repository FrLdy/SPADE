package main.algorithm.measure;

import main.algorithm.spade.EquivalenceClass;

public class Frequency implements IMeasure<Double> {

    private final int datasetSize;
    public Frequency(int size) {
        this.datasetSize = size;
    }

    @Override
    public Double computeConcreteValue(EquivalenceClass equivalenceClass) {
        return (double) equivalenceClass.getSequence().getIdList().size()/datasetSize;
    }

    @Override
    public Double computePotentialValue(EquivalenceClass equivalenceClass) {
        return this.computeConcreteValue(equivalenceClass);
    }
}
