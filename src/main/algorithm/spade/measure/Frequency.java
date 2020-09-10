package main.algorithm.spade.measure;

import main.algorithm.spade.structure.EquivalenceClass;

import javax.naming.Name;

public class Frequency extends Measure<Double> {

    private String name = "frequency";
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
        return computeConcreteValue(equivalenceClass);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
