package cs6301.personal;

/**
 * Created by Alan Lin on 9/5/2017.
 */
public class pQueue<T> {
    private T[] queueMemory;
    private int maxSize;
    private int currentSize = 16;
    private int head, tail;
    private boolean crossSign = false;

    public pQueue(){
        maxSize = 4096;
        queueMemory = (T[]) new Object[maxSize];
        head = 0;
        tail = -1;
    }

    public boolean offer(T target){
        if (tail - head + 1 == currentSize){
            // queue is full, offer failed
            return false;
        }
        else{
            queueMemory[(++tail) % currentSize] = target;
            if (tail >= currentSize) crossSign = true;
            return true;
        }
    }

    public T peek(){
        if (tail - head  < 0){
            // No element in current queue
            return null;
        }
        else{
            return queueMemory[head];
        }
    }

    public T poll(){
        int tmp = head;
        if (tail - tmp  < 0){
            // No element in current queue
            return null;
        }
        head++;
        if (head >= currentSize && tail >= currentSize){
            head -= currentSize;
            tail -= currentSize;
            crossSign = false;
        }
        return queueMemory[tmp];
    }

    public boolean resize(){
        // this method will return false if nothing executed
        // return true if resize process has been executed
        int currentElement = tail - head + 1;
        int tmp; // this attr will only be used in halve phase
        float load = (float)currentElement / (float)currentSize;
        if (load >= 0.9){
            if (currentSize * 2 > maxSize) return false;
            // resize failed if double the space will exceed the maxSize
            if (crossSign){
                // In this case, the head and the tail across the bound of this queue
                // copy all elements in [0:tail] to [currentSize:currentSize+tail]
                // if crossSign failed, double the available space then the works done
                for (int i=0; i<=tail%currentSize; i++){
                    queueMemory[i+currentSize] = queueMemory[i];
                }
                crossSign = false;
            }
            currentSize = currentSize * 2;
            return true;
        }
        else if (load <= 0.25){
            // the load of current queue is smaller than 25%, halve this queue if possible
            if (currentSize <= 16) return false; // minimum size is 16, nothing will be change
            else{
                tmp = currentSize / 2;
                if (crossSign){
                    // Since the load is less than 0.25, there is only one situation
                    for(int i=head; i<currentSize; i++){
                        queueMemory[i-tmp] = queueMemory[i];
                    }
                    head -= tmp;
                    return true;
                }
                else{
                    // No cross sign, three situation
                    if (head < tmp && tail < tmp)
                        return true; // in halved bound, do nothing
                    else if (head >= tmp && tail >= tmp){
                        // both head and tail in upper half, copy to lower half
                        for (int i=0; i<=tail; i++){
                            queueMemory[head - tmp + i] = queueMemory[head + i];
                        }
                        head -= tmp;
                        tail -= tmp;
                        return true;
                    }
                    else{
                        // tail cross the half bound, convert [tmp:tail] to [0:tail-tmp]
                        for (int i=tmp; tmp<=tail; i++){
                            queueMemory[i - tmp] = queueMemory[i];
                        }
                        crossSign = true;
                        return true;
                    }
                }
            }
        }
        else{
            // the load of current queue is between 0.25 and 0.9
            // the resize procedure will not be executed
            return false;
        }
    }

    public static void main(String[] args){
        //TODO
        pQueue<Integer> pqueue = new pQueue<>();
        for(int i=1; i<=16; i++){
            pqueue.offer(new Integer(i));
        }
        for (int i=1; i<=16; i++){
            pqueue.poll();
        }
        pqueue.poll();
        System.out.print(pqueue.poll());
    }
}
