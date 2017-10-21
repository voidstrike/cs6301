package cs6301.psp7;

import java.util.ArrayDeque;
import java.util.Scanner;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

public class AVLTree<T extends Comparable<? super T>> extends BST<T>{
    static class AVLEntry<T> extends BST.Entry<T>{
        int lHeight, rHeight;
        AVLEntry(T x, AVLEntry<T> left, AVLEntry<T> right){
            super(x, left, right);
            lHeight = 1;
            rHeight = 1;
        }

        int getNodeHeight(){
            return lHeight > rHeight ? lHeight : rHeight;
        }
    }

    private ArrayDeque<Boolean> pathStack; // Stack used to record the direction of corresponding element in operateStack

    /** Constructor */
    public AVLTree(){
        super();
        pathStack = new ArrayDeque<>();
    }

    /** Override method for add node into AVLTree */
    @Override
    public boolean add(T tar) {
        if (root == null){
            root = new AVLEntry<>(tar, null, null);
            size = 1;
            return true;
        }
        AVLEntry<T> currentNode = (AVLEntry<T>) find(tar);

        if (tar.compareTo(currentNode.element) == 0){
            currentNode.element = tar;
            return false;
        }
        else if (tar.compareTo(currentNode.element) < 0) {
            currentNode.left = new AVLEntry<>(tar, null, null);
            operateStack.push(currentNode);
            pathStack.push(true);
            currentNode = (AVLEntry<T>) currentNode.left;
        }
        else {
            currentNode.right = new AVLEntry<>(tar, null, null);
            operateStack.push(currentNode);
            pathStack.push(false);
            currentNode = (AVLEntry<T>) currentNode.right;
        }
        restoreAVLad(currentNode);
        size++;
        return false;
    }

    /** Override method for remove node from AVLTree */
    @Override
    public T remove(T tar) {
        // Element doesn't exist
        if (root == null)
            return null;
        AVLEntry<T> currentNode = (AVLEntry<T>) find(tar);
        if (tar.compareTo(currentNode.element) != 0)
            return null;
        // We found the target element, the elements inside the operateStack and pathStack is the information of its ancestors
        boolean dir;
        if (currentNode.left == null && currentNode.right == null){
            bypass(currentNode);
            currentNode = (AVLEntry<T>) operateStack.pop();
            dir = pathStack.pop();
            if (dir)
                currentNode.lHeight = 1;
            else
                currentNode.rHeight = 1;
        }
        else if (currentNode.left == null){
            // currentNode.right != null
            dir = pathStack.peek();
            bypass(currentNode);
            if (dir)
                currentNode = (AVLEntry<T>) operateStack.peek().left;
            else
                currentNode = (AVLEntry<T>) operateStack.peek().right;

        }
        else if (currentNode.right == null){
            dir = pathStack.peek();
            bypass(currentNode);
            if (dir)
                currentNode = (AVLEntry<T>) operateStack.peek().left;
            else
                currentNode = (AVLEntry<T>) operateStack.peek().right;
        }
        else{
            operateStack.push(currentNode);
            pathStack.push(false);
            Entry<T> minR = find(currentNode.right, currentNode.element);
            currentNode.element = minR.element;
            bypass(minR);
            currentNode = (AVLEntry<T>) operateStack.pop();
            pathStack.pop();
            currentNode.lHeight = 1;
        }
        restoreAVLre(currentNode);
        size--;
        return super.remove(tar);
    }

    @Override
    protected Entry<T> find(Entry<T> cNode, T target) {
        if (cNode == null || cNode.element.compareTo(target) == 0)
            return cNode;
        while (true) {
            if (cNode.element.compareTo(target) > 0) {
                if (cNode.left == null)
                    break;
                else {
                    operateStack.push(cNode);
                    pathStack.push(true);
                    cNode = cNode.left;
                }
            } else if (cNode.element.compareTo(target) == 0)
                break;
            else {
                if (cNode.right == null)
                    break;
                else {
                    operateStack.push(cNode);
                    pathStack.push(false);
                    cNode = cNode.right;
                }
            }
        }
        return cNode;
    }

    /** Method to perform the single rotate operation */
    private AVLEntry<T> singleRotate(AVLEntry<T> cur, boolean mode){
        AVLEntry<T> tmp;
        boolean isRoot = (cur == root);
        if (mode){
            // mode = true, perform the left rotation
            tmp = (AVLEntry<T>) cur.right;
            cur.right = tmp.left;

            if (cur.right == null)
                cur.rHeight = 1;
            else
                cur.rHeight = ((AVLEntry<T>) cur.right).getNodeHeight() + 1;

            tmp.left = cur;
            tmp.lHeight = ((AVLEntry<T>) tmp.left).getNodeHeight() + 1;
        }
        else{
            // mode = false, perform the right rotation
            tmp = (AVLEntry<T>) cur.left;
            cur.left = tmp.right;

            if(cur.left == null)
                cur.lHeight = 1;
            else
                cur.lHeight = ((AVLEntry<T>) cur.left).getNodeHeight() + 1;

            tmp.right = cur;
            tmp.rHeight = ((AVLEntry<T>) tmp.right).getNodeHeight() + 1;
        }
        // Change the root if cur originally is the root node
        if (isRoot)
            root = tmp;
        return tmp;
    }

    /** Method to perform the double rotate operation */
    private AVLEntry<T> doubleRotate(AVLEntry<T> cur, boolean fMode, boolean sMode){
        AVLEntry<T> tmp;
        if (fMode){
            if (sMode) {
                // RR case -- double left rotation
                tmp = singleRotate((AVLEntry<T>) cur.right, true);
                cur.right = tmp;
                tmp = singleRotate(cur, true);
            }
            else{
                // RL case -- right rotation on cur.right, then left rotation on cur
                tmp = singleRotate((AVLEntry<T>) cur.right, false);
                cur.right = tmp;
                tmp = singleRotate(cur, true);
            }
        }
        else{
            if (sMode){
                // LR case -- left rotation on cur.left, then right rotation on cur
                tmp = singleRotate((AVLEntry<T>) cur.left, true);
                cur.left = tmp;
                tmp = singleRotate(cur, false);
            }
            else {
                // LL case -- double right rotation
                tmp = singleRotate((AVLEntry<T>) cur.left, false);
                cur.left = tmp;
                tmp = singleRotate(cur, false);
            }
        }
        return tmp;
    }

    /** Method to restore balance after add operation */
    private void restoreAVLad(AVLEntry<T> currentNode){
        boolean lastDir = false;
        AVLEntry<T> holder;

        while(!operateStack.isEmpty()){
            // retrieve the parent node and the direction from parent node to processing node
            AVLEntry<T> parent = (AVLEntry<T>) operateStack.pop();
            boolean dir = pathStack.pop();

            if (dir){ // parent.left = current node
                parent.lHeight = currentNode.getNodeHeight() + 1;
                if (parent.lHeight - parent.rHeight > 1){
                    if (lastDir) // unbalance due to left child of left subtree
                        holder = singleRotate(parent, false);
                    else // unbalance due to right child of left subtree
                        holder = doubleRotate(parent, true, false);

                    if (holder != root){ // if holder equals root, then operateStack must be empty
                        parent = (AVLEntry<T>) operateStack.pop();
                        pathStack.pop();
                        parent.left = holder;
                        parent.lHeight = holder.getNodeHeight() + 1;
                    }
                }
                currentNode = parent;
            }
            else{
                parent.rHeight = currentNode.getNodeHeight() + 1;
                if (parent.rHeight - parent.lHeight > 1){
                    if (!lastDir) // unbalance due to right child of right subtree
                        holder = singleRotate(parent, true);
                    else // unbalance due to left child of right subtree
                        holder = doubleRotate(parent, false, true);

                    if (holder != root){ // if holder equals root, then operateStack must be empty
                        parent = (AVLEntry<T>) operateStack.pop();
                        pathStack.pop();
                        parent.right = holder;
                        parent.rHeight = holder.getNodeHeight() + 1;
                    }
                }
                currentNode = parent;
            }
            lastDir = dir;
        }
    }

    /** Method to restore balance after remove operation */
    private void restoreAVLre(AVLEntry<T> currentNode){
        AVLEntry<T> holder;

        while(!operateStack.isEmpty()){
            // retrieve the parent node and the direction from parent node to processing node
            AVLEntry<T> parent = (AVLEntry<T>) operateStack.pop();
            boolean dir = pathStack.pop();

            if (dir){ // parent.left = current node
                parent.lHeight = currentNode.getNodeHeight() + 1;
                if (parent.rHeight - parent.lHeight > 1){
                    AVLEntry<T> tmp = (AVLEntry<T>) parent.right;
                    if (tmp.lHeight > tmp.rHeight)
                        holder = doubleRotate(parent, true, false); // RL
                    else
                        holder = singleRotate(parent, true); //RR

                    if (holder != root){ // if holder equals root, then operateStack must be empty
                        parent = (AVLEntry<T>) operateStack.peek();
                        parent.right = holder;
                        parent.rHeight = holder.getNodeHeight() + 1;
                    }
                }
                currentNode = parent;
            }
            else{
                parent.rHeight = currentNode.getNodeHeight() + 1;
                if (parent.lHeight - parent.rHeight > 1){
                    AVLEntry<T> tmp = (AVLEntry<T>) parent.left;
                    if (tmp.lHeight > tmp.rHeight)
                        holder = singleRotate(parent, false); // LL
                    else
                        holder = doubleRotate(parent, false, true); //LR

                    if (holder != root){ // if holder equals root, then operateStack must be empty
                        parent = (AVLEntry<T>) operateStack.pop();
                        pathStack.pop();
                        parent.left = holder;
                        parent.lHeight = holder.getNodeHeight() + 1;
                    }
                }
                currentNode = parent;
            }
        }
    }

    public static void main(String[] args) {
       AVLTree<Integer> t = new AVLTree<>();
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
