package main.algorithm.spade;
import main.algorithm.spade.structure.IdList;
import main.algorithm.spade.structure.Sequence;
import main.pattern.Item;
import main.pattern.Itemset;

import java.util.ArrayList;

public class CandidateGenerator<T extends Comparable<? super T>> {
    boolean cMinGap, cMaxGap;
    int mingap, maxgap;

    public CandidateGenerator(boolean cMinGap, boolean cMaxGap, int mingap, int maxgap) {
        this.cMinGap = cMinGap;
        this.cMaxGap = cMaxGap;
        this.mingap = mingap;
        this.maxgap = maxgap;
    }

    public CandidateGenerator(){
        this(false, false, 0, 0);
    }


    public ArrayList<Sequence<T>> genCandidates(Sequence<T> s1, Sequence<T> s2){
        ArrayList<Sequence<T>> res = new ArrayList<>();
        boolean s1SeqAtom = isSequenceAtom(s1);
        boolean s2SeqAtom = isSequenceAtom(s2);

        // P -> X v P -> X = {P -> X -> X}
        if (s1.equals(s2) && s1SeqAtom && s2SeqAtom){
          res.add(temporalJoin(s1, s2));
        }

        // P -> X v P -> Y = {P -> X -> Y, P -> Y -> X, P -> X Y}
        else if (s1SeqAtom && s2SeqAtom){
            res.add(temporalJoin(s1, s2));
            if (!cMaxGap){
                res.add(temporalJoin(s2, s1));
                res.add(equalityJoin(s1, s2));
            }
        }

        // P -> X v PY = {PY -> X}
        else if (s1SeqAtom && !s2SeqAtom){
            res.add((cMaxGap) ? equalityJoin(s1, s2) : temporalJoin(s2, s1));
        }

        // PX v P -> Y = {PX -> Y}
        else if (!s1SeqAtom && s2SeqAtom){
            res.add(temporalJoin(s1, s2));
        }

        // PX v PY = {PXY}
        else if (!s1SeqAtom && !s2SeqAtom && !s1.equals(s2)){
            res.add(equalityJoin(s1, s2));
        }
        return res;
    }

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

    public Sequence<T> temporalJoin(Sequence<T> s1, Sequence<T> s2) {
        Sequence<T> clone = s1.cloneSequence();
        Itemset<T> newItemset = new Itemset<>();
        IdList newIdList;
        if (cMaxGap){
            clone.add(s2.getLastItemset().cloneItemset());
        } else {
            newItemset.add(s2.getLastItem().cloneItem());
            clone.add(newItemset);
        }

        if (cMaxGap && cMinGap){
            newIdList = s1.getIdList().temporalJoin(mingap, s2.getIdList(), maxgap);
        } else if (cMinGap){
            newIdList = s1.getIdList().temporalJoin(mingap, s2.getIdList());
        } else if (cMaxGap) {
            newIdList = s1.getIdList().temporalJoin(s2.getIdList(), maxgap);
        } else {
            newIdList = s1.getIdList().temporalJoin(s2.getIdList());
        }
        clone.setIdList(newIdList);
        return clone;
    }

    public void setcMinGap(boolean cMinGap) {
        this.cMinGap = cMinGap;
    }

    public void setcMaxGap(boolean cMaxGap) {
        this.cMaxGap = cMaxGap;
    }

    public void setMingap(int mingap) {
        this.mingap = mingap;
    }

    public void setMaxgap(int maxgap) {
        this.maxgap = maxgap;
    }

    public boolean iscMinGap() {
        return cMinGap;
    }

    public boolean iscMaxGap() {
        return cMaxGap;
    }

    public int getMingap() {
        return mingap;
    }

    public int getMaxgap() {
        return maxgap;
    }
}
