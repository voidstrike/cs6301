import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

public class SP2Problem1
{

	public static<T extends Comparable<? super T>> void intersect(List<T> l1, List<T> l2, List<T> outList) 
	{

		// Return the intersection of l1 and l2, in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	   	// Output is a set, so it has no duplicates.

		if(l1 == null || l2 == null)
		{
			return;
		}

		Iterator<T> iterator1 = l1.iterator();			// get and store iterators for both lists
		Iterator<T> iterator2 = l2.iterator();
		T l1Item = next(iterator1);						// get and store initial items from both lists 
		T l2Item = next(iterator2);
		int compareToValue;

		while(l1Item != null && l2Item != null) 		// run loop until at least one of the lists has been fully iterated
		{
			compareToValue = l1Item.compareTo(l2Item);

			if(compareToValue == -1)		 			// if one item is smaller, move to next item in the corresponding list
	   	  	{
	   	  		l1Item = next(iterator1);
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
		   	  		l2Item = next(iterator2);
	   	  		 }
	   	  		 else						
	   	  		 {										// if items are equal, add one of the items to outList and move to next item in both lists
	   	  		 	outList.add(l1Item);				
		   	  		l1Item = next(iterator1);
		   	  		l2Item = next(iterator2);
	   	  		 }

		}


	}

	public static<T extends Comparable<? super T>> void union(List<T> l1, List<T> l2, List<T> outList) 
	{
		// Return the union of l1 and l2, in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	   	// Output is a set, so it has no duplicates.

		if(l1 == null || l2 == null)
		{
			return;
		}

		Iterator<T> iterator1 = l1.iterator();			// get and store iterators for both lists
		Iterator<T> iterator2 = l2.iterator();
		T l1Item = next(iterator1);						// get and store initial items from both lists 
		T l2Item = next(iterator2);
		int compareToValue;

		while(l1Item != null && l2Item != null) 		// run loop until at least one of the lists has been fully iterated
		{
			compareToValue = l1Item.compareTo(l2Item);

			if(compareToValue == -1)		 			// if one item is smaller, add it to outList and move to next item in the corresponding list
	   	  	{
	   	  		outList.add(l1Item);
	   	  		l1Item = next(iterator1);
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
	   	  		 	outList.add(l2Item);
		   	  		l2Item = next(iterator2);
	   	  		 }
	   	  		 else						
	   	  		 {										// if items are equal, add one of the items to outList and move to next item in both lists
	   	  		 	outList.add(l1Item);				
		   	  		l1Item = next(iterator1);
		   	  		l2Item = next(iterator2);
	   	  		 }

		}

		if(l1Item == null && l2Item == null)			// if both lists have been completely iterated, quit
		{
			return;
		}
		else											// otherwise add remaining elements from either list to outList
		{
			if(l1Item != null)
			{
				while(l1Item != null)
				{
					outList.add(l1Item);
	   	  			l1Item = next(iterator1);
				}
			}
			else
			{
				while(l2Item != null)
				{
					outList.add(l2Item);
	   	  			l2Item = next(iterator2);
				}
			}
		}

	}

	public static<T extends Comparable<? super T>> void difference(List<T> l1, List<T> l2, List<T> outList) 
	{
	    // Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	    // Output is a set, so it should have no duplicates.

		if(l1 == null || l2 == null)
		{
			return;
		}

		Iterator<T> iterator1 = l1.iterator();			// get and store iterators for both lists
		Iterator<T> iterator2 = l2.iterator();
		T l1Item = next(iterator1);						// get and store initial items from both lists 
		T l2Item = next(iterator2);
		int compareToValue;

		while(l1Item != null && l2Item != null) 		// run loop until at least one of the lists has been fully iterated
		{
			compareToValue = l1Item.compareTo(l2Item);

			if(compareToValue == -1)		 			// if smaller item is from l1, add it to outlist and go to next element in l1
	   	  	{
	   	  		outList.add(l1Item);
	   	  		l1Item = next(iterator1);
	   	  	}
	   	  	else if(compareToValue == 1)				// if smaller item is from l2, go to next element in l2
	   	  		 {	
		   	  		l2Item = next(iterator2);
	   	  		 }
	   	  		 else						
	   	  		 {										// if items are equal, move to next item in both lists				
		   	  		l1Item = next(iterator1);
		   	  		l2Item = next(iterator2);
	   	  		 }

		}

	
		if(l1Item != null)								// add any remaining elements in l1 to outList
		{
			while(l1Item != null)
			{
				outList.add(l1Item);
   	  			l1Item = next(iterator1);
			}
		}

	}

	// helper method helps write cleaner code with iterator

	public static <T extends Comparable<? super T>> T next(Iterator<T> iterator)        
	{
		if(iterator.hasNext())
		{
			return iterator.next();
		}
		else
		{
			return null;
		}
	}

	public static void main(String[] args)
	{
		LinkedList<Integer> l1 = new LinkedList<Integer>();
		LinkedList<Integer> l2 = new LinkedList<Integer>();
		LinkedList<Integer> outList = new LinkedList<Integer>();

		l1.add(new Integer(10));
		l1.add(new Integer(26));
		l1.add(new Integer(33));
		l1.add(new Integer(41));

		l2.add(new Integer(10));
		l2.add(new Integer(26));
		l2.add(new Integer(30));
		l2.add(new Integer(40));
		l2.add(new Integer(42));
		
		difference(l1, l2, outList);

		System.out.println(Arrays.toString(outList.toArray()));         

	}

}