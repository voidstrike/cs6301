import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

public class SP2Problem1
{

	public static<T extends Comparable<? super T>> void intersect(List<T> l1, List<T> l2, List<T> outList) 
	{
		  // Return elements common to l1 and l2, in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	      // outList is an empty list created by the calling
          // program and passed as a parameter.

	   	  int l1Index = 0;
	   	  int l2Index = 0;							// l1Index and l2Index keep track of current positions in l1 and l2
	   	  int l1Size = l1.size();
	   	  int l2Size = l2.size();
	   	  int compareToValue;
	   	  T l1Item, l2Item;							// l1Item and l2Item will hold current elements in l1 and l2

	   	  while(l1Index < l1Size && l2Index < l2Size)			// algorithm is similar to merging algorithm from mergesort
	   	  {
	   	  	l1Item = l1.get(l1Index);
	   	  	l2Item = l2.get(l2Index);
	   	  	compareToValue = l1Item.compareTo(l2Item);			

	   	  	if(compareToValue == -1)	
	   	  	{
	   	  		l1Index++;
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
		   	  		l2Index++;
	   	  		 }
	   	  		 else						// if current items are equal then add item to intersection and increment both indices
	   	  		 {
	   	  		 	outList.add(l1Item);
		   	  		l1Index++;
		   	  		l2Index++;
	   	  		 }

	   	  }


	}

	public static<T extends Comparable<? super T>> void union(List<T> l1, List<T> l2, List<T> outList) 
	{
		   // Return the union of l1 and l2, in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	   	   // Output is a set, so it has no duplicates.

	  	  int l1Index = 0;
	   	  int l2Index = 0;							// l1Index and l2Index keep track of current positions in l1 and l2
	   	  int l1Size = l1.size();
	   	  int l2Size = l2.size();
	   	  int compareToValue;
	   	  T l1Item, l2Item;							// l1Item and l2Item will hold current elements in l1 and l2

	   	  while(l1Index < l1Size && l2Index < l2Size)			// algorithm is similar to merging algorithm from mergesort
	   	  {
	   	  	l1Item = l1.get(l1Index);
	   	  	l2Item = l2.get(l2Index);
	   	  	compareToValue = l1Item.compareTo(l2Item);			

	   	  	if(compareToValue == -1)		
	   	  	{
	   	  		outList.add(l1Item);
	   	  		l1Index++;
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
	   	  		 	outList.add(l2Item);
		   	  		l2Index++;
	   	  		 }
	   	  		 else						// if items are equal then add one of the items to the union and increment both indices
	   	  		 {
	   	  		 	outList.add(l1Item);
		   	  		l1Index++;
		   	  		l2Index++;
	   	  		 }

	   	  }

	   	  if(l1Index == l1Size && l2Index == l2Size)	 // if both lists have been completely finished then quit
	   	  {
	   	  	 return;
	   	  }
	   	  else											 
	   	  {

	   	  	 if(l1Index < l1Size)	 // if l1 is the list which hasn't been finished, add the remainder of l1 to the union
	   	  	 {
	   	  	 	while(l1Index < l1Size)
	   	  	 	{
	   	  	 		outList.add(l1.get(l1Index));
	   	  	 		l1Index++;
	   	  	 	}
	   	  	 }
	   	  	 else					 // otherwise l2 must be the one which hasn't finished so add remainder of l2 to the union
	   	  	 {
	   	  	 	while(l2Index < l2Size)
	   	  	 	{
	   	  	 		outList.add(l2.get(l2Index));
	   	  	 		l2Index++;
	   	  	 	}
	   	  	 }

	   	  }

	}

	public static<T extends Comparable<? super T>> void difference(List<T> l1, List<T> l2, List<T> outList) 
	{
	   // Return l1 - l2 (i.e, items in l1 that are not in l2), in sorted order. l1 and l2 are assumed to be already sorted and without duplicates
	   // Output is a set, so it should have no duplicates.

	   	  int l1Index = 0;
	   	  int l2Index = 0;							// l1Index and l2Index keep track of current positions in l1 and l2
	   	  int l1Size = l1.size();
	   	  int l2Size = l2.size();
	   	  int compareToValue;
	   	  T l1Item, l2Item;							// l1Item and l2Item will hold current elements in l1 and l2

	   	  while(l1Index < l1Size && l2Index < l2Size)			// algorithm is similar to merging algorithm from mergesort
	   	  {
	   	  	l1Item = l1.get(l1Index);
	   	  	l2Item = l2.get(l2Index);
	   	  	compareToValue = l1Item.compareTo(l2Item);			

	   	  	if(compareToValue == -1)		
	   	  	{
	   	  		outList.add(l1Item);
	   	  		l1Index++;
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
		   	  		l2Index++;
	   	  		 }
	   	  		 else						// if items are equal then simply increment both indices
	   	  		 {
		   	  		l1Index++;
		   	  		l2Index++;
	   	  		 }

	   	  }

	   	  if(l1Index < l1Size)              // add any remaining elements in l1 to the difference
	   	  {
	   	  	while(l1Index < l1Size)
   	  	 	{
   	  	 		outList.add(l1.get(l1Index));
   	  	 		l1Index++;
   	  	 	}
	   	  }

	}

	public static void main(String[] args)
	{
		LinkedList<Integer> l1 = new LinkedList<Integer>();
		LinkedList<Integer> l2 = new LinkedList<Integer>();
		LinkedList<Integer> outList = new LinkedList<Integer>();

		l1.add(new Integer(10));
		l1.add(new Integer(20));
		l1.add(new Integer(30));
		l1.add(new Integer(40));

		l2.add(new Integer(1));
		l2.add(new Integer(2));
		l2.add(new Integer(30));
		l2.add(new Integer(41));
		l2.add(new Integer(42));
		

		difference(l1, l2, outList);

		System.out.println(Arrays.toString(outList.toArray()));         

	}

}