package cs6301.g42;

/**
 * Created by Alan Lin on 8/26/2017.
 */
public class MergeSorter {

    public static <T extends Comparable<? super T>> void mergeSortPhase(T[] arr, T[] tmp, int start, int end)
    {
        int middle = (start + end) >>> 1;
        if(start < end)
        {
            mergeSortPhase(arr, tmp, start, middle);
            mergeSortPhase(arr, tmp, middle+1, end);
            mergeSortMerger(arr, tmp,start, middle, end);
        }
    }

    public static <T extends  Comparable<? super T>> void mergeSortMerger(T[] arr, T[] tmp, int start, int middle, int end)
    {
        //TODO
        int arrayLength = end - start + 1;
        int firstPivot = start, secondPivot = middle + 1;

        for(int i=0; i<arrayLength; i++)
            tmp[i+start] = arr[i+start];
        for(int k=0; k<arrayLength; k++)
        {
            if(tmp[firstPivot].compareTo(tmp[secondPivot]) == -1 && firstPivot <= middle)
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
    }

    public static <T extends Comparable<? super T>> void mergeSort(T[] arr, T[] tmp)
    {
        //TODO
        mergeSortPhase(arr, tmp, 0, arr.length-1);
    }

    public static void mergeSort(int[] arr, int[] tmp)
    {
        //TODO
    }

    public static <T extends Comparable<? super T>> void nSquareSort(T[] arr)
    {
        //TODO
    }

    public static void main(String[] args)
    {
        MergeSorter sorter = new MergeSorter();
        Integer[] test = {1,2,4,3,5};
        Integer[] tmp = {0,0,0,0,0};
        mergeSort(test, tmp);
        for(int i=0; i<test.length;i++)
            System.out.println(test[i]);
    }
}
