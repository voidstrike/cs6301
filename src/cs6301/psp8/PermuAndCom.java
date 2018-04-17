package cs6301.psp8;

import java.util.LinkedList;

/**
 * Created by Alan Lin on 10/27/2017.
 */
public class PermuAndCom {
    private int perNum, comNum;
    private LinkedList<int[]> permutationList, combinationList;

    PermuAndCom(){
        perNum = 0;
        comNum = 0;
        permutationList = new LinkedList<>();
        combinationList = new LinkedList<>();
    }

    /** Method to get the permutation of given array
     * @param arr
     *  The input integer array
     * @param  k
     *  The number of elements used for permutation -- use arr.length if you want all permutation
     * @param verb
     *  The flag parameter, 0 means the number of permutation, otherwise the permutation list */
    public static int permutation(int[] arr, int k, int verb){
        PermuAndCom helper = new PermuAndCom();
        helper.permutationAux(arr, k, verb);
        if (verb == 0)
            System.out.println("The number of permutation is: " + helper.perNum);
        else{
            for (int[] tar : helper.permutationList) {
                for (int e : tar)
                    System.out.print(e + " ");
                System.out.println();
            }
        }
        return helper.perNum;
    }

    // Inner private permutation function
    private void permutationAux(int[] arr, int per, int verb){
        // arr is the input integer list
        // per is the number of elements required -- k
        // verb is the flag parameter
        if (verb == 0)
            auxPermuCount(arr, per);
        else{
            auxPermuList(arr, per, per);
        }
    }
    // Inner auxiliary permutation method for count the number of permutation
    private void auxPermuCount(int[] arr, int last){
        if (last == 0)
            perNum++;
        else{
            int d = arr.length - last;
            auxPermuCount(arr, last - 1);
            for (int i= d+1; i<arr.length; i++){
                swap(arr, i, d);
                auxPermuCount(arr, last - 1);
                swap(arr, i, d);
            }
        }
    }
    // Inner auxiliary permutation method to visit each permutation
    private void auxPermuList(int[] arr, int last, int k){
        if (last == 0)
            permutationList.add(deepCopy(arr, k));
        else{
            int d = arr.length - last;
            auxPermuList(arr, last - 1, k);
            for (int i= d+1; i<arr.length; i++){
                swap(arr, i, d);
                auxPermuList(arr, last - 1, k);
                swap(arr, d, i);
            }
        }
    }

    /** Method to get the combination of given array
     * @param arr
     *  The input integer array
     * @param  k
     *  The number of elements used for combination -- use arr.length if you want all combination
     * @param verb
     *  The flag parameter, 0 means the number of permutation, otherwise the combination list */
    public static int combination(int[] arr, int k, int verb){
        PermuAndCom helper = new PermuAndCom();
        helper.combinationAux(arr, k, verb);
        if (verb == 0)
            System.out.println("The number of combination is: " + helper.comNum);
        else{
            for (int[] tar : helper.combinationList) {
                for (int e : tar)
                    System.out.print(e + " ");
                System.out.println();
            }
        }
        return helper.comNum;
    }

    private void combinationAux(int[] arr, int k, int verbose){
        if (verbose == 0)
            combinationCount(arr, 0, k, k);
        else
            combinationList(arr, 0, k, k);
    }

    private void combinationCount(int[] arr, int i, int c, int k){
        if (c == 0)
            comNum++;
        else{
            arr[k-c] = arr[i];
            combinationCount(arr, i+1, c-1, k);
            if (arr.length - i > c)
                combinationCount(arr, i+1, c, k);
        }
    }

    private void combinationList(int[] arr, int i, int c, int k){
        if (c == 0)
            combinationList.add(deepCopy(arr, k));
        else{
            arr[k-c] = arr[i];
            combinationList(arr, i+1, c-1, k);
            if (arr.length - i > c)
                combinationList(arr, i+1, c, k);
        }
    }

    public static void knuth(int[] arr){
        // Assume in ascending order and each element is less than 10 ?
        printList(arr);
        int j, k;
        while(!isDescend(arr)){
            j = arr.length - 2;
            while(arr[j] >= arr[j+1])
                j--;
            k =arr.length - 1;
            while (arr[k] < arr[j])
                k--;
            swap(arr, j, k);
            reverse(arr, j+1, arr.length-1);
            printList(arr);
        }

    }

    // Helper functions
    /** Method to swap two elements in target array */
    private static void swap(int[] arr, int s, int e){
        int tmp = arr[s];
        arr[s] = arr[e];
        arr[e] = tmp;
    }

    /** Method to generate new array of permutation */
    private static int[] deepCopy(int[] arr, int count){
        int[] res = new int[count];
        System.arraycopy(arr, 0, res, 0, count);
        return res;
    }

    private static void printList(int[] arr){
        for(int e : arr)
            System.out.print(e + " ");
        System.out.println();
    }

    private static boolean isDescend(int[] arr){
        for (int i=1; i<arr.length; i++){
            if (arr[i] > arr[i-1])
                return false;
        }
        return true;
    }

    private static void reverse(int[] arr, int s, int e){
        while(s < e){
            swap(arr, s, e);
            s++;
            e--;
        }
    }

    public static void main(String[] args){
        int[] test = {1, 2, 3};
        //permutation(test, 3, 1);
        //combination(test, 2, 1);
        knuth(test);
    }
}
