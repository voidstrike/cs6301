//** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
package cs6301.g42;
import java.util.Arrays;


public class RearrangeMinusPlus
{

	public static void rearrangeMinusPlus(int[] arr)
	{	
		// Reorder an int array by moving negative elements to the front, followed by its positive elements

		rearrangeHelper(arr, 0, arr.length-1);
	}

	public static void rearrangeHelper(int[] arr, int p, int r)
	{
		if(p < r)		
		{
			int q = (int)Math.floor((p + r)/2.0);	  
			rearrangeHelper(arr, p, q);				// rearrange elements in the left half, rearrange elements in the right half,
			rearrangeHelper(arr, q+1, r);			// and finally merge the two rearranged halves
			merge(arr, p, q, r);
		}
	}

	public static void merge(int[] arr, int p, int q, int r)
	{
		int leftPositiveStart = p;
		int rightNegativeEnd = q + 1;

		while(leftPositiveStart <= q && arr[leftPositiveStart] < 0)		// find starting index of positive elements in left half
		{
			leftPositiveStart++;
		}

		while(rightNegativeEnd <= r && arr[rightNegativeEnd] < 0)		// find ending index of negative elements in right half
		{
			rightNegativeEnd++;
		}

		rightNegativeEnd--;		

		reverse(arr, leftPositiveStart, q);							    // reverse positive part of left half, reverse negative part of right half 
		reverse(arr, q+1, rightNegativeEnd);							// and finally reverse the combination of two reversed parts
		reverse(arr, leftPositiveStart, rightNegativeEnd);

	}

	public static void reverse(int[] arr, int p, int r)
	{
		while(p < r)				// repeatedly swap pairs of elements starting at the two most outer elements of the array
		{							// and working in towards the middle
			swap(arr, p, r);
			p++;
			r--;	
		}
	}

	public static void swap(int[] arr, int p, int r)
	{
		int tmp = arr[p];
		arr[p] = arr[r];
		arr[r] = tmp;
	}

	public static void main(String[] args)
	{

		int a[] = {-1, 4, -2, 3, -5, 8};

		rearrangeMinusPlus(a);
		System.out.println("Rearranged array: " + Arrays.toString(a));

	}


}
