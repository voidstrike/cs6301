package cs6301.psp7;

/**
 * Created by Alan Lin on 10/21/2017.
 *
 */
import java.util.ArrayDeque;
import java.util.Iterator;
import cs6301.psp7.BST;

public class BSTMap<K extends Comparable<? super K>, V> extends BST<K> implements Iterable<K>{
    /** Extended BST node*/
    static class BMEntry<K, V> extends Entry<K>{
        V value;
        BMEntry(K key, V value, BMEntry<K, V> left, BMEntry<K, V> right){
            super(key, left, right);
            this.value = value;
        }
    }

    BSTMap(){
        root = null;
        operateStack = new ArrayDeque<>();
        size = 0;
    }

    /** Method to get a value based on key, return null if key doesn't exist
     *  Using signature getValue() rather than get due to clash between the get() method in BST
     * */
    public V getValue(K key){
        BMEntry<K, V> currentNode = (BMEntry<K, V>) find(key);
        if (currentNode == null || key.compareTo(currentNode.element) != 0)
            return null;
        else
            return currentNode.value;
    }

    /** Method to add a tuple into tree map */
    public boolean put(K key, V value){
        return add(key, value);
    }

    public Iterator<K> iterator(){
        return new BSTIterator(root);
    }

    // Auxiliary method
    public boolean add(K tar, V value) {
        if (root == null){
            root = new BMEntry<>(tar, value ,null, null);
            size = 1;
            return true;
        }

        Entry<K> currentNode = find(tar);
        if (tar.compareTo(currentNode.element) == 0){
            currentNode.element = tar;
            return false;
        }
        else if (tar.compareTo(currentNode.element) < 0)
            currentNode.left = new BMEntry<>(tar, value ,null, null);
        else
            currentNode.right = new BMEntry<>(tar, value ,null, null);
        size++;
        return true;
    }
}
