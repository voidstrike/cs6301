package cs6301.plp6;

/**
 * Created by Alan Lin on 11/13/2017.
 *
 */
public class MDS {
    public static class Pair{
        long id;
        int price;
        public Pair(long id, int price){
            this.id = id;
            this.price = price;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null)
                return false;
            Pair other = (Pair) obj;
            return id == other.id;
        }
    }

    public MDS(){}

    /** Add a new item. If an entry with the same id already exists
     * the new description is merged with the existing description of the item
     * Returns true if the item is new, and false otherwise */
}
