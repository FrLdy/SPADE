package main.dataset.deserializer;



import main.dataset.IDataset;
import main.pattern.IPattern;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Deserialize a data file to get a Database instance.
 */
public abstract class Deserializer<P extends IPattern> {

    /**
     * The path of the data file.
     */
    private final String path;

    protected BufferedReader bufferedReader;

    /**
     * General constructor.
     * @param path the path of the data file.
     */
    protected Deserializer(String path) {
        this.path = path;
        this.open();
    }

    /**
     * Opens the file into the BufferReader instance.
     */
    void open() {
        try {
            bufferedReader = new BufferedReader(new FileReader(this.path));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Closes the access at the data file.
     */
    public void close(){
        try {
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Convert the string representation of the pattern to an instance of Pattern.
     * @param sequenceString the string representation of the pattern.
     * @return the conversion of the pattern string representation to Pattern instance.
     */
    public abstract P stringToPattern(String sequenceString);

    /**
     * Method which traverses the data file to store all pattern in the database.
     * @return An instance of Database which contains the data of the file.
     */
    public abstract IDataset<P> deserialize();

}
