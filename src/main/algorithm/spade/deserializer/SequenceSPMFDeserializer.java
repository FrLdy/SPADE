package main.algorithm.spade.deserializer;

import main.dataset.Dataset;
import main.dataset.IDataset;
import main.dataset.deserializer.SequenceDeserializer;
import main.pattern.Item;
import main.pattern.Itemset;
import main.algorithm.spade.structure.Sequence;

import java.io.IOException;

public class SequenceSPMFDeserializer<T extends Comparable<? super T>> extends SequenceDeserializer<Sequence<T>, T> {

    private final String itemsetsSeparator;
    private final String itemsSeparator;
    private final String sequencesSeparator;

    public SequenceSPMFDeserializer(String path) {
        super(path);
        this.itemsetsSeparator = "-1";
        this.sequencesSeparator = "-2";
        this.itemsSeparator = " ";
    }

    public SequenceSPMFDeserializer() {
        this("");
    }



    @Override
    public IDataset<Sequence<T>> deserialize() {
        IDataset<Sequence<T>> sequenceDatabase = new Dataset<>();
        try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                sequenceDatabase.add(stringToPattern(currentLine));
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            this.close();
        }
        return sequenceDatabase;
    }

    @Override
    public Sequence<T> stringToPattern(String sequenceString){
        Sequence<T> sequence = new Sequence<>();
        for (String stringItemset : sequenceString.split(itemsetsSeparator)){
            stringItemset = stringItemset.trim();
            if (stringItemset.equals(this.sequencesSeparator)){
                break;
            } else {
                sequence.add(this.stringToItemset(stringItemset));
            }
        }
        return sequence;
    }

    @Override
    public Itemset<T> stringToItemset(String itemsetString){
        Itemset<T> itemset = new Itemset<>();
        for (String stringItem : itemsetString.split(this.itemsSeparator)){
            itemset.add(new Item<T>((T) stringItem));
        }
        return itemset;
    }
}
