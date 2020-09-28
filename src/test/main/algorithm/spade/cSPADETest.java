package main.algorithm.spade;

import main.algorithm.spade.deserializer.SequenceSPMFDeserializer;
import main.algorithm.spade.deserializer.SequencesWeirauchDeserializer;
import main.algorithm.spade.measure.Frequency;
import main.algorithm.spade.structure.Sequence;
import main.dataset.Dataset;
import main.dataset.serializer.SequenceSerializer;
import main.pattern.Item;
import main.pattern.Itemset;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.junit.jupiter.api.Test;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class cSPADETest {
    private SequenceSPMFDeserializer<String> deserializer = new SequenceSPMFDeserializer<>();
    private cSPADE<String> cSPADE;
    private SequenceSerializer<Sequence<String>, String> serializer;
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

    @Test
    void testWeihrauchGap1() throws IOException {
        cSPADE.setEntryDataset((Dataset<Sequence<String>>) new SequencesWeirauchDeserializer(
                "data/pTH0914_HK.raw")
                .deserialize()
        );
        double entrySize = cSPADE.getEntryDataset().size();
        cSPADE.setMeasure(new Frequency((int) entrySize));
        this.cSPADE.setMinSup(0.7);
        this.cSPADE.setcMaxGap(true);
        this.cSPADE.setMaxgap(2);
        cSPADE.run();

        serializer = getSerializer("data/out/pTH0914_HK.freq."+cSPADE.getMinSup()+".gap."+cSPADE.getMaxgap()+"res");

        File file = new File("data/pTH0914_HK.fil");
        for (Sequence<String> sequence : cSPADE.getResultDataset()){
            List<Line> lines = Unix4j.grep(this.seqstr(sequence, cSPADE.getMaxgap()-1), file).toLineList();
            double nb = lines.size();
            double prop = nb/entrySize;
            if (sequence.getSupport() != prop){
                fail(sequence.toString()+ " " + sequence.getSupport() + " " + prop);
            }
        }

        serializer.serialize(cSPADE.getResultDataset());
    }

    String seqstr(Sequence<String> seq, int gap){
        StringBuilder res = new StringBuilder();
        for (Itemset<String> items : seq){
            for (Item<String> item : items){
                res.append(item.toString())
                        .append(".{0,"+gap+"}");
                if (items.size() > 1){
                    res.append('-');
                }
            }
        }
        return res.toString();
    }

    SequenceSerializer<Sequence<String>, String> getSerializer(String path){
        return new SequenceSerializer<Sequence<String>, String>(path) {

            @Override
            public String itemsetToString(Itemset<String> itemset) {
                StringBuilder sb = new StringBuilder();
                for (Item<String> item: itemset){
                    sb.append(item);
                }
                return sb.toString();
            }

            @Override
            public java.lang.String PatternToString(Sequence<java.lang.String> sequence) {
                StringBuilder sb = new StringBuilder();
                for (Itemset<String> items: sequence){
                    sb.append(itemsetToString(items));
                }
                sb.append(";").append(sequence.getSupport().toString());
                return sb.toString();
            }

            @Override
            public void serialize(Dataset<Sequence<String>> dataset) throws IOException {
                fileWriter.write("frequent sequence;support(%)\n");
                super.serialize(dataset);
            }
        };
    }
}