package cs6301.psp6;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.lang.Exception;

/**
 * Created by Alan Lin on 10/3/2017.
 */
public class BinaryHeap<T> {
    protected T[] elements;
    protected Comparator<T> com;
    protected int current, maxSize;

    static class HeapFullException extends Exception{
        HeapFullException(String msg){
            super(msg);
        }
    }

    BinaryHeap(T[] q, Comparator<T> comp, int size){
        // Initialize the heap, the elements in q doesn't matter
        elements = q;
        com = comp;
        current = 0;
        // Specify the maxSize of this heap, if the size is smaller than q.length, accept this value, otherwise take q.length as maxSize
        maxSize = elements.length;
    }

    public void buildHeap(T[] candidates){
        // Fill the heap with the elements in candidates
        for (T t : candidates){
            elements[current] = t;
            current++;
        }
        int tmp = (current-1)>>>1;
        // PercolateDown from tmp to 0
        while (tmp >= 0){
            percolateDown(tmp);
            tmp--;
        }
    }

    public void insert(T x){
        try {
            add(x);
        }
        catch (HeapFullException e){
            e.printStackTrace();
        }
    }
    public void add(T target) throws HeapFullException{
        if (current < maxSize){
            elements[current] = target;
            percolateUP(current);
            current++;
        }
        else{
            // The heap is full in this situation
            throw new HeapFullException("Heap is Full");
        }
    }

    public T deleteMin(){
        return remove();
    }
    public T remove() throws  NoSuchElementException{
        T min = null;
        if (current <= 0){
            throw new NoSuchElementException("Heap is Empty");
        }
        else{
            min = elements[0];
            elements[0] = elements[current-1];
            current--;
            percolateDown(0);
        }
        return min;
    }

    public T min(){
        return peek();
    }
    public T peek()throws NoSuchElementException{
        if (current == 0){
            throw new NoSuchElementException("heap underflow");
        }
        else{
            return elements[0];
        }
    }

    public T[] heapSort(){
        T[] result = (T[]) new Object[current];
        for(int i=0; i<result.length;i++){
            result[i] = remove();
        }
        return result;
    }

    public static<T> void heapSort(T[] A, Comparator<T> comp) {
        BinaryHeap<T> helper = new BinaryHeap<>(A, comp, A.length);
        T[] result;
        helper.buildHeap(A);
        result = helper.heapSort();
        int i = 0;
        for (T e:result){
            A[i++] = e;
        }
    }

    // Helper Function
    private int parent(int i){
        if(i <= 0){
            return -1;
        }
        else
            return (i-1)>>>1;
    }

    protected void percolateUP(int i){
        T tmp = elements[i];
        while (i > 0 && com.compare(tmp, elements[parent(i)]) < 0){
            elements[i] = elements[parent(i)];
            i = parent(i);
        }
        elements[i] = tmp;
    }

    protected void percolateDown(int i){
        T tmp = elements[i];
        int child = 2*i + 1;
        while(child <= current-1){
            // if this child has a brother and that brother is small than it, go to that node
            if (child < current-1 && com.compare(elements[child], elements[child+1])>0)
                child++;
            if (com.compare(tmp, elements[child])<=0)
                break;
            elements[i] = elements[child];
            i = child;
            child = 2*i+1;
        }
        elements[i] = tmp;
    }

    public static void main(String[] args){
        // Test code below
        Integer[] testS = {1,4,6,8,0,2,5,6,8,23,10};
        Comparator<Integer> tmpA = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2)
                    return 1;
                else if (o1 < o2)
                    return -1;
                else
                    return 0;
            }
        };

        Comparator<Integer> tmpD = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2)
                    return -1;
                else if (o1 < o2)
                    return 1;
                else
                    return 0;
            }
        };

        heapSort(testS, tmpA);
        for(int e:testS){
            System.out.println(e);
        }

        System.out.println();

        heapSort(testS, tmpD);
        for(int e:testS){
            System.out.println(e);
        }
    }
}
