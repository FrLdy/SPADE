package main.algorithm.spade.cspade;

import main.algorithm.spade.structure.Sequence;

public class CandidateGenerator<T extends Comparable<? super T>> extends main.algorithm.spade.spade.CandidateGenerator<T> {
    @Override
    public Sequence<T> temporalJoin(Sequence<T> s1, Sequence<T> s2) {
        return super.temporalJoin(s1, s2);
    }
}
