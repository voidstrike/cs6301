package cs6301.sp5;

import java.util.Random;
import java.util.PriorityQueue;

/**
 * Created by Alan Lin on 9/26/2017.
 * @Auther Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class QuickSort {
    public static void quickSort(int[] arr, int mode){
        if (mode == 1) quickSortV1(arr, 0, arr.length-1);
        else if (mode == 2) quickSortV2(arr, 0, arr.length-1);
        else if (mode == 3) quickSortV3(arr, 0, arr.length-1);
    }

    // Lumuto Partition SP5 Q1
    private static void quickSortV1(int[] arr, int start, int end){
        int cut;
        if (end > start){
            cut = partitionV1(arr, start, end);
            quickSortV1(arr, start, cut-1);
            quickSortV1(arr, cut+1, end);
        }
    }
    private static int partitionV1(int[] arr, int start, int end){
        Random generator = new Random();
        int cutPoint = generator.nextInt(end-start+1) + start;
        int pivot;
        // Swap the randomly choose element to the end of array
        swap(arr, cutPoint, end);
        pivot = arr[end];
        cutPoint = start - 1;
        // Iterate through the array, find the right position for pivot
        for (int i = start; i<end; i++){
            if (arr[i] <= pivot){
                cutPoint++;
                swap(arr, i, cutPoint);
            }
        }
        swap(arr, cutPoint+1, end);
        return cutPoint+1;
    }

    // Hoare Partition SP5 Q1
    private static void quickSortV2(int[] arr, int start, int end){
        int cut;
        if (end > start){
            cut = partitionV2(arr, start, end);
            quickSortV2(arr, start, cut);
            quickSortV2(arr, cut+1, end);
        }
    }
    private static int partitionV2(int[] arr, int start, int end){
        Random generator = new Random();
        int cutPoint = generator.nextInt(end-start+1) + start;
        int pivot, fTracker = start-1, sTracker=end+1;
        // Swap the randomly choose element to the end of array
        pivot = arr[cutPoint];
        while(true){
            do{
                fTracker++;
            }while(arr[fTracker] < pivot);

            do{
                sTracker--;
            }while(arr[sTracker] > pivot);

            if (fTracker >= sTracker)
                break;
            swap(arr, fTracker, sTracker);
        }
        return sTracker;
    }

    // Dual Pivot Partition SP5 Q2
    public static void quickSortV3(int[] arr, int start, int end){
        int[] cut;
        if (end > start){
            cut = partitionV3(arr, start, end);
            quickSortV3(arr, start, cut[0]-1);
            quickSortV3(arr, cut[1]+1, end);
            if (arr[cut[1]] != arr[cut[0]])
                quickSortV3(arr, cut[0]+1, cut[1]-1);
        }
    }
    private static int[] partitionV3(int[] arr, int start, int end){
        int[] result = new int[2];
        // Generate new leftPivot and rightPivot
        Random generator = new Random();
        int tmp1 = generator.nextInt(end-start+1) + start;
        int tmp2 = generator.nextInt(end-start+1) + start;
        if (tmp1 == tmp2)
            // IF two random indices are same, update the second one
            tmp2 = tmp1+1 > end ? tmp1 - 1 : tmp1 + 1;
        swap(arr, start, tmp1);
        swap(arr, end, tmp2);
        if (arr[start] > arr[end]){
            swap(arr, start, end);
        }
        int lPivot = arr[start], rPivot = arr[end];
        int k=start+1, lBound=start+1, rBound=end-1;

        // Partition phase
        while(k <= rBound)
        {
            if (arr[k] < lPivot){
                // Current Element less than left pivot
                swap(arr, k, lBound);
                lBound++;
            }
            else{
                if (arr[k] > rPivot){
                    // Current Element large than right pivot
                    while (arr[rBound] > rPivot && k < rBound)
                        rBound--;
                    swap(arr, k, rBound--);
                    if (arr[k] < lPivot){
                        // Swapped Element less than left pivot
                        swap(arr, k, lBound);
                        lBound++;
                    }
                }
            }
            k++;
        }
        result[0] = --lBound;
        result[1] = ++rBound;
        swap(arr, start, lBound);
        swap(arr, rBound, end);
        return result;
    }

    // Aux method, swap two elements in a given array
    private static void swap(int[] arr, int ori, int tar){
        if (ori != tar){
            int tmp = arr[ori];
            arr[ori] = arr[tar];
            arr[tar] = tmp;
        }
    }

    // Select Algorithm SP5 Q3
    private static int[] select(int[] arr, int k, int mode){
        int[] result = new int[k];
        if (k >= arr.length){
            // Base case : Require more elements than arr have
            System.arraycopy(arr, 0, result, 0, result.length);
        }
        else{
            if (mode == 1){
                result = selectV1(arr, k);
            }
            else if (mode == 2){
                result = selectV2(arr, k);
            }
            else if (mode == 3){
                result = selectV3(arr, k);
            }
            else{
                result = null;
                System.out.println("Error Mode");
            }
        }
        return result;
    }
    // MaxHeap Version
    private static int[] selectV1(int[] arr, int k){
        int[] result = new int[k];
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((x, y) -> y - x);
        // Initialize the maxHeap
        for (int i = 0; i < arr.length; i++)
        {
            maxHeap.add(arr[i]);
        }

        // Poll the max element from the heap
        for (int i=0; i<result.length;i++)
            result[i] = maxHeap.poll();

        return result;
    }

    // MinHeap Version
    private static int[] selectV2(int[] arr, int k){
        int[] result = new int[k];
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // Initialize the maxHeap
        for (int i = 0; i < k; i++)
        {
            minHeap.add(arr[i]);
        }

        // Start iterate
        for (int i=k; i<arr.length; i++){
            if (arr[i] > minHeap.peek()){
                minHeap.remove();
                minHeap.add(arr[i]);
            }
        }

        for (int i=0; i<result.length;i++)
            result[i] = minHeap.poll();

        return result;
    }

    // log(n) Version
    private static int[] selectV3(int[] arr, int k){
        int[] result = new int[k];
        int pivot = arr.length-k;

        selectV3Aux(arr, 0, arr.length-1, k);

        for(int i=pivot; i<arr.length;i++){
            result[i-pivot] = arr[i];
        }
        return result;
    }
    private static void selectV3Aux(int[] arr, int start, int end, int k){
        int eNum = end - start + 1, tmp;
        if (eNum < 27)
            insertSort(arr, start, end);
        else{
            tmp = partitionV1(arr, start, end);
            if (end - tmp >= k){
                selectV3Aux(arr, tmp+1, end, k);
            }
            else if (end - tmp + 1 == k)
                ; // do nothing in this case
            else{
                selectV3Aux(arr, start, tmp-1, k - (end-tmp+1));
            }
        }
    }

    // Best Merge Sort SP5 Q4
    public static void mergeSort(int[] arr, int dataSize, int mode){
        int[] tmp = new int[dataSize];
        System.arraycopy(arr, 0, tmp, 0, tmp.length);
        mergeSort(arr, tmp, 0, arr.length - 1);
    }
    private static void mergeSort(int[] arr, int[] tmp, int start, int end){
        int middle = (start + end) >>> 1;
        if (end - start < 27){
            insertSort(arr, start, end);
        }
        else{
            mergeSort(tmp, arr, start, middle);
            mergeSort(tmp, arr, middle+1, end);
            merge(arr, tmp, start, middle, end);
        }
    }
    private static void merge(int[] arr, int[] tmp, int start, int middle, int end) {
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
        // Test code below
        int[] test1 = {12,3,443,46,7,71,33,4};
//        quickSort(test, 3);
        int[] test = select(test1, 3, 2);
        for (int e : test){
            System.out.print(e+" ");
        }
    }
}
