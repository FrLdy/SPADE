package main.algorithm.measure;

import main.algorithm.spade.EquivalenceClass;

public interface IMeasure<N extends Number> {
    N computeConcreteValue(EquivalenceClass equivalenceClass);
    N computePotentialValue(EquivalenceClass equivalenceClass);
}
