package cs6301.g42;

import java.util.NoSuchElementException;

public class TestBoundedQueue {
	public static void main(String[] args) {
        ArrayBoundedQueue<Integer> Queue = new ArrayBoundedQueue<Integer>(16); //Initial size of queue
      
        for(int i = 0; i < 60; i++){
        	Queue.offer(i);
        }

 
        System.out.print("\nElements polled from Queue: \n");
        try{
        	for(int i = 0; i < 65; i++){
            	if( i%10 == 0)
            		System.out.println();
            	System.out.print(Queue.poll()+" ");
            }
        }catch(NoSuchElementException noSuchElementException){
        	System.err.println();
        	noSuchElementException.printStackTrace();
        }
        
       
    }
}
