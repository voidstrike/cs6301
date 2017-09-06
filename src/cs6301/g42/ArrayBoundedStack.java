package cs6301.g42;

import java.util.NoSuchElementException;

//problem 6.
public class ArrayBoundedStack<E>{
    private E[] arr;
    private final int size;
    private int top;
   
    
    //constructor with fixed bound of 'input size'
    public ArrayBoundedStack(){
    	this(10);
    }
    
    public ArrayBoundedStack(int inputSize){
    	size = inputSize > 0 ? inputSize : 10;
    	top = -1;
    	arr = (E[]) new Object[size];
    }
    
    // push, pop, peek and isEmpty methods
    public void push(E element){
    	// if full, throw exception
    	if(top == size-1){
    		throw new IllegalStateException("The stack is full, cannot add element");
    	}
    	arr[++top] = element;
    }
    
    public E pop(){
    	if(top == -1){
    		throw new NoSuchElementException("Stack is empty, cannot pop");
    	}
    	return arr[top--];
    }
    
    public E peek(){
    	if(top == -1){
    		throw new NoSuchElementException("Stack is empty");
    	}
    	return arr[top];
    }
    
    public boolean isEmpty(){
    	return top == -1;
    }
}
