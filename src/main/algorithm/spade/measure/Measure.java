package main.algorithm.spade.measure;

import main.algorithm.spade.structure.EquivalenceClass;

public abstract class Measure<N extends Number> {
    private String name;

    public abstract N computeConcreteValue(EquivalenceClass equivalenceClass);

    public abstract N computePotentialValue(EquivalenceClass equivalenceClass);

    @Override
    public String toString() {
        return this.name;
    }
}
