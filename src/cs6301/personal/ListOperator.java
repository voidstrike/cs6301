package cs6301.personal;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
/**
 * Created by Alan Lin on 9/4/2017.
 * Personal Code for SP2 question 1
 * Implemented the Intersect, Union and Difference methods
 */
public class ListOperator {
    public static <T extends Comparable<? super T>>
        void intersect(List<T> l1, List<T> l2, List<T> outList){
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();

        if (!iter1.hasNext() || !iter2.hasNext())
            //At least one input list is empty, return empty outList
            return;

        T currentElement = (T) iter1.next();
        T compareElement = (T) iter2.next();
        T lastElement = null;

        while(true){
            if (currentElement.compareTo(compareElement) == 0){
                // Two elements are equal
                if (lastElement == null || currentElement != lastElement){
                    // Add currentElement to outList if never insert before
                    outList.add(currentElement);
                    lastElement = currentElement;
                }
                if (iter1.hasNext() && iter2.hasNext()){
                    // Update current&compare Elements if available
                    currentElement = (T) iter1.next();
                    compareElement = (T) iter2.next();
                }
                else break; // Exit the loop if one list is empty
            }
            else if (currentElement.compareTo(compareElement) == 1){
                // Element in l1 is larger than element in l2
                // l2 should update if available
                if (iter2.hasNext())
                    compareElement = (T) iter2.next();
                else break; // Exit the loop due to l2 is empty now
            }
            else{
                // Element in l1 is smaller than element in l2
                // l1 should update if available
                if(iter1.hasNext())
                    currentElement = (T) iter1.next();
                else break; // Exit the loop due to l1 is empty now
            }
        }
    }

    public static <T extends Comparable<? super T>>
        void union(List<T> l1, List<T> l2, List<T> outList){
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();
        boolean firstEnter = true;
        T lastElement = null, candidate1 = null, candidate2 = null;

        while(true){
            if (firstEnter){
                if (!iter1.hasNext() || !iter2.hasNext())
                    break; // At least one list is empty
                // Initialize candidates while first enter the loop
                firstEnter = false;
                candidate1 = (T) iter1.next();
                candidate2 = (T) iter2.next();
            }

            // Compare phase
            if (candidate1.compareTo(candidate2) == 0){
                if ( lastElement == null || candidate1 != lastElement){
                    outList.add(candidate1);
                    lastElement = candidate1;
                }
                // Update phase
                if (iter1.hasNext() && iter2.hasNext()){
                    candidate1 = (T) iter1.next();
                    candidate2 = (T) iter2.next();
                }
                else break;
            }
            else if (candidate1.compareTo(candidate2) == -1){
                // Element in l1 is smaller than element in l2
                // Add candidate1 into outList and update it if available
                if (lastElement == null || candidate1 != lastElement){
                    outList.add(candidate1);
                    lastElement = candidate1;
                }
                if (iter1.hasNext())
                    candidate1 = (T) iter1.next();
                else break;
            }
            else {
                // Element in l1 is larger than element in l2
                // Add candidate2 into outList and update it if available
                if (lastElement == null || candidate2 != lastElement) {
                    outList.add(candidate2);
                    lastElement = candidate2;
                }
                if (iter2.hasNext())
                    candidate2 = (T) iter2.next();
                else break;
            }
        }
        // if one of the iterator still has next
        while (iter1.hasNext()){
            candidate1 = (T) iter1.next();
            if (lastElement == null || candidate1 != lastElement){
                outList.add(candidate1);
                lastElement = candidate1;
            }
        }
        while (iter2.hasNext()){
            candidate2 = (T) iter2.next();
            if (lastElement == null || candidate2 != lastElement){
                outList.add(candidate2);
                lastElement = candidate2;
            }
        }
    }

    public static <T extends Comparable<? super T>>
        void difference(List<T> l1, List<T> l2, List<T> outList){
        Iterator iter1 = l1.iterator();
        Iterator iter2 = l2.iterator();
        T lastElement = null, currentElement = null, compareElement = null;

        if (!iter1.hasNext())
            return; // l1 is empty list, return empty list
        if (!iter2.hasNext()){
            while(iter1.hasNext()){
                currentElement = (T) iter1.next();
                if (lastElement == null || currentElement != lastElement){
                    outList.add(currentElement);
                    lastElement = currentElement;
                }
            }
            return; // l2 is empty list, outList equals l1
        }

        currentElement = (T) iter1.next();
        compareElement = (T) iter2.next();

        while(true) {
            if (currentElement.compareTo(compareElement) == 0) {
                // two elements are equal, just update the iterator if possible
                if (iter1.hasNext() && iter2.hasNext()) {
                    currentElement = (T) iter1.next();
                    compareElement = (T) iter2.next();
                } else break;
            } else if (currentElement.compareTo(compareElement) == -1) {
                // Element in l1 smaller than element in l2
                // add this element to outList and update l1 if possible
                if (lastElement == null || currentElement != lastElement) {
                    outList.add(currentElement);
                    lastElement = currentElement;
                }
                if (iter1.hasNext())
                    currentElement = (T) iter1.next();
                else break;
            } else {
                // Element in l1 larger than element in l2
                // update l2 if possible
                if (iter2.hasNext())
                    compareElement = (T) iter2.next();
                else break;
            }
        }
        // add elements if l1 still have elements
        while(iter1.hasNext()){
            currentElement = (T) iter1.next();
            if (lastElement == null || currentElement != lastElement){
                outList.add(currentElement);
                lastElement = currentElement;
            }
        }
    }

    public static void main(String[] args){
        // Test code below
        List<Integer> test1 = new ArrayList<>();
        List<Integer> test2 = new ArrayList<>();
        List<Integer> result = new ArrayList<>();
        test1.add(new Integer(1));
        test1.add(new Integer(3));
        test1.add(new Integer(5));
        test1.add(new Integer(7));
        test2.add(new Integer(2));
        test2.add(new Integer(3));
        test2.add(new Integer(6));
        test2.add(new Integer(7));
        difference(test1, test2, result);
        System.out.print(result);

    }

}
