package cs6301.plp4;

import java.util.Comparator;

/**
 * Created by Alan Lin on 10/23/2017.
 */
public class IndexedHeap<T extends Index> extends BinaryHeap<T>{

    /** Constructor */
    public IndexedHeap(T[] q, Comparator<T> comp, int n){
        super(q, comp, n);
    }
    public IndexedHeap(T[] q, Comparator<T> comp){
        super(q, comp);
    }

    public void update(int index){
        percolateUP(index);
    }

    public T get(int index){
        return elements[index];
    }

    /** Auxiliary Methods */
    @Override
    protected void percolateUP(int i) {
        T tmp = elements[i];
        tmp.putIndex(i);
        while (i > 0 && com.compare(tmp, elements[parent(i)]) < 0){
            elements[i] = elements[parent(i)];
            elements[i].putIndex(i);
            i = parent(i);
        }
        elements[i] = tmp;
        elements[i].putIndex(i);
    }

    @Override
    protected void percolateDown(int i) {
        T tmp = elements[i];
        if (tmp == null)
            return; // In this case, there is no element in the Heap
        tmp.putIndex(i);
        int child = 2*i + 1;
        while(child <= current-1){
            // if this child has a brother and that brother is small than it, go to that node
            if (child < current-1 && com.compare(elements[child], elements[child+1])>0)
                child++;
            if (com.compare(tmp, elements[child])<=0)
                break;
            elements[i] = elements[child];
            elements[i].putIndex(i);
            i = child;
            child = 2*i+1;
        }
        elements[i] = tmp;
        elements[i].putIndex(i);
    }
}
