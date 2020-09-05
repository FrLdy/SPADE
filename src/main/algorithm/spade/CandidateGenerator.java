package main.algorithm.spade;
import main.pattern.Item;
import main.pattern.Itemset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandidateGenerator<T extends Comparable<? super T>> {

    /**
     * Test if sequence is an sequence Atom *after factorisation* by prefix of k-1 length.
     * A -> B -> C ==factorisation P= A -> B==> [P] -> C : true
     * A -> B -> C,E ==factorisation P=A -> B -> C ==> [P]E : false
     * @param sequence
     * @return
     */
    public boolean isSequenceAtom(Sequence<T> sequence){
        int endPrefixPosition = this.getPenultimateItemItemsetPostion(sequence);
        int lastItemPosition = this.getLastItemItemsetPosition(sequence);

        return lastItemPosition > endPrefixPosition;
    }

    /**
     * Test if sequence is an event Atom *after factorisation* by prefix of k-1 length.
     * A -> B -> C ==factorisation==> [P= A -> B] -> C : false
     * A -> B -> C,E ==factorisation==> [P A -> B -> C]E : true
     * @param sequence
     * @return
     */
    public boolean isEventAtom(Sequence<T> sequence){
        return !this.isSequenceAtom(sequence);
    }

    /**
     * Get the position of the itemset which contains the penultimate item of the sequence.
     * @param sequence<T>
     * @return the index of an itemset
     */
    public int getPenultimateItemItemsetPostion(Sequence<T> sequence){
        Itemset<T> lastItemset = sequence.getLastItemset();
        if (sequence.size() == 1){
            return 0;
        } else if (lastItemset.size()>1){
            return sequence.size()-1;
        } else {
            return sequence.size()-2;
        }
    }

    /**
     * Get the position of the itemset which contains the last item.
     * @param sequence<T>
     * @return the index of an itemset
     */
    public int getLastItemItemsetPosition(Sequence<T> sequence){
        return sequence.size()-1;
    }

    public Sequence<T> equalityJoin(Sequence<T> s1, Sequence<T> s2){
        Sequence<T> clone = s1.cloneSequence();
        clone.getLastItemset().add(s2.getLastItem().cloneItem());
        clone.setIdList(s1.getIdList().equalityJoin(s2.getIdList()));
        return clone;
    }

    public Sequence<T> temporalJoin(Sequence<T> s1, Sequence<T> s2){
        Sequence<T> clone = s1.cloneSequence();
        Itemset<T> newItemset = new Itemset<>();
        newItemset.add(s2.getLastItem().cloneItem());
        clone.add(newItemset);
        clone.setIdList(s1.getIdList().temporalJoin(s2.getIdList()));
        return clone;
    }
}
