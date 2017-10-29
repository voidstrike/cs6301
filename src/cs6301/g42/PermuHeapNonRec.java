package cs6301.g42;

public class PermuHeapNonRec {

	// generating all possible permutations of n elements 
	// using Heap's algorithm (non-recursive)
	static void nonRecHeapPermutation(int[] A, int n) {
		int[] c = new int[n];
		printArray(A);
		int i = 0;
		while (i < n) {
			if (c[i] < i) {
				// if i is even
				if (i % 2 == 0) {
					// exchange A[0] and A[i]
					swap(A, 0, i);
				} else {
					// i is odd
					// exchange A[c[i]] and A[i]
					swap(A, c[i], i);
				}
				printArray(A);
				c[i] += 1;
				i = 0;
			} else {
				c[i] = 0;
				i += 1;
			}
		}
	}

	// util function to print an int array
	static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (i != arr.length - 1)
				System.out.print(arr[i] + ", ");
			else
				System.out.print(arr[i]);
		}
		System.out.println();
	}

	// swapping two elements in an int array
	static void swap(int[] A, int i, int j) {
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	public static void main(String[] args) {
		int[] A = { 5, 2, 1 };
		nonRecHeapPermutation(A, 3);
	}



}
