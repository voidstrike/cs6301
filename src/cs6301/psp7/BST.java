package cs6301.psp7;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

public class BST<T extends Comparable<? super T>> implements Iterable<T>{
    /** Nested Tree Node */
    static class Entry<T>{
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right){
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    protected Entry<T> root; // The root node of this BST tree
    protected ArrayDeque<Entry<T>> operateStack; // Stack used to record ancestor while travel
    protected int size; // The size of current BST

    /** Constructor */
    public BST(){
        root = null;
        operateStack = new ArrayDeque<>();
        size = 0;
    }

    /** Method to check whether selected element is inside this BST */
    public boolean contains(T tar){
        operateStack.clear();
        Entry<T> currentNode = find(tar);
        return currentNode != null && tar.compareTo(currentNode.element) == 0;
    }

    /** Method to return the selected element if it inside this BST */
    public T get(T tar){
        operateStack.clear();
        Entry<T> currentNode = find(tar);
        if (currentNode == null || tar.compareTo(currentNode.element) != 0)
            return null;
        else
            return currentNode.element;
    }

    /** Method to add element into this BST */
    public boolean add(T tar){
        if (root == null){
            root = new Entry<>(tar, null, null);
            size = 1;
            return true;
        }

        Entry<T> currentNode = find(tar);
        if (tar.compareTo(currentNode.element) == 0){
            currentNode.element = tar;
            return false;
        }
        else if (tar.compareTo(currentNode.element) < 0)
            currentNode.left = new Entry<>(tar, null, null);
        else
            currentNode.right = new Entry<>(tar, null, null);
        size++;
        return true;
    }

    /** Method to remove an element from this BST */
    public T remove(T tar){
        if (root == null)
            return null; // No element inside this BST, cannot remove

        Entry<T> currentNode = find(tar);
        if (tar.compareTo(currentNode.element) != 0)
            return null; // The target element doesn't existed in this BST, cannot remove

        T result = currentNode.element;
        if (currentNode.left == null || currentNode.right == null)
            bypass(currentNode); // at least one child is null, trivially bypass it
        else{
            operateStack.push(currentNode);
            Entry<T> minR = find(currentNode.right, currentNode.element);
            currentNode.element = minR.element;
            bypass(minR);
        }
        size--;
        return result;
    }

    /** Method to return the new iterator of this BST, in order */
    @Override
    public Iterator<T> iterator() {
        return new BSTIterator(root);
    }

    /** BSTIterator, stack version iterator*/
    public class BSTIterator implements Iterator<T>{
        ArrayDeque<Entry<T>> iterStack;
        Entry<T> tracker;

        BSTIterator(Entry<T> root){
            iterStack = new ArrayDeque<>();
            tracker = root;
            if (tracker != null){
                do{
                    iterStack.push(tracker);
                    tracker = tracker.left;
                }while(tracker != null);
            }
        }

        @Override
        public boolean hasNext() {
            return !iterStack.isEmpty();
        }

        @Override
        public T next() {
            Entry<T> cur = iterStack.pop();
            Entry<T> tmp = cur;
            if (cur.right != null){
                tmp = tmp.right;
                do{
                    iterStack.push(tmp);
                    tmp = tmp.left;
                }while(tmp!=null);
            }
            return cur.element;
        }
    }

    /** Method to return the minimum of this BST, null if the root is null*/
    public T min(){
        Entry<T> holder = min(root);
        return holder == null ? null : holder.element;
    }
    protected Entry<T> min(Entry<T> startNode){
        if (startNode == null)
            return null;
        else{
            Entry<T> tracker = startNode;
            while(tracker.left != null)
                tracker = tracker.left;
            return tracker;
        }
    }

    /** Method to return the maximum of this BST, null if the root is null */
    public T max() {
        Entry<T> holder = max(root);
        return holder == null ? null : holder.element;
    }
    protected Entry<T> max(Entry<T> startNode){
        if (startNode == null)
            return null;
        else{
            Entry<T> tracker = startNode;
            while(tracker.right != null)
                tracker = tracker.right;
            return tracker;
        }
    }

    // Auxiliary methods
    /** Method to find target element, wrapped version of its inner method */
    protected Entry<T> find(T target){
        operateStack.clear();
        return find(root, target);
    }

    /** Method to find target element, push all its ancestor vertices into helperStack
     *  return the Entry if target exist in this BST, otherwise return the last vertices it visited
     */
    protected Entry<T> find(Entry<T> cNode, T target) {
        if (cNode == null || cNode.element.compareTo(target) == 0)
            return cNode;
        while (true) {
            if (cNode.element.compareTo(target) > 0) {
                if (cNode.left == null)
                    break;
                else {
                    operateStack.push(cNode);
                    cNode = cNode.left;
                }
            }
            else if (cNode.element.compareTo(target) == 0)
                break;
            else { // cNode.element.compareTo(target) < 0
                if (cNode.right == null)
                    break;
                else {
                    operateStack.push(cNode);
                    cNode = cNode.right;
                }
            }
        }
        return cNode;
    }

    /** Method to perform the bypass operation for selected node */
    protected void bypass(Entry<T> tar){
        Entry<T> parent = !operateStack.isEmpty() ? operateStack.peek() : null;
        Entry<T> child = tar.left == null ? tar.right : tar.left;
        if (parent == null)
            root = child;
        else if (parent.left == tar)
            parent.left = child;
        else
            parent.right = child;

    }

    /** Method to print the tree out in order */
    public void printTree(){
        System.out.print("["+size+"]");
        printTree(root);
        System.out.println();
    }

    /** Inorder traversal of the tree */
    private void printTree(Entry<T> node){
        if (node != null){
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }

    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        Iterator<T> curIter = this.iterator();
        int i=0;
        while (curIter.hasNext()){
            arr[i] = curIter.next();
            i++;
        }
        return arr;
    }

    public static void main(String[] args) {
        BST<Integer> t = new BST<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                t.remove(-x);
                t.printTree();
            } else {
                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();
                return;
            }
        }
    }
}
