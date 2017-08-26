/**
 * Created by Alan Lin on 8/23/2017.
 */

import java.util.Iterator;
import java.util.List;

public class LinkListSort {
    private static <T extends Comparable<? super T>>
            boolean getSortedOrder(List<T> l1, List<T> l2)
    {
        int l1Length = l1.size(), l2Length = l2.size();
        T firstElement, lastElement = null;
        if(l1Length > 1){
            Iterator iter = l1.iterator();
            firstElement = (T) iter.next();
            while(iter.hasNext()){
                lastElement = (T) iter.next();
            }
        }
        else if(l2Length > 1){
            Iterator iter = l2.iterator();
            firstElement = (T) iter.next();
            while(iter.hasNext()){
                lastElement = (T) iter.next();
            }
        }
        else{
            return true; // the Size of both list are less than 2, take them as increment list
        }
        return firstElement.compareTo(lastElement) != 1 ? true : false;
    }

    public static <T extends Comparable<? super T>>
            void intersect(List<T> l1, List<T> l2, List<T> outList)
    {
        boolean sortOrder = getSortedOrder(l1, l2); // true indicates non-decreasing order, false otherwise

        //Intersect Phase
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();

        if(!iter1.hasNext() || !iter2.hasNext())
        {
            return; // output list would be empty once one of the list is empty
        }
        T compareElement = (T) iter2.next(), lastElement = null;

        for(;iter1.hasNext();){
            T currentElement = (T) iter1.next(); //come to next element each iteration
            if(currentElement.compareTo(compareElement) == 0){
                if(lastElement == null || lastElement != currentElement) {
                    outList.add(currentElement);
                    lastElement = currentElement;
                }
                if(iter2.hasNext())
                    compareElement = (T) iter2.next();
                else
                    break;
            }
            else if((sortOrder && currentElement.compareTo(compareElement) == 1) ||
                    (!sortOrder && currentElement.compareTo(compareElement) == -1))
            {
                if(iter2.hasNext())
                    compareElement = (T) iter2.next();
                else
                    break;
            }
            // compareElement will be updated only if it's equal to currentElement
            // or sortOrder is true and it's less than currentElement
            // or sortOrder is false and it's larger than currentElement
        }
    }

    public static<T  extends  Comparable<? super T>>
            void union(List<T> l1, List<T> l2, List<T> outList)
    {
        boolean sortOrder = getSortedOrder(l1, l2);

        // Union Phase
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();
        T candidateElement1 = null, candidateElement2 = null, lastElement = null;
        boolean pivot = true; // true means first time enter loop
        while(true)
        {
            if( !iter1.hasNext() || !iter2.hasNext() )
                break; // finish the loop while one list is empty

            if(pivot)
            {
                candidateElement1 = (T) iter1.next();
                candidateElement2 = (T) iter2.next();
                pivot = false;
            }
            // Perform the check phase
            if((candidateElement1.compareTo(candidateElement2) == -1 && sortOrder) ||
                    (candidateElement1.compareTo(candidateElement2) == 1 && !sortOrder))
            {
                if(lastElement == null || lastElement != candidateElement1)
                {
                    outList.add(candidateElement1);
                    lastElement = candidateElement1;
                }
                candidateElement1 = (T) iter1.next();
            }
            else
            {
                if(lastElement == null || lastElement != candidateElement1)
                {
                    outList.add(candidateElement2);
                    lastElement = candidateElement2;
                }
                candidateElement2 = (T) iter2.next();
            }
        }
        while(iter1.hasNext())
            outList.add((T) iter1.next());
        while(iter2.hasNext())
            outList.add((T) iter2.next());
    }

    public static<T extends  Comparable<? super T>>
            void difference(List<T> l1, List<T> l2, List<T> outList)
    {
        boolean sortOrder = getSortedOrder(l1, l2);

        // difference phase
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();
        T candidateElement1 = null, candidateElement2 = null, lastElement = null;
        boolean pivot = true; // true means first time enter loop
        while(true)
        {
            if( !iter1.hasNext() || !iter2.hasNext() )
                break; // finish the loop while one list is empty

            if(pivot)
            {
                candidateElement1 = (T) iter1.next();
                candidateElement2 = (T) iter2.next();
                pivot = false;
            }
            // Perform the check phase
            if((candidateElement1.compareTo(candidateElement2) == -1 && sortOrder) ||
                    (candidateElement1.compareTo(candidateElement2) == 1 && !sortOrder))
            {
                if(lastElement == null || lastElement != candidateElement1)
                {
                    outList.add(candidateElement1);
                    lastElement = candidateElement1;
                }
                candidateElement1 = (T) iter1.next();
            }
            else
            {
                if(lastElement == null || lastElement != candidateElement1)
                {
                    outList.add(candidateElement2);
                    lastElement = candidateElement2;
                }
                candidateElement2 = (T) iter2.next();
            }
        }
        while(iter1.hasNext())
            outList.add((T) iter1.next());
    }
}
