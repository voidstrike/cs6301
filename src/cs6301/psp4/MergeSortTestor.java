package cs6301.psp4;

import cs6301.g00.Timer;
import static cs6301.g00.Shuffle.shuffle;

/**
 * Created by Alan Lin on 9/22/2017.
 */
public class MergeSortTestor {
    private static void merge(int[] arr, int[] tmp, int start, int middle, int end) //int version
    {
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;
        // firstPivot indicates the start position of first sub_array
        // secondPivot indicates the start position of second sub_array
        System.arraycopy(arr, start, tmp, start, arrayLength);

        for(int k=0; k<arrayLength; k++) {
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
    private static void mergeSortV1(int[] arr, int[] tmp, int start, int end) // int version
    {
        // Standard Merge Sort Algorithm
        int middle = (start + end) >>> 1;
        if(start < end)
        {
            mergeSortV1(arr, tmp, start, middle);
            mergeSortV1(arr, tmp, middle+1, end);
            mergeV1(arr, start, middle, end);
        }
    }

    private static void mergeSortV2(int[] arr, int[] tmp, int start, int end) // int version
    {
        // Standard Merge Sort Algorithm
        int middle = (start + end) >>> 1;
        if(start < end)
        {
            mergeSortV2(arr, tmp, start, middle);
            mergeSortV2(arr, tmp, middle+1, end);
            merge(arr, tmp, start, middle, end);
        }
    }

    private static void mergeSortV3(int[] arr, int[] tmp, int start, int end){
        int middle = (start + end) >>> 1;
        if (end - start < 100){
            insertSort(arr, start, end);
        }
        else{
            mergeSortV3(arr, tmp, start, middle);
            mergeSortV3(arr, tmp, middle+1, end);
            merge(arr, tmp, start, middle, end);   // this is mergeV2
        }
    }

    private static void mergeSortV4(int[] arr, int[] tmp, int start, int end){
        int middle = (start + end) >>> 1;
        if (end - start < 100){
            insertSort(arr, start, end);
        }
        else{
            mergeSortV4(tmp, arr, start, middle);
            mergeSortV4(tmp, arr, middle+1, end);
            mergeV4(arr, tmp, start, middle, end);
        }
    }

    private static void mergeV4(int[] arr, int[] tmp, int start, int middle, int end) //int version
    {
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;
        // firstPivot indicates the start position of first sub_array
        // secondPivot indicates the start position of second sub_array

        for(int k=0; k<arrayLength; k++) {
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

    public static void mergeSort(int[] arr, int dataSize, int mode){
        int[] tmp = new int[dataSize];
        if (mode == 1)
            mergeSortV1(arr, tmp, 0, arr.length-1);
        else if (mode == 2)
            mergeSortV2(arr, tmp, 0, arr.length-1);
        else if (mode == 3) {
            System.arraycopy(arr, 0, tmp, 0, tmp.length);
            mergeSortV3(arr, tmp, 0, arr.length - 1);
        }
        else if (mode == 4) {
            System.arraycopy(arr, 0, tmp, 0, tmp.length);
            mergeSortV4(arr, tmp, 0, arr.length - 1);
        }
        else
            System.out.println("Error Mode");
    }

    public static void printList(int[] arr){
        for (int e : arr)
            System.out.print(e + " ");
    }

    // Different versions of Merge
    private static void mergeV1(int[] arr, int start, int middle, int end) //int version
    {
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;
        // firstPivot indicates the start position of first sub_array
        // secondPivot indicates the start position of second sub_array
        int[] tmp = new int[arr.length];
        System.arraycopy(arr, start, tmp, start, arrayLength);

        for(int k=0; k<arrayLength; k++) {
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

    private static void insertSort(int[] arr, int start, int end){
        //Standard Insertion Sort Algorithm
        //int arrLength = arr.length;
        int currentElement;

        for(int currentPosition = start; currentPosition <= end; currentPosition++){
            currentElement = arr[currentPosition];
            int comparePosition = currentPosition - 1;
            while(comparePosition >= start && arr[comparePosition] > (currentElement))
            {
                arr[comparePosition+1] = arr[comparePosition];
                comparePosition--;
            }
            arr[comparePosition+1] = currentElement;
        }
    }

    public static void main(String[] args){
//        int dataSize = 1000000;
        int dataSize = 10000000;
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
//        System.out.println("After Shuffle");
//        printList(test);
//        System.out.println("-----------------");
        t.start();
        mergeSort(test, dataSize, 2);
//        insertSort(test, 0, test.length-1);
        t.end();
        System.out.print(t.toString());
        System.out.println();
//        printList(test);
    }
}
