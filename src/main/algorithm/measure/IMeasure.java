package main.algorithm.measure;

import main.algorithm.spade.EquivalenceClass;

public interface IMeasure<D extends Double> {
    D computeConcreteValue(EquivalenceClass equivalenceClass);
    D computePotentialValue(EquivalenceClass equivalenceClass);
}
