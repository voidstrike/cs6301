package cs6301.lp3;

import java.util.LinkedList;

/**
 * Created by Alan Lin on 10/16/2017.
 */
public class Testor {
    static class testNode{
        int val1;
        int val2;
        testNode(int v1, int v2){
            val1 = v1;
            val2 = v2;
        }

        @Override
        public String toString() {
            String tmp = val1 + " " + val2;
            return tmp;
        }
    }
    public static void main(String[] args) {
        LinkedList<testNode> test = new LinkedList<>();
        test.add(new testNode(1, 1));
        test.add(new testNode(2, 2));
        test.add(new testNode(3, 3));
        for (testNode t : test){
            t.val1 += 1;
        }
        for (testNode t : test){
            System.out.println(t.toString());
        }
    }

}
