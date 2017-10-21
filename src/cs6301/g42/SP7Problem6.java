package cs6301.g42;

/**
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

import java.util.Map;
import java.util.TreeMap;

public class SP7Problem6
{

	public static int howMany(int[] a, int x) 
	{ 
		TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
		int currentInt, numOccurences;

		for(int i = 0; i < a.length; i++)
		{
		   currentInt = a[i];	

		   if(treeMap.containsKey(currentInt))
		   {
		   	  numOccurences = treeMap.remove(currentInt);
		   	  treeMap.put(currentInt, numOccurences+1);
		   }
		   else
		   {
		   	  treeMap.put(currentInt, 1);
		   }

		}

		int numPairs = 0;
		int currentValue, currentSearchNum, currentNumPairs;
		Map.Entry<Integer, Integer> currentEntry;

		while(!treeMap.isEmpty())
		{
			currentEntry = treeMap.pollFirstEntry();
			currentValue = currentEntry.getKey();
			currentSearchNum = x - currentValue;

			if(currentSearchNum == currentValue)
			{
			  	numPairs += chooseTwo(currentEntry.getValue());
			}
			else
			{
				Integer searchResult = treeMap.get(currentSearchNum);

				if(searchResult != null)
				{
				   numPairs += currentEntry.getValue()*searchResult;	
				}
			}
			
		}

		return numPairs;
    }

    public static int chooseTwo(int n)
    {
    	return (n*(n-1))/2;
    }

	public static void main(String[] args)
	{
		int[] a = {1,7,4,5,6,7,5,3,8,91};
		System.out.println(howMany(a, 12));
	}

}
