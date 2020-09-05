package main.pattern;

/**
 * Generic implementation of an item.
 * Any kind of item (int, String, ...) can be managed.
 * @param <T>
 */
public class Item<T extends Comparable<? super T>> implements Comparable<Item<T>>{

    /**
     * Content of the item.
     * Generic type to manage any kind of item type.
     */
    private T content;

    /**
     * General constructor
     * @param content Content of the item.
     */
    public Item(T content){
        this.content = content;
    }

    /**
     * Constructor. Built an new item from an item.
     * @param item
     */
    public Item(Item<T> item){
        this(item.getContent());
    }

    /**
     * Get the content of the item.
     * @return the content
     */
    T getContent() {
        return content;
    }

    /**
     * Set the content of the item
     * @param content the new content
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Get the string representation of the item.
     * @return the string representation
     */
    @Override
    public String toString() {
        return "" + this.content.toString();
    }

    /**
     * Clone this item.
     * @return new instance of Item.
     */
    public Item<T> cloneItem(){
        return new Item<>(this.content);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || getClass() != o.getClass()) return false;
        Item i = (Item) o;
        return content.equals(i.getContent());
    }

    @Override
    public int compareTo(Item<T> o) {
        return this.content.compareTo(o.getContent());
    }

    public static void main(String[] args){
        Item<Integer> item = new Item<>(9);
        Item<Integer> item2 = item.cloneItem();
        System.out.println(System.identityHashCode(item));
        System.out.println(System.identityHashCode(item2));
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }
}

