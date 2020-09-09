package main.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of sequence main.pattern.
 * A sequence is a data structure composed by itemset not empty and ordered.
 * An itemset is a not ordered list of items. The items are atomic element of a sequence.
 * By example a sequence can be used to represent items purchased by a customer.
 * Itemset are shopping cart and items are products, and each cart are separated by a the time.
 * 1 : a b -> c -> e a f => customer 1 bought a and b product together and after he bought c
 * and last time products e a and f in the same cart.
 */
public class Sequence<T extends Comparable<? super T>> extends ArrayList<Itemset<T>> implements IPattern, Comparable<Sequence<T>>  {

    /**
     * Sequence identifier
     */
    private Integer id = null;

    /**
     * General constructor for a sequence.
     * @param id The sequence identifier.
     */
    private Sequence(int id) {
        this.id = id;
    }

    /**
     * General constructor with no id.
     */
    public Sequence(){}

    public Sequence(List<Itemset<T>> itemsets){
    }


    /**
     * Get the sequence id.
     * @return the sequence id.
     */
    public int getId() {
        return id;
    }





    /**
     * Remove the itemset at the index given.
     * @param indexItemset the index of the itemset to remove.
     * @return the itemset removed.
     */
    public Itemset<T> removeItemset(int indexItemset){
        Itemset<T> itemset = this.get(indexItemset);
        this.remove(indexItemset);
        return itemset;
    }

    /**
     * Get the item at the position j in the itemset at the position i of the itemsets list.
     * @param indexItemset index of the itemset where the item is extract.
     * @param indexItem index of the item to extract.
     * @return
     */
    public Item<T> getItem(int indexItemset, int indexItem){
        return this.get(indexItemset).get(indexItem);
    }

    /**
     * Add a item to itemset at the position i.
     * @param indexItemset the index of the itemset who the item is append.
     * @param item the item added.
     */
    public void addItem(int indexItemset, Item<T> item){
        this.get(indexItemset).add(item);
    }

    /**
     * Remove the item j at the position i.
     * @param indexItemset index of the itemset who contains the item to remove.
     * @param indexItem index of the item to remove.
     * @return
     */
    public Item<T> removeItem(int indexItemset, int indexItem){
        return this.get(indexItemset).removeItem(indexItem);
    }

    /**
     * Clone the sequence.
     * @return a copy of this sequence.
     */
    public Sequence<T> cloneSequence(){
        Sequence<T> res = new Sequence<>();
        for (Itemset<T> itemset : this){
            res.add(itemset.cloneItemset());
        }
        return res;
    }



    /**
     * Return the the length of the sequence, e.g the number of items of the sequence.
     * @return the number of items of the sequence.
     */
    public int length(){
        int res = 0;
        for (Itemset<T> itemset: this) {
            res += itemset.size();
        }
        return res;
    }

    /**
     * Get the string representation of the itemset.
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            stringBuilder.append(this.get(i).toString());
            stringBuilder.append((i != this.size()-1) ? " " : "");
        }
        return stringBuilder.toString();
    }

    /**
     * Get prefix of k-length.
     * @param k The size of the prefix, e.q. number of items.
     * @return new Sequence which is the prefix of this sequence.
     */
    private Sequence<T> getPrefix(int k){
        Sequence<T> res = new Sequence<>();
        int cpt = 0;

        for (Itemset<T> itemset: this) {
            Itemset<T> newItemset = new Itemset<>();
            res.add(newItemset);
            for (Item<T> item:itemset){
                if (cpt == k) {
                    break;
                } else {
                    newItemset.add(item.cloneItem());
                    cpt++;
                }
            }
            if (cpt == k){
                break;
            }
        }

        return res;
    }

    /**
     * Get the greater prefix of this sequence.
     * @return new Sequence of k-1 length.
     */
    public Sequence<T> getPrefix(){
        return (this.size()>1) ?  this.getPrefix(this.length()-1) : this.getPrefix(1);
    }


    /**
     * Get the last itemset.
     * @return the last itemset.
     */
    public Itemset<T> getLastItemset(){
        return this.get(this.size()-1);
    }

    /**
     * Get the last item of the sequence.
     * @return the last item of the sequence.
     */
    public Item<T> getLastItem(){
        return this.getLastItemset().getLastItem();
    }

    @Override
    public int compareTo(Sequence<T> o) {
        return this.equals(o) ? 1 : -1;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || !Sequence.class.isAssignableFrom(o.getClass())) return false;
        Sequence sequence = (Sequence) o;
        if ((sequence.length()) != this.length() || sequence.size() != this.size()) {
            return false;
        } else {
            for (int i = 0; i<this.size(); i++){
                if (!this.get(i).equals(sequence.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Item<T> getFirstItem() {
        return this.get(0).get(0);
    }
}
