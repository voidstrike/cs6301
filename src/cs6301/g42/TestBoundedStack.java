package cs6301.g42;

public class TestBoundedStack {
    
    public static void main(String args[]){
    	// double and integer array, both 11 elements
    	double[] doubleElements = { 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.1, 11.1 };
        int[] intElements = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        
        ArrayBoundedStack<Double> doubleStack = new ArrayBoundedStack<>(10);
        ArrayBoundedStack<Integer> intStack = new ArrayBoundedStack<>(10);
        
        //test push function
        try{
        	System.out.println("\nPush elements to double stack");
        	
        	for(double elem : doubleElements){
        		System.out.printf("%.1f ", elem);
        		doubleStack.push(elem);
        	}
        }catch(IllegalStateException illegalStateException){
        	System.err.println();
        	illegalStateException.printStackTrace();
        }
        
        //test pop function
        try{
        	System.out.println("\nPush elements to integer stack");
        	
        	for(int elem : intElements){
        		System.out.printf("%d ", elem);
        		intStack.push(elem);
        	}
        }catch(IllegalStateException illegalStateException){
    	    System.err.println();
    	    illegalStateException.printStackTrace();
        }
        
        try{
        	System.out.println("\nPop elements from integer stack");
        	int popElem;
        	
        	while(true){
        		popElem = intStack.pop();
        		System.out.printf("%d ", popElem);
        	}
        }catch (IllegalStateException illegalStateException){
        	System.err.println();
        	illegalStateException.printStackTrace();
        }
        
    }
}
