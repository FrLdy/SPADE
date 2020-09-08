package main.algorithm.spade.measure;

import main.algorithm.spade.structure.EquivalenceClass;

public interface IMeasure<N extends Number> {
    N computeConcreteValue(EquivalenceClass equivalenceClass);
    N computePotentialValue(EquivalenceClass equivalenceClass);
}
