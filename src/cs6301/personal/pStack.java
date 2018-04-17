package cs6301.personal;

/**
 * Created by Alan Lin on 9/5/2017.
 */
public class pStack<T> {
    private T[] stackMemory;
    private int maxSize;
    private int head, tail;

    public pStack(int requireSize){
        maxSize = requireSize;
        stackMemory = (T[]) new Object[maxSize];
        head = 0;
        tail = -1;
    }

    public void push(T target) throws stackFullException{
        if (tail + 1 >= maxSize){
            throw new stackFullException(("Stack is already full"));
        }
        else{
            tail++;
            stackMemory[tail] = target;
        }
    }

    public T pop(){
        if (tail < 0){
            return null;
        }
        else{
            tail--;
            return stackMemory[tail+1];
        }
    }

    static class stackFullException extends Exception{
        public stackFullException(String msg){
            super(msg);
        }
    }

    public static void main(String[] args){
        pStack<Integer> pstack = new pStack<>(1);
//        try{
//            pstack.push(new Integer(10));
//        }catch (stackFullException e){
//            System.out.println(e);
//        }
//        try{
//            pstack.push(new Integer(11));
//        }catch (stackFullException e){
//            System.out.println(e);
//        }
//        try{
//            pstack.push(new Integer(12));
//        }catch (stackFullException e){
//            System.out.print(e);
//        }
        System.out.println(pstack.pop());
        System.out.println(pstack.pop());
    }
}
