package main.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Implementation of an itemset.
 * The itemset is a list of items.
 */
public class Itemset<T extends Comparable<? super T>> extends ArrayList<Item<T>> implements IPattern {
    /**
     * General constructor.
     */
    public Itemset(){}

    public Itemset(T[] items){
        for (int i = 0; i<items.length; i++){
            this.add(new Item<>(items[i]));
        }
    }

    public Itemset(Item<T>[] items){
        this.addAll(Arrays.asList(items));
    }
    /**
     * Add an item at the actual set of items.
     * @param item is the item to addPattern.
     */
    public boolean add(Item<T> item){
        super.add(item);
        this.sort();
        return true;
    }

    /**
     * Remove an item from the list of items
     * @param i index of the item to remove.
     * @return the item removed.
     */
    public Item<T> removeItem(int i){
        return this.remove(i);
    }

    /**
     * Clone the itemset. Create new itemset - new instance - and copy the items in.
     * @return copy of the itemset.
     */
    public Itemset<T> cloneItemset() {
        Itemset<T> itemset = new Itemset<>();
        for (Item item : this){
            itemset.add(item.cloneItem());
        }
        return itemset;
    }

    /**
     * Get the string representation of the itemset.
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<");
        for (int i = 0 ; i  < this.size(); i++){
            Item<T> item = this.get(i);
            stringBuilder.append(item.toString());
            stringBuilder.append((i != this.size()-1) ? ", " : "");
        }
        stringBuilder.append(">");
        return stringBuilder.toString();
    }



    /**
     * Get the last item.
     * @return the last item.
     */
    public Item<T> getLastItem(){
        return this.get(this.size()-1);
    }

    public boolean contains(Item<T> o){
        for (Item<T> item : this){
            if (item.equals(o)){
                return true;
            }
        }
        return false;
    }

    void sort(){
        Collections.sort(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj == null || !Itemset.class.isAssignableFrom(obj.getClass())) return false;
        Itemset itemset = (Itemset) obj;
        if (this.size() != itemset.size()) {
            return false;
        }
        else {
            for (int i = 0; i < this.size(); i ++){
                if (!itemset.get(i).equals(this.get(i))) return false;
            }
        }
        return true;
    }
}
