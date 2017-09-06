package cs6301.g42;

import java.util.NoSuchElementException;

//problem 5.
public class ArrayBoundedQueue<E> {
	private E[] arr;
	private int size;           //size of queue (max number of elements it can contain)
	private int front;          //elements removed from front
	private int rear;           //elements added at rear
	private int count;          //number of elements currently in queue
	private final int min = 16; //minimum size of queue
	
	
	/*
	 * constructor with bound of 'input size'
	 */
    public ArrayBoundedQueue(){
    	this(16);
    }
    
	public ArrayBoundedQueue(int inputSize){
		size = inputSize > min ? inputSize : min;
		front = 0;
		rear = 0;
		count = 0; //initially number of elements in queue is 0
		arr = (E[]) new Object[size];
	}
	
	
	/*
	 *  adds element in queue, at rear
	 *  offer, poll, peek, isEmpty methods
	 */
	public void offer(E elem){
		// doubles queue size if it is over 90% full
		// halves queue size if it is less than 25% full
		//( only if after halves it, the size is still > 16) 
		if( (count*1.0 / size) > 0.9){
			resize(size*2);
		}
		
		//it will not be full, since will keep detect 
		//how full the queue is and double the size if necessary
		arr[rear] = elem;
		rear = (rear+1) % size;
		count++;
	}
	
	/*
	 * remove element from queue, at front
	 */
	public E poll(){
		E pollElem;
		if(isEmpty()){
			throw new NoSuchElementException("Queue is empty, cannot remove element");
		}else{
		    pollElem = arr[front];
		    arr[front] = null;
		    front = (front+1) % size;
		    count--;    
		}
		
		// detect if queue is only 25% full
	    if( (count*1.0 / size) < 0.25 && ( size/2 > min) ){
		    resize(size/2);
	    }
		
		return pollElem;
	}
	/*
	 *  return the element at front
	 */
	public E peek(){
		if(isEmpty()){
			throw new NoSuchElementException("Queue is empty");
		}
		return  arr[front];
	}
	/*
	 * return true if queue is empty
	 */
	public boolean isEmpty(){
		return count==0;
	}
	
	// resize methods
	public void resize(int newSize){
		E[] tempArr = (E[]) new Object[newSize];
		System.out.printf("\nResize the queue to %d size (%d element(s) in queue). \n", newSize, count);
		
		// copy old array to the new large/small array
		int iter = 0;
		int cursor = front;
		while( (cursor) != rear){
			tempArr[iter++] = arr[cursor];
			cursor = (cursor+1) % size;
		}
		
		arr = tempArr;
		front = 0;
		rear = count;
		size = newSize;
	}
	
	
}
