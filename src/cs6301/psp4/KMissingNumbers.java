package cs6301.psp4;

import java.util.List;
import java.util.ArrayList;

public class KMissingNumbers {

	public static List<Integer> findKMissingNumber(int[] A){
		List<Integer> result = new ArrayList<>();
		findK(A, 0, A.length-1, result);
		return result;
	}

	public static void findK(int[] input, int start, int end, List<Integer> result){

		int middle = (start + end) / 2;
		if( input[end]-input[start] == end - start || start == end){
			// Nothing should be done in this situation
		}
		else if(start+1 == end){
			for(int i=input[start]+1; i<input[end];i++){
				result.add(i);
			}
		}
		else{
			// Recursive call
			findK(input, start, middle, result);
			findK(input, middle, end, result);
		}
	}
	
	public static void main(String args[]){

		// Test code
		int arr[] = {1,3,5,7,9,12};
		List<Integer> missingNum;

		missingNum = findKMissingNumber(arr);
		System.out.println(missingNum);
		
	}
	

}
