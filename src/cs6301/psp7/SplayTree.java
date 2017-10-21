package cs6301.psp7;

import java.util.Scanner;

/**
 * Created by Alan Lin on 10/21/2017.
 */
public class SplayTree<T extends Comparable<? super T>> extends BST<T> {
    SplayTree(){
        super();
    }

    @Override
    public boolean contains(T tar) {
        operateStack.clear();
        Entry<T> currentNode = find(tar);
        splay(currentNode);
        return currentNode != null && tar.compareTo(currentNode.element) == 0;
    }

    @Override
    public T get(T tar) {
        operateStack.clear();
        Entry<T> currentNode = find(tar);
        splay(currentNode);
        if (currentNode == null || tar.compareTo(currentNode.element) != 0)
            return null;
        else
            return currentNode.element;
    }

    @Override
    public T max() {
        operateStack.clear();
        Entry<T> holder = max(root);
        splay(holder);
        return holder == null ? null : holder.element;
    }

    @Override
    public T min() {
        operateStack.clear();
        Entry<T> holder = min(root);
        splay(holder);
        return holder == null ? null : holder.element;
    }

    @Override
    public boolean add(T tar) {
        operateStack.clear();
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
        else if (tar.compareTo(currentNode.element) < 0) {
            currentNode.left = new Entry<>(tar, null, null);
            operateStack.push(currentNode);
            currentNode = currentNode.left;
        }
        else {
            currentNode.right = new Entry<>(tar, null, null);
            operateStack.push(currentNode);
            currentNode = currentNode.right;
        }
        size++;
        splay(currentNode);
        return true;
    }

    @Override
    public T remove(T tar) {
        operateStack.clear();
        if (root == null)
            return null;
        Entry<T> currentNode = find(tar);
        if (tar.compareTo(currentNode.element) != 0){
            splay(currentNode);
            return null;
        }
        T result = currentNode.element;
        if (currentNode.left == null || currentNode.right == null)
            bypass(currentNode);
        else{
            operateStack.push(currentNode);
            Entry<T> minR = find(currentNode.right, currentNode.element);
            currentNode.element = minR.element;
            bypass(minR);
        }
        size--;
        return result;
    }

    private void splay(Entry<T> t){
        while (root != t){
            // t must have at least one ancestor
            if (root.left == t){
                root = rightRotate(root);
            }
            else if (root.right == t){
                root = leftRotate(root);
            }
            else{
                // In those cases, t must have at least two ancestors
                Entry<T> p_t = operateStack.pop();
                Entry<T> g_t = operateStack.pop();
                Entry<T> holder;
                boolean isRoot = operateStack.isEmpty();
                boolean dir = false;
                if (!isRoot)
                    dir = operateStack.peek().left == g_t;

                if (g_t.left == p_t && p_t.left == t){
                    // LL case -- double right rotations
                    g_t.left = rightRotate(p_t);
                    holder = rightRotate(g_t);
                }
                else if (g_t.right == p_t && p_t.right == t){
                    // RR case -double rotation LL
                    g_t.right = leftRotate(p_t);
                    holder = leftRotate(g_t);
                }
                else if (g_t.left == p_t && p_t.right == t){
                    // LR case -- RL rotation
                    g_t.left = leftRotate(p_t);
                    holder = rightRotate(g_t);
                }
                else{
                    // RL case -- LR rotation
                    g_t.right = rightRotate(p_t);
                    holder = leftRotate(g_t);
                }
                reset(holder, isRoot, dir);
                t= holder;
            }
        }
    }

    /** Method to perform left rotation, deal with RR case */
    private Entry<T> leftRotate(Entry<T> input){
        Entry<T> result = input.right;
        Entry<T> tmp = result.left;

        result.left = input;
        input.right = tmp;
        return result;
    }

    /** Method to perform right rotation, deal with LL case */
    private Entry<T> rightRotate(Entry<T> input){
        Entry<T> result = input.left;
        Entry<T> tmp = result.right;

        result.right = input;
        input.left = tmp;

        return result;
    }

    /** Helper function to reset the structure of the tree after rotation */
    private void reset(Entry<T> tar, boolean isRoot, boolean dir){
        // Update the structure
        if (isRoot)
            root = tar; // if there is no more element in the operateStack, set root as holder
        else {
            if (dir)
                operateStack.peek().left = tar;
            else
                operateStack.peek().right = tar;
        }
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
