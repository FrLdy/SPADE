package main.database.deserializer.sequence;

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
    public Sequence<String> stringToPattern(String sequenceString) {
        Sequence<String> sequence = new Sequence<>();
        for (String item : sequenceString.split("")) {
            sequence.add(this.stringToItemset(item));
        }
        return sequence;
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
            while ((currentLine = lineNumberReader.readLine()) != null) {
                if (lineNumberReader.getLineNumber() != 1) {
                    String[] line = currentLine.split("\\s+");
                    if (line.length == 10) {
                        int sequencePosition = 5;
                        Sequence<String> sequence = this.stringToPattern(line[sequencePosition]);
                        int labelPosition = 7;
                        Double label = this.stringToLabel(line[labelPosition]);
                        sequenceDatabase.add(sequence, label);
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
