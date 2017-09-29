//** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan

package cs6301.sp5;

import cs6301.g00.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PartitionsSP5P1 {
	// swap function
	public static<T extends Comparable<? super T>> void swap(T[] arr, int i, int j){
		T temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}// end of swap function
	
	//partition version 1
    public static<T extends Comparable<? super T>> int partition1(T[] arr, int begin_sub_array, int end_sub_array){
        Random rand = new Random();
        // select i uniformly at random in arr[p..r]
        int i = begin_sub_array + rand.nextInt(end_sub_array - begin_sub_array);
        swap(arr, i, end_sub_array);  // exchange arr[i], arr[end_sub_array]
        T pivot = arr[end_sub_array];
        i = begin_sub_array - 1;
        
        for( int j = begin_sub_array; j < end_sub_array; j++){
        	if( arr[j].compareTo(pivot) < 0){
        		i++;
        		swap(arr, i, j);	
        	}
        }
        
        // bring pivot back to the middle
        swap(arr, i + 1, end_sub_array);
        return i + 1;
    }// end of partition version 1
    
    //partition version 2
    public static<T extends Comparable<? super T>> int partition2(T[] arr, int begin_sub_array, int end_sub_array){
    	Random rand = new Random();
        // select i uniformly at random in arr[p..r]
        int index = begin_sub_array + rand.nextInt(end_sub_array - begin_sub_array);
        T x = arr[index];
        int i = begin_sub_array - 1, j = end_sub_array + 1;
        
        while(true){
        	do{
        		i++;
        	}while( arr[i].compareTo(x) < 0);
        	
        	do{
        		j--;
        	}while( arr[j].compareTo(x) > 0);
        	
        	if( i >= j){
        		return j;
        	}
        	swap(arr, i, j);
        	i++;
        	j--;
        }    
    }// end of partition 2 function
    
    // quickSort main body with partition version 1
    private static<T extends Comparable<? super T>> void quickSort1(T[] arr, int begin_sub_array, int end_sub_array){
    	if( begin_sub_array < end_sub_array){
    		//pivot index
    		int q = partition1(arr, begin_sub_array, end_sub_array);
    		quickSort1( arr, begin_sub_array, q - 1);
    		quickSort1( arr, q + 1, end_sub_array);
    	}
    }// end of quickSort 1
    
    // quickSort main body with partition version 2
    private static<T extends Comparable<? super T>> void quickSort2(T[] arr, int begin_sub_array, int end_sub_array){
    	if( begin_sub_array < end_sub_array){
    		//pivot index
    		int q = partition2(arr, begin_sub_array, end_sub_array);
    		quickSort2( arr, begin_sub_array, q - 1);
    		quickSort2( arr, q, end_sub_array);
    	}
    }// end of quickSort 2
    
    // call the quickSort, run version 1 if true
    public static<T extends Comparable<? super T>> void quickSort(T[] arr, boolean version){
    	if( version = true)
    		quickSort1(arr, 0, arr.length - 1);
    	else
    		quickSort2(arr, 0, arr.length - 1);
    }// end 
    
    public static void main(String[] args){
    	
    	Timer t = new Timer();
    	// create a shuffled array
    	int inputSize = 32000000; // 8MB
    	List<Integer> dataList = new ArrayList<Integer>();
    	for(int i = 0; i < inputSize; i++){
    		dataList.add(i);
    	}
    	
    	// shuffle for randomly ordered array
    	Collections.shuffle(dataList);
    	Integer[] num = new Integer[dataList.size()];
    	
    	// random array or
    	// descending order array
    	for(int i = 0; i < dataList.size(); i++){
    		//num[i] = dataList.get(i);// get random order from shuffle list
    		num[i] = dataList.size()-i;// descending order
    	}
    	
    	/*
    	for (int i = 0; i < num.length; i++) {
    	    System.out.println(num[i]);
    	}*/
    	
    	// true for version 1, false for version 2
    	boolean version = true;
    	t.start();
    	quickSort(num, version);
    	System.out.println(t.end());
    	
    	/*
    	for (int i = 0; i < num.length; i++) {
    	    System.out.println(num[i]);
    	}*/
    }
    
}


/*
random array

n = 8,000,000 (8MB)
output:
version 1:
Time: 3525 msec.
Memory: 295 MB / 490 MB.

version 2:
Time: 3493 msec.
Memory: 295 MB / 484 MB


n = 32MB
version 1:
Time: 50918 msec.
Memory: 869 MB / 1003 MB.
version 2:
Time: 50635 msec.
Memory: 869 MB / 1003 MB.

---------------------------------------
descending order array

n = 8,000,000 (8MB)
output:
version 1:
Time: 15413 msec.
Memory: 493 MB / 961 MB.

version 2:
Time: 14892 msec.
Memory: 493 MB / 961 MB.


n = 16MB
version 1:
Time: 15229 msec.
Memory: 646 MB / 1003 MB.

version 2:
Time: 15413 msec.
Memory: 646 MB / 1003 MB.

*/