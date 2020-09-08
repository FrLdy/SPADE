package main.algorithm.spade.structure;

import main.pattern.Item;
import main.pattern.Itemset;

public class Sequence<T extends Comparable<? super T>> extends main.pattern.Sequence<T> {
    private IdList idList;
    private Double support;

    public Sequence() {
        this.setIdList(new IdList());
    }

    public Sequence(String seq){

    }

    public Sequence(Item<T> item){
        this.setIdList(new IdList());
        Itemset<T> itemset = new Itemset<>();
        itemset.add(item);
        this.add(itemset);
    }

    public IdList getIdList() {
        return idList;
    }

    public Double getSupport() {
        return support;
    }

    public void setIdList(IdList idList) {
        this.idList = idList;
    }

    public void setSupport(Double support) {
        this.support = support;
    }

    @Override
    public Sequence<T> cloneSequence() {
        Sequence<T> clone = new Sequence<>();
        for (Itemset<T> iset : this){
            clone.add(iset.cloneItemset());
        }
        clone.idList = (IdList) idList.clone();
        clone.support = support;
        return clone;
    }

    @Override
    public String toString() {
        return super.toString() + " : " + getSupport();
    }
}
