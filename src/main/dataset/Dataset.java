package main.dataset;

import main.pattern.IPattern;
import main.pattern.Item;
import main.pattern.Itemset;
import main.pattern.Sequence;

import java.util.ArrayList;
import java.util.Arrays;

public class Dataset<P extends IPattern> extends ArrayList<P> implements IDataset<P>{
}


