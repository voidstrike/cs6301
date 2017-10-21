package cs6301.psp7;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
import java.util.Scanner;

public class RedBlackTree<T extends Comparable<? super T>> extends BST<T> {
    static class RBEntry<T> extends Entry<T>{
        boolean isRed;
        RBEntry(T x, RBEntry<T> left, RBEntry<T> right){
            super(x, left, right);
            isRed = true;
        }

        void setRed(){
            this.isRed = true;
        }

        void setBlack(){
            this.isRed = false;
        }
    }


    RedBlackTree(){
        super();
    }

    @Override
    public boolean add(T tar) {
        operateStack.clear();
        if (root == null){
            root = new RBEntry<>(tar, null, null);
            size = 1;
            ((RBEntry<T>) root).setBlack();
            return true;
        }

        // Normal BST add node operation
        RBEntry<T> currentNode = (RBEntry<T>) find(tar);
        boolean dir;
        if (tar.compareTo(currentNode.element) == 0){
            currentNode.element = tar;
            return false;
        }
        else if (tar.compareTo(currentNode.element) < 0) {
            currentNode.left = new RBEntry<>(tar, null, null); // add new node into the tree, as red node
            dir = true;
        }
        else {
            currentNode.right = new RBEntry<>(tar, null, null); // add new node into the tree, as red node
            dir = false;
        }

        // Corresponding RBTree repair operation
        operateStack.push(currentNode);
        if (dir)
            repair((RBEntry<T>) currentNode.left);
        else
            repair((RBEntry<T>) currentNode.right);

        // Set the color of root to black after repair operation
        ((RBEntry<T>) root).setBlack();
        size++;
        return true;
    }

    @Override
    public T remove(T tar) {
        operateStack.clear();
        if (root == null)
            return null;
        RBEntry<T> currentNode = (RBEntry<T>) find(tar);
        if (tar.compareTo(currentNode.element) != 0)
            return null;

        T result = currentNode.element;
        RBEntry<T> c;
        if (currentNode.left == null || currentNode.right == null) {
            c = currentNode.left == null ? (RBEntry<T>) currentNode.right : (RBEntry<T>) currentNode.left;
            bypass(currentNode);
            if (!currentNode.isRed){
                fix(c);
            }
        }
        else{
            operateStack.push(currentNode);
            RBEntry<T> minR = (RBEntry<T>) find(currentNode.right, currentNode.element);
            currentNode.element = minR.element;
            bypass(minR);
            if (!minR.isRed){
                fix(null);
            }
        }
        size--;
        ((RBEntry<T>) root).setBlack();
        return result;
    }

    /** Method to restore the balance of Red-black tree after add operation */
    private void repair(RBEntry<T> t){
        // initialize all the parameters
        RBEntry<T> p_t = operateStack.isEmpty() ? null : (RBEntry<T>) operateStack.pop();
        RBEntry<T> g_t = operateStack.isEmpty() ? null : (RBEntry<T>) operateStack.pop();
        RBEntry<T> u_t;
        if (g_t == null)
            u_t = null;
        else{
            u_t = g_t.left == p_t ? (RBEntry<T>) g_t.right : (RBEntry<T>) g_t.left;
        }

        Entry<T> holder;
        while(t.isRed){
            if(root == t || root == p_t || p_t == null || !p_t.isRed)
                // break condition
                // t is root, p_t is root or p_t is black
                return;

            if (u_t != null && u_t.isRed){
                // Trivially case
                p_t.setBlack();
                u_t.setBlack();
                g_t.setRed();

                // Update all the parameters
                t = g_t;
                p_t = operateStack.isEmpty() ? null : (RBEntry<T>) operateStack.pop();
                g_t = operateStack.isEmpty() ? null : (RBEntry<T>) operateStack.pop();
                if (g_t == null)
                    u_t = null;
                else{
                    u_t = g_t.left == p_t ? (RBEntry<T>) g_t.right : (RBEntry<T>) g_t.left;
                }
            }
            else {
                // In those cases, t, p_t, and g_t cannot be null
                boolean dir = false, isRoot = operateStack.isEmpty();
                if (!isRoot){
                    // Check whether g_t is the left or right child or its parent
                    dir = ((RBEntry<T>) operateStack.peek()).left == g_t;
                }

                if (g_t == null){
                    System.out.print("Error");
                }
                else{
                    // Handle each case
                    if (g_t.left == p_t && p_t.left == t){
                        // LL case
                        p_t.setBlack();
                        g_t.setRed();
                        holder = rightRotate(g_t);

                    }
                    else if (g_t.right == p_t && p_t.right == t){
                        // RR case
                        p_t.setBlack();
                        g_t.setRed();
                        holder = leftRotate(g_t);
                    }
                    else if (g_t.left == p_t && p_t.right == t){
                        // LR case
                        t.setBlack();
                        g_t.setRed();
                        g_t.left = leftRotate(p_t);
                        holder = rightRotate(g_t);
                    }
                    else{
                        // RL case
                        t.setBlack();
                        g_t.setRed();
                        g_t.right = rightRotate(p_t);
                        holder = leftRotate(g_t);
                    }

                    // Update the structure
                    if (isRoot)
                        root = holder; // if there is no more element in the operateStack, set root as holder
                    else {
                        if (dir)
                            operateStack.peek().left = holder;
                        else
                            operateStack.peek().right = holder;
                    }
                }
                return;
            }
        }
    }

    /** Method to restore the balance of Red-Black tree after remove operation */
    private void fix(RBEntry<T> t){
        // Initialize all the parameters
        RBEntry<T> p_t= (RBEntry<T>) operateStack.pop(), s_t;
        Entry<T> holder;
        boolean takeSign = false; // The flag parameter that indicates whether to retrieve an new parent node from operateStack

        while(root != t){
            // initialize all the parameters
            // if t is not root, it must have a parent node
            if (takeSign)
                p_t = (RBEntry<T>) operateStack.pop();

            s_t = p_t.left == t ? (RBEntry<T>) p_t.right : (RBEntry<T>) p_t.left;
            boolean dir = false, isRoot = operateStack.isEmpty();
            if (!isRoot){
                // Check whether p_t is the left or right child or its parent
                dir = ((RBEntry<T>) operateStack.peek()).left == p_t;
            }

            if (t != null && t.isRed){
                // case 1
                t.setBlack();
                return;
            }
            else if (!s_t.isRed && isBlack((RBEntry<T>) s_t.left) && isBlack((RBEntry<T>) s_t.right)){
                // case 2, s_t and its children are all black
                s_t.setRed();
                t = p_t;
                takeSign = true;
            }
            else if (isBlack(s_t)){
                // case 3 and 4, s_t is black, s_t has at least one red child
                RBEntry<T> rc = !isBlack((RBEntry<T>) s_t.left) ? (RBEntry<T>) s_t.left : (RBEntry<T>) s_t.right;
                if (p_t.right == s_t && s_t.right == rc){
                    // LL
                    s_t.isRed = p_t.isRed; // Swap the color of s_t and p_t, s_t must be black
                    p_t.setBlack();
                    rc.setBlack(); // set the color of rc to black
                    holder = leftRotate(p_t);

                    // Update the structure
                    reset(holder, isRoot, dir);
                    return;
                }
                else if (p_t.left == s_t && s_t.left == rc){
                    // RR
                    s_t.isRed = p_t.isRed;
                    p_t.setBlack();
                    rc.setBlack();
                    holder = rightRotate(p_t);

                    // Update the structure
                    reset(holder, isRoot, dir);
                    return;
                }
                else if (p_t.right == s_t && s_t.left == rc){
                    // RL
                    rc.setBlack(); // Swap the color of rc and s_t, but s_t must be black and rc must be red in this scenario
                    s_t.setRed();
                    p_t.right = rightRotate(s_t);
                    takeSign = false;
                }
                else{
                    // LR
                    rc.setBlack(); // Swap the color of rc and s_t, but s_t must be black and rc must be red in this scenario
                    s_t.setRed();
                    p_t.left = leftRotate(s_t);
                    takeSign = false;
                }
            }
            else{
                // s_t is Red, s_t cannot be null
                s_t.isRed = p_t.isRed;
                p_t.setRed();
                if (p_t.left == t)
                    p_t = (RBEntry<T>) rightRotate(s_t);
                else
                    p_t = (RBEntry<T>) leftRotate(s_t);
                takeSign = false;
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

    /** Method to return whether a given node is black or not */
    private boolean isBlack(RBEntry<T> node){
        return (node==null||!node.isRed);
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
        // Test code below
        BST<Integer> t = new RedBlackTree<>();
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
