package main.dataset.deserializer;


/**
 * Abstract class of deserializer of Data file where each pattern is linked with a label.
 * It fills a Database with concrete instances of wish pattern.
 * It extends Deserializer to get method to read the file.
 */
public interface DeserializerWithLabels<N extends Number> {

    /**
     * Convert the label written on the file to the T type.
     * @param labelString The label value written in the file.
     * @return The conversion of the labelString into T instance.
     */
    N stringToLabel(String labelString);
}
