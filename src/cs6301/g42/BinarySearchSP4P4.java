package cs6301.g42;

import cs6301.g00.Timer;

public class BinarySearchSP4P4 {
    // Preconditions: arr[0..n-1] is sorted, and arr[0] <= x < arr[n-1].
    // Returns index i such that arr[i] <= x < arr[i+1].
    public static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x){
    	int left  = 0;
    	int right = arr.length-1;
    	
    	if( arr[right].compareTo(x) == 0)
    		return right;
    	
    	return binarySearch(arr, x, left, right);
    }
    
    private static<T extends Comparable<? super T>> int binarySearch(T[] arr, T x, int left, int right){
    	if( left >= right){
    		if(arr[right].compareTo(x) == 0)
    			return right;
    		return -1;
    	}
    	int mid = (left+right)/2;
    	int compare = arr[mid].compareTo(x);
    	if( compare == 0){
    		while( arr[mid].compareTo( arr[mid+1] ) == 0){
    			mid++;
    		}
    		return mid;
    	}else if( compare > 0){
    		return binarySearch(arr, x, left, mid - 1);
    	}else{
    		return binarySearch(arr, x, mid + 1, right);
    	}
    	
    }
    
    public static void main(String args[]){
		Integer[] arr = new Integer[100];
		for(int i = 0; i < arr.length; i++){
			arr[i] = i;
		}
		
		for(int i = 60; i < 90; i++){
			arr[i] = 66;
		}
		
		int index = binarySearch(arr, 90);
		System.out.println(index);
	}
}
