package main.algorithm.spade;

import main.algorithm.spade.deserializer.SequenceSPMFDeserializer;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class cSPADETest {
    private SequenceSPMFDeserializer<String> deserializer = new SequenceSPMFDeserializer<>();
    private cSPADE<String> cSPADE;

    public cSPADETest() {
        Dataset<Sequence<String>> dataset = (Dataset<Sequence<String>>) new SequenceSPMFDeserializer<String>("data/spadeTest1.txt").deserialize();
        cSPADE = new cSPADE<>(0.5, dataset, false);
    }

    @Test
    void enumerateSeq() {
        cSPADE.run();
        Dataset<Sequence<String>> res = cSPADE.getResultDataset();

        Dataset<Sequence<String>> verif = new Dataset<>();
        String[] seqstr = new String[]{
                "A -1 -2", "B -1 -2", "D -1 -2", "F -1 -2",
                "A B -1 -2", "A F -1 -2", "B -1 A -1 -2", "B F -1 -2",
                "D -1 A -1 -2", "D -1 B -1 -2", "D -1 F -1 -2", "F -1 A -1 -2",
                "A B F -1 -2", "B F -1 A -1 -2", "D -1 B F -1 -2", "D -1 B -1 A  -1 -2", "D -1 F -1 A -1 -2",
                "D -1 B F -1 A -1 -2"
        };
        if (res.size() < seqstr.length){
            System.out.println(seqstr.length);
            System.out.println(res.size());
            fail("Error");
        }

        if (res.size() > seqstr.length){
            fail("Error");
        }

        for (int i = 0; i < seqstr.length; i++){
            Sequence<String> cand = deserializer.stringToPattern(seqstr[i]);
            if (!res.contains(cand)){
                fail("Error " + cand+ " not in res");
            }
        }

        System.out.println(res);
    }

    @Test
    void testGapMax1(){
        Dataset<Sequence<String>> verif = new Dataset<>();
        String[] seqstr = new String[]{
                "A -1 -2", "B -1 -2", "D -1 -2", "F -1 -2",
                "A B -1 -2", "A F -1 -2", "B F -1 -2",
                "B -1 A -1 -2", "D -1 B -1 -2", "F -1 A -1 -2",
                "A B F -1 -2", "B F -1 A -1 -2", "D -1 B -1 A -1 -2"
        };

        this.cSPADE.setcMaxGap(true);
        this.cSPADE.setMaxgap(1);
        cSPADE.run();
        Dataset<Sequence<String>> res = cSPADE.getResultDataset();

        if (res.size() < seqstr.length){
            System.out.println(seqstr.length);
            System.out.println(res.size());
            fail("Error");
        }

        if (res.size() > seqstr.length){
            fail("Error");
        }

        for (int i = 0; i < seqstr.length; i++){
            Sequence<String> cand = deserializer.stringToPattern(seqstr[i]);
            if (!res.contains(cand)){
                fail("Error " + cand+ " not in res");
            }
        }
    }
}