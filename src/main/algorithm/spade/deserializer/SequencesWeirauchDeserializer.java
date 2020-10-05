package main.algorithm.spade.deserializer;

import main.algorithm.spade.structure.Sequence;
import main.dataset.DatasetWithLabels;
import main.dataset.IDataset;
import main.dataset.deserializer.DeserializerWithLabels;
import main.dataset.deserializer.SequenceDeserializer;
import main.pattern.Item;
import main.pattern.Itemset;

import java.io.IOException;
import java.io.LineNumberReader;

public class SequencesWeirauchDeserializer extends SequenceDeserializer<Sequence<String>, String> implements DeserializerWithLabels<Double> {

    public SequencesWeirauchDeserializer(String path) {
        super(path);
    }

    @Override
    public Sequence<String> stringToPattern(String sequenceString, Integer id) {
        Sequence<String> sequence = new Sequence<>(id);
        for (String item : sequenceString.split("")) {
            sequence.add(this.stringToItemset(item));
        }
        return sequence;
    }

    @Override
    public Sequence<String> stringToPattern(String sequenceString) {
        return stringToPattern(sequenceString, null);
    }

    public Itemset<String> stringToItemset(String itemsetString) {
        Itemset<String> itemset = new Itemset<>();
        itemset.add(new Item<>(itemsetString));
        return itemset;
    }

    @Override
    public Double stringToLabel(String labelString) {
        return Double.parseDouble(labelString);
    }

    @Override
    public IDataset<Sequence<String>> deserialize() {
        DatasetWithLabels<Sequence<String>, Double> sequenceDatabase = new DatasetWithLabels<>();
        try {
            String currentLine;
            LineNumberReader lineNumberReader = new LineNumberReader(bufferedReader);
            int cptid = 0;
            while ((currentLine = lineNumberReader.readLine()) != null) {
                if (lineNumberReader.getLineNumber() != 1) {
                    cptid ++;
                    String[] line = currentLine.split("\\s+");
                    if (line.length == 10) {
                        int sequencePosition = 5;
                        Sequence<String> sequence = this.stringToPattern(line[sequencePosition], cptid);
                        int labelPosition = 7;
                        Double label = this.stringToLabel(line[labelPosition]);
                        sequenceDatabase.add(sequence, sequence.getId(), label);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        return sequenceDatabase;
    }
}
