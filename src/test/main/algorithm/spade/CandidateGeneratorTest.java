package main.algorithm.spade;

import main.pattern.Item;
import main.pattern.Itemset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandidateGeneratorTest {
    private CandidateGenerator<String> candidateGenerator;
    private Sequence<String> p_a, p_f;

    public CandidateGeneratorTest() {
        this.candidateGenerator = new CandidateGenerator<>();

        this.p_a = new Sequence<>();
        p_a.add(new Itemset<>(new String[]{"p"}));
        p_a.add(new Itemset<>(new String[]{"a"}));
        p_a.setIdList(new IdList());
        p_a.getIdList().add(1, 20);
        p_a.getIdList().add(1, 30);
        p_a.getIdList().add(1, 40);
        p_a.getIdList().add(8, 10);
        p_a.getIdList().add(8, 30);
        p_a.getIdList().add(8, 50);
        p_a.getIdList().add(8, 80);
        p_a.getIdList().add(13, 50);
        p_a.getIdList().add(13, 70);
        p_a.getIdList().add(4, 60);
        p_a.getIdList().add(7, 40);
        p_a.getIdList().add(15, 60);
        p_a.getIdList().add(17, 20);
        p_a.getIdList().add(20, 10);

        this.p_f = new Sequence<>();
        p_f.add(new Itemset<>(new String[]{"p"}));
        p_f.add(new Itemset<>(new String[]{"f"}));
        p_f.setIdList(new IdList());
        p_f.getIdList().add(1, 70);
        p_f.getIdList().add(1, 80);
        p_f.getIdList().add(8,30);
        p_f.getIdList().add(8,40);
        p_f.getIdList().add(8,50);
        p_f.getIdList().add(8,80);
        p_f.getIdList().add(13,10);
        p_f.getIdList().add(3,10);
        p_f.getIdList().add(5,70);
        p_f.getIdList().add(11,30);
        p_f.getIdList().add(16,80);
        p_f.getIdList().add(20,20);
    }



    @Test
    void isEventAtom() {
        Sequence<String> sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b"}));
        // <a> <b> : false
        if (candidateGenerator.isEventAtom(sequence)){
            fail("Error event atom");
        }

        sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b", "c"}));
        // <a> <b, c> : true
        if (!candidateGenerator.isEventAtom(sequence)){
            fail("Error event atom");
        }

        sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b", "c"}));
        sequence.add(new Itemset<>(new String[]{"e"}));
        // <a> <b, c> <e> : false
        if (candidateGenerator.isEventAtom(sequence)){
            fail("Error event atom");
        }
    }

    @Test
    void getPenultimateItemPostion() {
        Sequence<String> sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        if (candidateGenerator.getPenultimateItemItemsetPostion(sequence) != 0){
            fail("Error get penultimate ");
        }
        sequence.addItem(0, new Item<>("c"));
        if (candidateGenerator.getPenultimateItemItemsetPostion(sequence) != 0){
            fail("Error get penultimate ");
        }

        sequence.add(new Itemset<>(new String[]{"b"}));
        if (candidateGenerator.getPenultimateItemItemsetPostion(sequence) != 0){
            fail("Error get penultimate ");
        }

        sequence.add(new Itemset<>(new String[]{"e", "g"}));
        if (candidateGenerator.getPenultimateItemItemsetPostion(sequence) != 2){
            fail("Error get penultimate ");
        }
    }

    @Test
    void isSequenceAtom() {
        Sequence<String> sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b"}));
        // <a> <b> : true
        if (!candidateGenerator.isSequenceAtom(sequence)){
            fail("Error event atom");
        }

        sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b", "c"}));
        // <a> <b, c> : false
        if (candidateGenerator.isSequenceAtom(sequence)){
            fail("Error event atom");
        }

        sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        sequence.add(new Itemset<>(new String[]{"b", "c"}));
        sequence.add(new Itemset<>(new String[]{"e"}));
        // <a> <b, c> <e> : true
        if (!candidateGenerator.isSequenceAtom(sequence)){
            fail("Error event atom");
        }
    }

    @Test
    void getLastItemItemsetPosition() {
        Sequence<String> sequence = new Sequence<>();
        sequence.add(new Itemset<>(new String[]{"a"}));
        // <a>
        if (candidateGenerator.getLastItemItemsetPosition(sequence) != 0){
            fail("Error get lastitem position ");
        }
        // <a,c>
        sequence.addItem(0, new Item<>("c"));
        if (candidateGenerator.getLastItemItemsetPosition(sequence) != 0){
            fail("Error get lastitem position");
        }

        // <a,c> <b>
        sequence.add(new Itemset<>(new String[]{"b"}));
        if (candidateGenerator.getLastItemItemsetPosition(sequence) != 1){
            fail("Error get penlastitemultimate position");
        }
        // <a,c> <b> <e,g>
        sequence.add(new Itemset<>(new String[]{"e", "g"}));
        if (candidateGenerator.getLastItemItemsetPosition(sequence) != 2){
            fail("Error get lastitem position");
        }
    }

    @Test
    void equalityJoin() {
        Sequence<String> s = candidateGenerator.equalityJoin(p_a, p_f);
        Sequence<String> verif = new Sequence<>();
        verif.setIdList(new IdList());
        verif.add(new Itemset<>(new String[]{"p"}));
        verif.add(new Itemset<>(new String[]{"a", "f"}));
        verif.getIdList().add(8, 30);
        verif.getIdList().add(8, 50);
        verif.getIdList().add(8, 80);

        if (!s.equals(verif)){
            fail("Error sequence");
        }
        if (!s.getIdList().equals(verif.getIdList())){
            fail("Error equality join");
        }
    }

    @Test
    void temporalJoin1() {
        Sequence<String> s = candidateGenerator.temporalJoin(p_a, p_f);
        Sequence<String> verif = new Sequence<>();
        verif.setIdList(new IdList());
        verif.add(new Itemset<>(new String[]{"p"}));
        verif.add(new Itemset<>(new String[]{"a"}));
        verif.add(new Itemset<>(new String[]{"f"}));
        verif.getIdList().add(1, 70);
        verif.getIdList().add(1, 80);
        verif.getIdList().add(8, 30);
        verif.getIdList().add(8, 40);
        verif.getIdList().add(8, 50);
        verif.getIdList().add(8, 80);
        verif.getIdList().add(20, 20);

        if (!s.equals(verif)){
            fail("Error sequence");
        }
        if (!s.getIdList().equals(verif.getIdList())){
            fail("Error equality join");
        }
    }

    @Test
    void temporalJoin2() {
        Sequence<String> s = candidateGenerator.temporalJoin(p_f, p_a);
        Sequence<String> verif = new Sequence<>();
        verif.setIdList(new IdList());
        verif.add(new Itemset<>(new String[]{"p"}));
        verif.add(new Itemset<>(new String[]{"f"}));
        verif.add(new Itemset<>(new String[]{"a"}));
        verif.getIdList().add(8, 50);
        verif.getIdList().add(8, 80);
        verif.getIdList().add(13, 50);
        verif.getIdList().add(13, 70);

        if (!s.equals(verif)){
            fail("Error sequence");
        }
        if (!s.getIdList().equals(verif.getIdList())){
            fail("Error equality join");
        }
    }
}