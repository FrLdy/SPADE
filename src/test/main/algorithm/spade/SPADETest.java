package main.algorithm.spade;

import main.algorithm.spade.deserializer.SequenceSPMFDeserializer;
import main.algorithm.spade.spade.SPADE;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SPADETest {
    private SequenceSPMFDeserializer<String> deserializer = new SequenceSPMFDeserializer<>();
    private Dataset<Sequence<String>> res;

    public SPADETest() {
        Dataset<Sequence<String>> dataset = (Dataset<Sequence<String>>) new SequenceSPMFDeserializer<String>("data/spadeTest1.txt").deserialize();
        SPADE<String> spade = new SPADE<>(0.5, dataset, false);
        spade.run();
        res = spade.getResultDataset();
    }

    @Test
    void enumerateSeq() {
        Dataset<Sequence<String>> verif = new Dataset<>();
        String[] seqstr = new String[]{
                "A -1 -2", "B -1 -2", "D -1 -2", "F -1 -2",
                "A B -1 -2", "A F -1 -2", "B -1 A -1 -2", "B F -1 -2",
                "D -1 A -1 -2", "D -1 B -1 -2", "D -1 F -1 -2", "F -1 A -1 -2",
                "A B F -1 -2", "B F -1 A -1 -2", "D -1 B F -1 -2", "D -1 B -1 A  -1 -2", "D -1 F -1 A -1 -2",
                "D -1 B F -1 A -1 -2"
        };
        if (res.size() < seqstr.length){
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