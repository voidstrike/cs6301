//** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan

package cs6301.g42;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs6301.g00.Timer;

public class DaulPivotQuickSortSp5P2 {
	// swap function
	public static<T extends Comparable<? super T>> void swap(T[] arr, int i, int j){
		T temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}// end of swap function
	
	//dual_pivot_partition
    public static<T extends Comparable<? super T>> int[] dual_pivot_partition(T[] arr, int begin_sub_array, int end_sub_array){
        Random rand = new Random();
        // select 2 pivots uniformly at random in arr[p..r]
        int x1 = begin_sub_array + rand.nextInt(end_sub_array - begin_sub_array);
        int x2= begin_sub_array + rand.nextInt(end_sub_array - begin_sub_array);
        
        swap( arr, begin_sub_array, x1);
        swap( arr, end_sub_array, x2);
        
		// Choose outermost elements as pivots
		if(arr[begin_sub_array].compareTo(arr[end_sub_array]) > 0){
			swap( arr, begin_sub_array, end_sub_array);
		}
	    T pivot1 = arr[begin_sub_array];
	    T pivot2 = arr[end_sub_array];

	    // Partition A according to invariant below
	    int l = begin_sub_array + 1, g = end_sub_array - 1, k = l;
	    while( k <= g){
	    	if(arr[k].compareTo(pivot1) < 0){
	    		swap(arr, k, l);
	    		++l;
	    	}else if( arr[k].compareTo(pivot2) >= 0){
	    		while( arr[g].compareTo(pivot2) > 0 && k < g) --g;
	    		swap( arr, k, g);
	    		--g;
	    		if( arr[k].compareTo(pivot1) < 0){
	    			swap( arr, k, l);
	    			++l;
	    		}
	    	}
	    	++k;
	    }
	    --l;
	    ++g;
	    
	    // Swap pivots to final place
	    swap(arr, begin_sub_array, l);
	    swap(arr, end_sub_array, g);
    	
    	return new int[]{l, g};
    }// end of dual_pivot_partition
    
    
    
    // quickSort main body with dual pivot partition
    private static<T extends Comparable<? super T>> void quickSort(T[] arr, int begin_sub_array, int end_sub_array){
    	if ( end_sub_array > begin_sub_array){

    		int result[] = dual_pivot_partition(arr, begin_sub_array, end_sub_array);
    	    // Recursively sort partitions
    		quickSort( arr, begin_sub_array, result[0] - 1);
    	    quickSort( arr, result[0] + 1, result[1] - 1);
    	    quickSort( arr, result[1] + 1, end_sub_array);

    	}

    }// end of quickSort
    
    // call the quickSort, run version 1 if true
    public static<T extends Comparable<? super T>> void quickSort(T[] arr){
    	quickSort(arr, 0, arr.length - 1);
    }// end 
      
    
    public static void main(String[] args){
    	
    	Timer t = new Timer();
    	// create a shuffled array
    	int inputSize = 8000000; // 32MB
    	List<Integer> dataList = new ArrayList<Integer>();
    	for(int i = 0; i < inputSize; i++){
    		if( i > 60 && i < 80){
    			dataList.add(70);
    		}else{
    		    dataList.add(i);
    		}
    	}
    	Collections.shuffle(dataList);
    	Integer[] num = new Integer[dataList.size()];
    	for(int i = 0; i < dataList.size(); i++){
    		num[i] = dataList.get(i);
    	}
    	
    	/*
    	for (int i = 0; i < num.length; i++) {
    	      System.out.println(num[i]);
    	    }*/

    	t.start();
    	quickSort(num);
    	System.out.println(t.end());
    	
    	/*
    	for (int i = 0; i < num.length; i++) {
    	      System.out.println(num[i]);
    	    }*/
    	
    }
}
/*
Dual Pivot Quick Sort:
n = 8,000,000 (8MB)
Time: 3248 msec.
Memory: 224 MB / 432 MB.

n = 32MB
Time: 24730 msec.
Memory: 791 MB / 1480 MB.

Comment: the dual-pivot quick sort(24730 msec) is one time faster than the original quick sort(50635 msec)
--------------------------------
Original Quick Sort

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


*/