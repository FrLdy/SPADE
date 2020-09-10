package main.dataset.serializer;



import main.dataset.Dataset;
import main.dataset.IDataset;
import main.pattern.IPattern;

import java.io.*;

/**
 * Deserialize a data file to get a Database instance.
 */
public abstract class Serializer<P extends IPattern> {

    /**
     * The path of the data file.
     */
    private final String path;

    protected FileWriter fileWriter;

    /**
     * General constructor.
     * @param path the path of the data file.
     */
    protected Serializer(String path) {
        this.path = path;
        this.open();
    }

    /**
     * Opens the file into the BufferReader instance.
     */
    void open() {
        try {
            fileWriter = new FileWriter(this.path);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Closes the access at the data file.
     */
    public void close(){
        try {
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public abstract String PatternToString (P pattern);

    public void serialize(Dataset<P> dataset) throws IOException {
        for (P pattern : dataset){
            fileWriter.write(this.PatternToString(pattern)+"\n");
        }
        this.close();
    };
}
