package main.algorithm.spade;

import main.algorithm.spade.deserializer.SequenceSPMFDeserializer;
import main.algorithm.spade.structure.IdList;
import main.algorithm.spade.structure.Sequence;
import main.pattern.Item;
import main.pattern.Itemset;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CandidateGeneratorTest {
    private CandidateGenerator<String> candidateGenerator;
    private Sequence<String> p_a, p_f;
    private SequenceSPMFDeserializer<String> deserializer = new SequenceSPMFDeserializer<>();

    public CandidateGeneratorTest() {
        this.candidateGenerator = new CandidateGenerator<>();

        this.p_a = deserializer.stringToPattern("p -1 a -1 -2");
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

        this.p_f = deserializer.stringToPattern("p -1 f -1 -2");
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
        Sequence<String> sequence = deserializer.stringToPattern("a -1 b -1 -2");

        // <a> <b> : false
        if (candidateGenerator.isEventAtom(sequence)){
            fail("Error event atom");
        }

        sequence = deserializer.stringToPattern("a -1 b c -1 -2");
        // <a> <b, c> : true
        if (!candidateGenerator.isEventAtom(sequence)){
            fail("Error event atom");
        }

        sequence = deserializer.stringToPattern("a -1 b c -1 e -1 -2");
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

    @Test
    void genCandidates() {
        Sequence<String> s1, s2, v1, v2, v3;
        ArrayList<Sequence<String>> res;

        s1 = deserializer.stringToPattern("p -1 a -1 f -1 -2");
        s2 = deserializer.stringToPattern("p -1 a -1 d -1 -2");

        v1 = deserializer.stringToPattern("p -1 a -1 f d -1 -2");
        v2 = deserializer.stringToPattern("p -1 a -1 f -1 d -1 -2");
        v3 = deserializer.stringToPattern("p -1 a -1 d -1 f -1 -2");

        res =  candidateGenerator.genCandidates(s1, s2);
        if (!res.containsAll(Arrays.asList(v1, v2, v3)) || res.size()!=3){
            fail("Error");
        }

        s1 = deserializer.stringToPattern("p -1 a f");
        s2 = deserializer.stringToPattern("p -1 a -1 d -1 -2");
        v1 = deserializer.stringToPattern("p -1 a f -1 d -1 -2");
        res =  candidateGenerator.genCandidates(s1, s2);
        if (!res.contains(v1) || res.size()!=1){
            fail("Error");
        }

        s2 = deserializer.stringToPattern("p -1 a f");
        s1 = deserializer.stringToPattern("p -1 a -1 d -1 -2");
        v1 = deserializer.stringToPattern("p -1 a f -1 d -1 -2");
        res =  candidateGenerator.genCandidates(s1, s2);
        if (!res.contains(v1) || res.size()!=1){
            fail("Error");
        }

        s1 = deserializer.stringToPattern("p -1 a f");
        s2 = deserializer.stringToPattern("p -1 a d");
        v1 = deserializer.stringToPattern("p -1 a d f -1 -2");
        res =  candidateGenerator.genCandidates(s1, s2);
        if (!res.contains(v1) || res.size()!=1){
            fail("Error");
        }

        s1 = deserializer.stringToPattern("p -1 a d -1 -2");
        v1 = deserializer.stringToPattern("p -1 a d -1 -2");
        res =  candidateGenerator.genCandidates(s1, s1);
        if (res.size()!=0){
            fail("Error");
        }
    }

    @Test
    void joinWith2Seq(){
        candidateGenerator.cMaxGap = true;
        Sequence<String> s1, s2, s3, s4, v1, v2, v3, v4;

        s1 = deserializer.stringToPattern("A -1 C -1 -2");
        s2 = deserializer.stringToPattern("A C -1 -2");

        s3 = deserializer.stringToPattern("C E -1 -2");
        s4 = deserializer.stringToPattern("C -1 E -1 -2");

        v1 = deserializer.stringToPattern("A -1 C E -1 -2");
        v2 = deserializer.stringToPattern("A -1 C -1 E -1 -2");
        v3 = deserializer.stringToPattern("A C E -1 -2");
        v4 = deserializer.stringToPattern("A C -1 E -1 -2");

        joinWith2Seq(s1, s3, v1);
        joinWith2Seq(s1, s4, v2);
        joinWith2Seq(s2, s3, v3);
        joinWith2Seq(s2, s4, v4);
    }

    void joinWith2Seq(Sequence<String> s1, Sequence<String> s2, Sequence<String> res){
        if (!candidateGenerator.genCandidates(s1, s2).get(0).equals(res)){
            fail();
        }
    }
}