package main.algorithm.spade;


import java.util.ArrayList;
import java.util.List;

public class EquivalenceClass{

    /**
     * Sequence which is the identifier of the equivalence class.
     * It's the greater prefix of equivalence class id of level-1 children.
     */
    public Sequence<?> sequence;

    private List<EquivalenceClass> members = new ArrayList<>();

    public EquivalenceClass(Sequence<?> sequence) {
        this.sequence = sequence;
    }

    public EquivalenceClass(){
        this(null);
    }

    public void setMembers(List<EquivalenceClass> members) {
        this.members = members;
    }

    public Sequence<?> getSequence(){
        return this.sequence;
    }

    /**
     * Getter of children of the
     * @return The list that contains equivalence classes of Sequence constructed from identifier.
     */
    public List<EquivalenceClass> getMembers() {
        return members;
    }

    /**
     * Add new equivalence class child at this equivalence class.
     * @param sequence The identifier of the new equivalence class.
     */
    public void addMember(Sequence<?> sequence){
        this.members.add(new EquivalenceClass(sequence));
    }

    public void addMember(EquivalenceClass equivalenceClass){
        this.members.add(equivalenceClass);
    }

    boolean contains(EquivalenceClass equivalenceClass){
        for (EquivalenceClass eqC : this.getMembers()){
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
