
package cs6301.psp7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class TestBST {
    static String msg = "";

    static boolean validate(BST<Integer> t) {
        // Write code to validate a BST
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException{
        BST<Integer> t = new SplayTree<>();
        System.setIn(new FileInputStream("C:/Users/Alan Lin/Downloads/test-sp7/test-sp7/sp7-t3"));
        Scanner in = new Scanner(System.in);
        cs6301.g00.Timer timer = new cs6301.g00.Timer();
        boolean flag = true;
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 10) {
                t.add(x);
            } else if(x < -10) {
                t.remove(-x);
            } else if(x != 0) {
                flag = flag && validate(t);
            } else {
                Object[] arr = t.toArray();
                long sum = arr.length;
                for(int i=0; i<arr.length; i++) {
                    sum += (int) arr[i];
                }
                System.out.println(sum + "\n" + flag);
                if(!flag) { System.out.println("Error: " + msg); }
                System.out.println(timer.end());
                return;
            }
        }
    }
}
