package cs6301.g42;

/**
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;


public class SP7Problem7
{

    public static<T extends Comparable<? super T>> T[] exactlyOnce(T[] a)
    {
    	Hashtable<T, Integer> hashtable = new Hashtable<T, Integer>();
    	int j = 0;

    	for(T object: a)
    	{
    		if(!hashtable.containsKey(object))
    		{
    		   hashtable.put(object, j);
    		}
    		
    		j++;
    	}

    	Comparator<T> sortByIndex = new Comparator<T>()
    	{
		    public int compare(T o1, T o2) 
		    {
		       int index1 = hashtable.get(o1);
		       int index2 = hashtable.get(o2);

		       if(index1 < index2)
		       {
		       	 return -1;
		       }
		       else if(index1 > index2)
		       {
		       	 return 1;
		       }
		       else
		       {
		       	 return 0;
		       }
		       
		    }
		};

    	TreeMap<T, Integer> treeMap = new TreeMap<T, Integer>(sortByIndex);

		T currentObject;
		int numOccurences;

		for(int i = 0; i < a.length; i++)
		{
		   currentObject = a[i];	

		   if(treeMap.containsKey(currentObject))
		   {
		   	  numOccurences = treeMap.remove(currentObject);
		   	  treeMap.put(currentObject, numOccurences+1);
		   }
		   else
		   {
		   	  treeMap.put(currentObject, 1);
		   }

		}

		ArrayList<T> answer = new ArrayList<T>();
		Map.Entry<T, Integer> currentEntry;
		int currentNumOccur;

		while(!treeMap.isEmpty())
		{
			currentEntry = treeMap.pollFirstEntry();
			currentNumOccur = currentEntry.getValue();

			if(currentNumOccur == 1)
			{
				answer.add(currentEntry.getKey());
			}

		}

		@SuppressWarnings("unchecked")
		T[] solution = (T[])Array.newInstance(a.getClass().getComponentType(), answer.size());	
		return answer.toArray(solution);
    }

	public static void main(String[] args)
	{
		String[] a = {"a","ad","a","x"};
		Integer[] b = {1,2,1,6,2,1,2,3,4,50,6,72};
		System.out.println(Arrays.toString(exactlyOnce(b)));
	}

}
