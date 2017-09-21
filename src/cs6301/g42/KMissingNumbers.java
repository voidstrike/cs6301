import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class KMissingNumbers
{

	public static void getKMissing(int[] input, int p, int r, List<Integer> missingNumbers)
	{
		// input array is assumed to be an array of n distinct integers, in sorted order, starting at 1
		// this method returns the k missing numbers in the sequence

		int n = r - p + 1;

		if(n <= 1)			
		{
			return;
		}
		else if(n == 2)		// if there are only two elements, add all the missing numbers between them to missingNumbers
		{
			int numMissing = input[r] - input[p] - 1;

			if(numMissing == 0)
			{
				return;
			}
			else
			{
				int firstNumber = input[p] + 1;

				for(int i = 0; i < numMissing; i++)
				{
					missingNumbers.add(new Integer(firstNumber + i));
				}

				return;
			}
			 
		}
		else if(n > 2)			// if there are more than two elements, recursively find the missing numbers from the halves which have missing numbers
		{
			int middleIndex = (p+r)/2;
			int numMissingFromLeft, numMissingFromRight;

			numMissingFromLeft = input[middleIndex] - input[p] - 1 - (middleIndex - p - 1);
			numMissingFromRight = input[r] - input[middleIndex] - 1 - (r - middleIndex - 1);

			if(numMissingFromLeft == 0 && numMissingFromRight == 0)
			{
				return;
			}
			else if(numMissingFromLeft == 0 && numMissingFromRight != 0)
			{
				getKMissing(input, middleIndex, r, missingNumbers);
			}
			else if(numMissingFromLeft != 0 && numMissingFromRight == 0)
			{
				getKMissing(input, p, middleIndex, missingNumbers);		
			}
			else if(numMissingFromLeft != 0 && numMissingFromRight != 0)
			{
				getKMissing(input, p, middleIndex, missingNumbers);
				getKMissing(input, middleIndex, r, missingNumbers);
			}

		}

	}

	public static void main(String[] args)
	{
		Random r  = new Random();			
		int a[] = new int[5];
		a[0] = 1;

		for(int i=1; i < a.length; i++)			// generate input array of length 5 with random gaps between consecutive elements in range 1 to 5
		{
			a[i] = a[i-1] + r.nextInt(5) + 1;
		}

		List<Integer> missingNumbers = new ArrayList<Integer>();
		getKMissing(a, 0, a.length-1, missingNumbers);
		System.out.println("Input array: " + Arrays.toString(a));
		System.out.println("Missing numbers: " + Arrays.toString(missingNumbers.toArray()));

	}


}