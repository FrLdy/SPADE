package main.algorithm.spade.structure;


import java.util.ArrayList;
import java.util.List;

public class EquivalenceClass<T extends Comparable<? super T>> extends ArrayList<EquivalenceClass<T>>{

    /**
     * Sequence which is the identifier of the equivalence class.
     * It's the greater prefix of equivalence class id of level-1 children.
     */
    public Sequence<T> sequence;

    public EquivalenceClass(Sequence<T> sequence) {
        this.sequence = sequence;
    }

    public EquivalenceClass(){
        this(null);
    }

    public Sequence<T> getSequence(){
        return this.sequence;
    }

    /**
     * Getter of children of the
     * @return The list that contains equivalence classes of Sequence constructed from identifier.
     */
    public List<EquivalenceClass<T>> getMembers() {
        return this;
    }

    /**
     * Add new equivalence class child at this equivalence class.
     * @param sequence The identifier of the new equivalence class.
     */
    public void addMember(Sequence<T> sequence){
        this.add(new EquivalenceClass<>(sequence));
    }

    public void addMember(EquivalenceClass<T> equivalenceClass){
        this.add(equivalenceClass);
    }

    boolean contains(EquivalenceClass<T> equivalenceClass){
        for (EquivalenceClass<T> eqC : this){
            if (eqC.getSequence().equals(equivalenceClass.getSequence())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + getSequence() + "]";
    }
}
