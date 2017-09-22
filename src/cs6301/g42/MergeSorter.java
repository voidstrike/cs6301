package cs6301.g42;
import cs6301.g00.Timer;

import static cs6301.g00.Shuffle.shuffle;

/**
 * Created by Alan Lin on 8/26/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class MergeSorter {

    private static <T extends Comparable<? super T>> void mergeSortPhase(T[] arr, T[] tmp, int start, int end)
    {
        // Standard Merge Sort Algorithm
        int middle = (start + end) >>> 1;
        if(start < end)
        {
            mergeSortPhase(arr, tmp, start, middle);
            mergeSortPhase(arr, tmp, middle+1, end);
            mergeSortMerger(arr, tmp, start, middle, end);
        }
    }

    private static void mergeSortPhase(int[] arr, int[] tmp, int start, int end) // overload : int version
    {
        // Standard Merge Sort Algorithm
        int middle = (start + end) >>> 1;
        if(start < end)
        {
            mergeSortPhase(arr, tmp, start, middle);
            mergeSortPhase(arr, tmp, middle+1, end);
            mergeSortMerger(arr, tmp, start, middle, end);
        }
    }

    private static <T extends  Comparable<? super T>> void mergeSortMerger(T[] arr, T[] tmp, int start, int middle, int end)
    {
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;
        // firstPivot indicates the start position of first sub_array
        // secondPivot indicates the start position of second sub_array
        System.arraycopy(arr, start, tmp, start, arrayLength);

        for(int k=0; k<arrayLength; k++)
        {
            if(firstPivot<=middle && secondPivot<=end) // Both sub_array one and two are in the legal range
            {
                if( tmp[firstPivot].compareTo(tmp[secondPivot]) == -1)
                {
                    arr[start+k] = tmp[firstPivot];
                    firstPivot++;
                }
                else
                {
                    arr[start+k] = tmp[secondPivot];
                    secondPivot++;
                }
            }
            else if(firstPivot>middle) // Run out of elements in first sub_array
            {
                arr[start+k] = tmp[secondPivot];
                secondPivot++;
            }
            else // Run out of elements in second sub_array
            {
                arr[start+k] = tmp[firstPivot];
                firstPivot++;
            }
        }
    }

    private static void mergeSortMerger(int[] arr, int[] tmp, int start, int middle, int end) // overload : int version
    {
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;
        // firstPivot indicates the start position of first sub_array
        // secondPivot indicates the start position of second sub_array
        System.arraycopy(arr, start, tmp, start, arrayLength);

        for(int k=0; k<arrayLength; k++)
        {
            if(firstPivot<=middle && secondPivot<=end) // Both sub_array one and two are in the legal range
            {
                if( tmp[firstPivot] < tmp[secondPivot])
                {
                    arr[start+k] = tmp[firstPivot];
                    firstPivot++;
                }
                else
                {
                    arr[start+k] = tmp[secondPivot];
                    secondPivot++;
                }
            }
            else if(firstPivot>middle) // Run out of elements in first sub_array
            {
                arr[start+k] = tmp[secondPivot];
                secondPivot++;
            }
            else // Run out of elements in second sub_array
            {
                arr[start+k] = tmp[firstPivot];
                firstPivot++;
            }
        }
    }

    public static <T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp)
    {
        mergeSortPhase(arr, tmp, 0, arr.length-1);
    }

    public static void mergeSort(int[] arr, int[] tmp)
    {
        mergeSortPhase(arr, tmp, 0, arr.length-1);
    }

    public static <T extends Comparable<? super T>> void nSquareSort(T[] arr)
    {
        //Standard Insertion Sort Algorithm
        int arrLength = arr.length;
        T currentElement;

        for(int currentPosition = 1; currentPosition < arrLength; currentPosition++){
            currentElement = arr[currentPosition];
            int comparePosition = currentPosition - 1;
            while(comparePosition >= 0 && arr[comparePosition].compareTo(currentElement) > 0)
            {
                arr[comparePosition+1] = arr[comparePosition];
                comparePosition--;
            }
            arr[comparePosition+1] = currentElement;
        }
    }

    public static void main(String[] args)
    {
        //Test code
        int dataSize = 3500000;
        Timer t = new Timer();
        //Integer[] test = new Integer[dataSize];
        int[] test = new int[dataSize];
        //Integer[] tmp = new Integer[dataSize];
        int[] tmp = new int[dataSize];
        for(int i=0;i<dataSize;i++)
        {
            //test[i] = new Integer(i);
            test[i] = i;
            //tmp[i] = new Integer(0);
            tmp[i] = 0;
        }
        shuffle(test); // shuffle the array to random order
        t.start();
        mergeSort(test, tmp);
        t.end();
        System.out.print(t.toString());
    }
}
