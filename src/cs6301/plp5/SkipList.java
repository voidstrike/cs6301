package cs6301.plp5;

import java.util.*;

/**
 * Created by Alan Lin on 10/30/2017.
 *
 */
public class SkipList<T extends Comparable<? super T>> implements Iterable<T>{
    /** nested class for the node of SkipList */
    static class SLNode<T>{
        T element;
        ArrayList<SLInnerNode<T>> pointerList;

        SLNode(T tar){
            element = tar;
            pointerList = new ArrayList<>();
        }

        SLNode(T tar, int num){ // Create isolate node with selected # of level
            this(tar);
            for (int i=0; i<=num; i++) // include the 0-layer
                pointerList.add(new SLInnerNode<>(null, null));
        }

        SLInnerNode<T> getComponent(int tar){
            return pointerList.get(tar);
        }

        SLNode<T> next(){ // Default next(), using Lv.0 list
            return pointerList.get(0).next;
        }
    }

    /** nested class for the component of SkipList node */
    static class SLInnerNode<T>{
        SLNode<T> next, prev;
        int gap;

        // Constructors
        SLInnerNode(SLNode<T> pre, SLNode<T> nxt){
            this.prev = pre;
            this.next = nxt;
            this.gap = -1;
        }

        SLInnerNode(SLNode<T> pre, SLNode<T> nxt, int cGap){
            this(pre, nxt);
            this.gap = cGap;
        }

        public void setGap(int gap){
            this.gap = gap;
        }
    }

    private SLNode<T> head, tail; // Dummy head and tail nodes
    private ArrayDeque<SLNode<T>> operatePath; // Stack used to store the path to target node
    private ArrayDeque<Integer> stepSum;
    private int count, maxLevel; // count is the element stores in this SkipList and maxLevel indicates the maxLevel of this SkipList
    private Random ranGen; // Random Generator to generate level -- cooperated with mask

    // Constructor
    public SkipList(){
        head = new SLNode<>(null);
        tail = new SLNode<>(null);
        head.pointerList.add(new SLInnerNode<>(null, tail, 1)); // Add initial component to head
        tail.pointerList.add(new SLInnerNode<>(head, null, -1)); // Add initial component to tail

        operatePath = new ArrayDeque<>();
        stepSum = new ArrayDeque<>();
        ranGen = new Random(System.currentTimeMillis()); // Seed - Current System Time
        count = 0; maxLevel = 0;
    }

    /** Method to add x to list.
     *  If x already exists, replace it.
     *  Return true if new node is added to list */
    public boolean add(T x){
        find(x);
        SLNode<T> parent = operatePath.peek(); // Node list in Lv.0
        SLNode<T> target = parent.getComponent(0).next;
        if (target.element != null && x.compareTo(target.element) == 0) {
            target.element = x; // Target element is already in the SkipList
            return false;
        }
        else{
            // Target element isn't in the SkipList
            int level = chooseLevel(); // Choose the # of levels for this new node
            if (level == maxLevel+1){
                updateHeadTail();
                maxLevel++;
            }
            SLNode<T> tmp = new SLNode<>(x, level);
            for (int i=0; i<=level; i++){
                parent = operatePath.pop();
                addAfter(parent, tmp, i);
            }
        }
        count++;
        return true;
    }

    /** Method to remove x from list.
     *  Removed element is returned.
     *  Return null if x not in list */
    public T remove(T x){
        find(x);
        SLNode<T> parent = operatePath.peek(); // Node list in Lv.0
        SLNode<T> target = parent.getComponent(0).next;
        if (target.element == null || x.compareTo(target.element) != 0) {
            return null; // x is not in this SkipList
        }
        else {
            // x is in this SkipList
            T result = target.element;
            int i = 0; // Pivot used to count the Lv of SkipList
            while(!operatePath.isEmpty()){
                parent = operatePath.pop();
                if (parent.pointerList.get(i).next == target)
                    removeAfter(parent, target, i);
                i++;
            }
            count--;
            // handle no element level
            for (int j=head.pointerList.size()-1; j >0 ; j--) {
                if (head.getComponent(j).next == tail) {
                    head.pointerList.remove(j);
                    maxLevel--;
                }
                else
                    break;
            }

            return result;
        }
    }

    /** Method to find smallest element that is greater than or equal to x */
    public T ceiling(T x){
        find(x);
        if (operatePath.isEmpty())
            return null;
        else{
            SLNode<T> parent = operatePath.peek();
            SLNode<T> target = parent.getComponent(0).next;
            if(target.element == null)
                return null;
            else
                return target.element;
        }
    }

    /** Method to find the largest element that is less than or equal to x */
    public T floor(T x){
        find(x);
        if (operatePath.isEmpty())
            return null;
        else{
            SLNode<T> parent = operatePath.peek();
            SLNode<T> target = parent.getComponent(0).next;
            if (target.element != null && x.compareTo(target.element) == 0)
                return target.element;
            else
                return parent.element; // could be null
        }
    }

    /** Method to tell does list contains x */
    public boolean contains(T x){
        if (x == null)
            return false; // cannot contain null in SkipList
        find(x);
        SLNode<T> tmp = operatePath.peek();
        SLNode<T> target = tmp.getComponent(0).next;
        return target != null && x.compareTo(target.element) == 0;
    }

    /** Method to return the first element of this SkipList, return null if no element */
    public T first(){
        SLNode<T> tracker = head.getComponent(0).next;
        return tracker.element;
    }

    /** Method to return the last element of this SkipList, return null if no element */
    public T last(){
        SLNode<T> tracker = tail.getComponent(0).prev;
        return tracker.element;
    }

    /** Method to return the number of elements in the list */
    public int size(){
        return count;
    }

    /** Method to tell whether list is empty */
    public boolean isEmpty(){
        return count == 0;
    }

    /** Override iterator */
    @Override
    public Iterator<T> iterator() {
        return new SLIterator(head);
    }
    /** Nested Iterator Class */
    class SLIterator implements Iterator<T>{
        SLNode<T> tracker;

        SLIterator(SLNode<T> start){
            tracker = start;
        }

        @Override
        public boolean hasNext() {
            return tracker.getComponent(0).next != tail;
        }

        @Override
        public T next() {
            tracker = tracker.getComponent(0).next;
            return tracker.element;
        }
    }

    // Additional Operations
    /** Method to reorganize the elements of the list into a perfect skip list */
    public void rebuild(){
        //TODO
    }

    /** Method to return the element at index n of list.
     *  First element is at index 0 */
    public T get(int n){
        //TODO
        return null;
    }

    // Helper functions
    private void find(T tar){
        operatePath.clear(); // initialize the operate stack

        SLNode<T> holder = head;
        int steps = 0;
        for (int i = maxLevel; i >= 0; i--){
            SLInnerNode<T> tmp = holder.pointerList.get(i); // get next[i] of this block

            while(tmp.next.element != null && tar.compareTo(tmp.next.element) > 0){
                holder = tmp.next;
                steps += tmp.gap; // Accumulate the gap between nodes
                tmp = holder.pointerList.get(i);
            }
            operatePath.push(holder);
            stepSum.push(steps);
        }
        assert !operatePath.isEmpty(); // operatePath cannot be empty after this operation
    }

    private int chooseLevel(){
        int mask = (1<<maxLevel) - 1;
        int lev = Integer.numberOfTrailingZeros(ranGen.nextInt() & mask);
        if (lev > maxLevel)
            return maxLevel + 1;
        else
            return lev;
    }

    private void updateHeadTail(){
        head.pointerList.add(new SLInnerNode<>(null, tail, 1));
        operatePath.addLast(head);
        tail.pointerList.add(new SLInnerNode<>(head, null));
    }

    /** Method to change the relationship between nodes after add operation */
    private void addAfter(SLNode<T> parent, SLNode<T> newIn, int index){
        SLNode<T> tmp = parent.getComponent(index).next;
        parent.getComponent(index).next = newIn;
        newIn.getComponent(index).prev = parent;
        newIn.getComponent(index).next = tmp;
        tmp.getComponent(index).prev = newIn;

    }
    /** Method to change the relationship between nodes after remove operation */
    private void removeAfter(SLNode<T> parent, SLNode<T> reIn, int index){
        SLNode<T> tmp = reIn.pointerList.get(index).next;
        parent.getComponent(index).next = tmp;
        tmp.getComponent(index).prev = parent;
    }

    /** Method to print the structure of current SkipList */
    public void printList(){
        for (int i=maxLevel; i>=0; i--) {
            System.out.print("Level " + i + ": ");
            SLNode<T> currentLine = head.pointerList.get(i).next;
            while(currentLine != tail){
                System.out.print(currentLine.element + " ");
                currentLine  = currentLine.pointerList.get(i).next;
            }
            System.out.println();
        }
    }

    // Helper function for enhanced operations
    private void eliminateHigherLevel(){
        SLNode<T> tracker = head;
        SLInnerNode<T> tmp = tracker.getComponent(0);
        tracker.pointerList.clear();
        tracker.pointerList.add(tmp);
        tracker = tracker.getComponent(0).next;
        while(true){
            tmp = tracker.getComponent(0);
            tracker.pointerList.clear();
            tracker.pointerList.add(tmp);
            if (tracker.element == null)
                break;
            tracker = tracker.getComponent(0).next;
        }
    }

    public static void main(String[] args){
        // Test code below
        SkipList<Integer> t = new SkipList<>();
        Scanner in = new Scanner(System.in);

        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : \n");
                t.add(x);
                t.printList();
            } else if(x < 0) {
                System.out.print("Remove " + -x + " : \n");
                t.remove(-x);
                t.printList();
            } else {
                System.out.print("Final: \n");
                t.printList();
                return;
            }
        }
    }

}
