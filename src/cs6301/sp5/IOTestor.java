package cs6301.sp5;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Alan Lin on 9/28/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class IOTestor {

    // External MergeSort
    public static void wrappedEMergeSort(String inputPath, int requiredSize){
        try{
            externalMergeSort(inputPath, requiredSize);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static void externalMergeSort(String inputPath, int requiredSize)
            throws FileNotFoundException{
        File currentFile = new File(inputPath);
        String  outputPath = inputPath + "_sorted";
        int[] tmp = new int[requiredSize];
        int arrSize = readFile(new Scanner(currentFile), tmp);
        if (arrSize != -1){
            // If the file can fit into sized memory, sort it and write into file
            QuickSort.quickSortV3(tmp, 0, arrSize-1);
            try{writeFile(outputPath, tmp, arrSize);}
            catch (IOException e){e.printStackTrace();}
        }
        else{
            String sub1 = inputPath + "part1";
            String sub2 = inputPath + "part2";

            try{splitFile(new Scanner(currentFile), sub1, sub2);}
            catch (IOException e){e.printStackTrace();}
            // recursively merge sort
            innerMergeSort(sub1, tmp);
            innerMergeSort(sub2, tmp);

            try{
                scannerMerge(new Scanner(new File(sub1+"_sorted")),
                        new Scanner(new File(sub2+"_sorted")),
                        outputPath);}
            catch (IOException e) {e.printStackTrace();}

            deleteFile(sub1+"_sorted");
            deleteFile(sub2+"_sorted");
        }
    }
    private static void innerMergeSort(String inputPath, int[] tmp)
        throws FileNotFoundException{
        File currentFile = new File(inputPath);
        String  outputPath = inputPath + "_sorted";
        int arrSize = readFile(new Scanner(currentFile), tmp);
        if (arrSize != -1){
            // If the file can fit into sized memory, sort it and write into file
            QuickSort.quickSortV3(tmp, 0, arrSize-1);
            try{writeFile(outputPath, tmp, arrSize);}
            catch (IOException e){e.printStackTrace();}
         }
        else{
            String sub1 = inputPath + "part1";
            String sub2 = inputPath + "part2";

            try{splitFile(new Scanner(currentFile), sub1, sub2);}
            catch (IOException e){e.printStackTrace();}

            // recursively merge sort call
            innerMergeSort(sub1, tmp);
            innerMergeSort(sub2, tmp);

            try{scannerMerge(new Scanner(new File(sub1+"_sorted")),
                    new Scanner(new File(sub2+"_sorted")),
                    outputPath);}
            catch (IOException e) {e.printStackTrace();}

            deleteFile(sub1+"_sorted");
            deleteFile(sub2+"_sorted");
        }
        deleteFile(inputPath);
    }
    private static void scannerMerge(Scanner fin, Scanner sin, String outfile)
            throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(outfile));
        int fElement, sElement;
        boolean breakPos;
        if (!fin.hasNextInt()){
            while(sin.hasNextInt()) {
                sElement = sin.nextInt();
                writerHelper(out, sElement);
            }
        }
        else if (!sin.hasNextInt()){
            while(fin.hasNextInt()) {
                fElement = fin.nextInt();
                writerHelper(out, fElement);
            }
        }

        if (!fin.hasNextInt()){
            fin.close();
            sin.close();
            out.close();
            return;
        }

        fElement = fin.nextInt();
        sElement = sin.nextInt();

        // neither one of the input file is empty
        while(true){

            if (fElement <= sElement){
                writerHelper(out, fElement);
                // UPDATE fElement
                if (!fin.hasNextInt()) {
                    breakPos = true;
                    break;
                }
                else
                    fElement = fin.nextInt();
            }
            else{
                writerHelper(out, sElement);
                // UPDATE sElement
                if (!sin.hasNextInt()){
                    breakPos = false;
                    break;}
                else
                    sElement = sin.nextInt();
            }
        }

        // Handle rest of the file
        if (breakPos){
            writerHelper(out, sElement);
            while(sin.hasNextInt()){
                sElement = sin.nextInt();
                writerHelper(out, sElement);
            }
        }
        else{
            writerHelper(out, fElement);
            while(fin.hasNextInt()){
                fElement = fin.nextInt();
                writerHelper(out, fElement);
            }
        }

        // Close File Handler
        fin.close();
        sin.close();
        out.close();
    }

    // External QuickSort
    public static void wrappedEQuickSort(String inputPath, int requiredSize){
        try{
            externalQuickSort(inputPath, requiredSize);
        } catch (IOException e){e.printStackTrace();}
    }
    private static void externalQuickSort(String inputPath, int requiredSize)
        throws  FileNotFoundException{
        File currentFile = new File(inputPath);
        String  outputPath = inputPath + "_sorted";
        boolean executeSign=false;
        int[] tmp = new int[requiredSize];
        int arrSize = readFile(new Scanner(currentFile), tmp);
        if (arrSize != -1){
            // If the file can fit into sized memory, sort it and write into file
            QuickSort.quickSortV3(tmp, 0, arrSize-1);
            try{writeFile(outputPath, tmp, arrSize);}
            catch (IOException e){e.printStackTrace();}
        }
        else{
            String fpart = inputPath + "1";
            String spart = inputPath + "2";
            String tpart = inputPath + "3";
            try{
                executeSign = partitionFile(new Scanner(currentFile), fpart, spart, tpart, tmp);
            } catch (IOException e){e.printStackTrace();}

            innerQuickSort(fpart, tmp);
            innerQuickSort(tpart, tmp);
            if (executeSign){
                innerQuickSort(spart, tmp);
            }
            try{
                combinePartition(new Scanner(new File(fpart+"_sorted")),
                        new Scanner(new File(spart+"_sorted")),
                        new Scanner(new File(tpart+"_sorted")),
                        outputPath);
            } catch (IOException e){e.printStackTrace();}
            deleteFile(fpart+"_sorted");
            deleteFile(spart+"_sorted");
            deleteFile(tpart+"_sorted");
        }
    }
    private static void innerQuickSort(String inputPath, int[] tmp)
            throws  FileNotFoundException{
        File currentFile = new File(inputPath);
        String  outputPath = inputPath + "_sorted";
        boolean executeSign=false;
        int arrSize = readFile(new Scanner(currentFile), tmp);
        if (arrSize != -1){
            // If the file can fit into sized memory, sort it and write into file
            QuickSort.quickSortV3(tmp, 0, arrSize-1);
            try{writeFile(outputPath, tmp, arrSize);}
            catch (IOException e){e.printStackTrace();}
        }
        else{
            String fpart = inputPath + "1";
            String spart = inputPath + "2";
            String tpart = inputPath + "3";
            try{
                executeSign = partitionFile(new Scanner(currentFile), fpart, spart, tpart, tmp);
            } catch (IOException e){e.printStackTrace();}

            innerQuickSort(fpart, tmp);
            innerQuickSort(tpart, tmp);
            if (executeSign){
                innerQuickSort(spart, tmp);
            }
            try{
                combinePartition(new Scanner(new File(fpart+"_sorted")),
                        new Scanner(new File(spart+"_sorted")),
                        new Scanner(new File(tpart+"_sorted")),
                        outputPath);
            } catch (IOException e){e.printStackTrace();}
            deleteFile(fpart+"_sorted");
            deleteFile(spart+"_sorted");
            deleteFile(tpart+"_sorted");
        }
        deleteFile(inputPath);
    }
    private static void combinePartition(Scanner fpart, Scanner spart, Scanner tpart, String outputPath)
            throws IOException{
        BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
        while(fpart.hasNextInt()){
            writerHelper(out, fpart.nextInt());
        }
        while(spart.hasNextInt()){
            writerHelper(out, spart.nextInt());
        }
        while(tpart.hasNextInt()){
            writerHelper(out, tpart.nextInt());
        }
        fpart.close();
        spart.close();
        tpart.close();
        out.close();
    }

    // Aux file operations
    private static void writerHelper(BufferedWriter out, int element){
        try{
            out.write(element+"\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static int  readFile(Scanner in, int[] arr){
        // Try to read the file, file split is needed return size is -1
        int count=0, fileSize;
        while (in.hasNextInt() && count < arr.length){
            arr[count++] = in.nextInt();
        }
        if (in.hasNextInt()){
            in.close();
            fileSize = -1;
        }
        else{
            in.close();
            fileSize = count;
        }
        return fileSize;
    }
    private static void deleteFile(String filename){
        File tmp = new File(filename);
        tmp.delete();
    }
    private static void writeFile(String output, int[] arr, int fileSize)
            throws IOException {
        FileWriter tmp = new FileWriter(output);
        BufferedWriter writer = new BufferedWriter(tmp);
        for (int i=0; i<fileSize; i++){
            writerHelper(writer, arr[i]);
        }
        writer.flush();
        writer.close();
        tmp.close();
    }
    // Split file into two part -- mergeSort
    private static void splitFile(Scanner in, String out1, String out2)
        throws IOException{
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(out1));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(out2));
        while (in.hasNextInt()){
            writerHelper(writer1, in.nextInt());
            if (in.hasNextInt())
                writerHelper(writer2, in.nextInt());
        }
        in.close();
        writer1.flush();
        writer2.flush();
        writer1.close();
        writer2.close();
    }
    // Part file by partition algorithm -- quickSort
    private static boolean partitionFile(Scanner in, String fpart, String spart, String tpart, int[] tmp)
        throws IOException{
        int count=0, curNum, pivot;
        int firstpart=0, secondpart=0, arrBound = tmp.length-1;
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(fpart));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(tpart));
        BufferedWriter writer3 = new BufferedWriter(new FileWriter(spart));
        while (in.hasNextInt() && count < tmp.length){
            tmp[count++] = in.nextInt();
        }
        QuickSort.quickSortV3(tmp, 0, arrBound);
        while(in.hasNextInt()){
            curNum = in.nextInt();
            if (curNum < tmp[0]){
                writerHelper(writer1, curNum);
                firstpart++;
            }
            else{
                if (curNum > tmp[arrBound]){
                    writerHelper(writer2, curNum);
                    secondpart++;
                }
                else{
                    // curNum is between two selected pivot
                    if (firstpart > secondpart){
                        // Write into secondPart;
                        writerHelper(writer2, tmp[arrBound]);
                        secondpart++;
                        // Update the tmp array
                        if (arrBound != 0)
                        {
                            for (pivot=arrBound-1; pivot>=0; pivot--){
                                if (tmp[pivot] > curNum){
                                    tmp[pivot+1] = tmp[pivot];
                                }
                                else{
                                    break;
                                }
                            }
                            tmp[pivot+1] = curNum;
                        }
                    }
                    else{
                        // Write into firstPart
                        writerHelper(writer1, tmp[0]);
                        firstpart++;
                        // Update the tmp array
                        if (arrBound != 0)
                        {
                            for (pivot=1; pivot<=arrBound; pivot++){
                                if (tmp[pivot] < curNum){
                                    tmp[pivot-1] = tmp[pivot];
                                }
                                else{
                                    break;
                                }
                            }
                            tmp[pivot-1] = curNum;
                        }
                    }
                }
            }
        }
        // Write middle result into file
        for (int i=0; i<=arrBound; i++){
            writerHelper(writer3, tmp[i]);
        }
        in.close();
        writer1.flush();
        writer2.flush();
        writer3.flush();
        writer1.close();
        writer2.close();
        writer3.close();
        if (tmp[0] == tmp[arrBound]){
            return false;
        }
        else{
            return true;
        }
    }



    public static void main(String[] args){
//        File test = new File("G:/test");
        String testFile = "G:/test";
        if (args.length > 0)
            testFile = args[0];
        else
        {
            System.out.println("No File Selected!");
            return;
        }

        int operateModel = 0;
        if (args.length > 1)
            operateModel = Integer.parseInt(args[1]);

        int requireSize = 27;
        if (args.length > 2)
            requireSize = Integer.parseInt(args[2]);

        if (operateModel == 0)
            wrappedEMergeSort(testFile, requireSize);
        else
            wrappedEQuickSort(testFile, requireSize);

    }
}
