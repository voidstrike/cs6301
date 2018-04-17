package cs6301.psp6;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan Lin on 10/3/2017.
 */
public class PerfectPower {
    static class powerNode{
        int base;
        int power;
        int val;
        powerNode(int b, int p){
            base = b;
            power = p;
            val = pPow(b, p);
        }
    }

    public static Comparator<powerNode> powerNodeComparator = new Comparator<powerNode>() {
        @Override
        public int compare(powerNode o1, powerNode o2) {
            if (o1.val > o2.val)
                return 1;
            else if (o1.val == o2.val)
                return 0;
            else
                return -1;
        }
    };

    private static int pPow(int a, int b){
        if(b == 0)
            return 1;
        else if (b==1)
            return a;
        else{
            int tmp = b>>>1;
            int holder = pPow(a, tmp);
            if (b % 2 == 0)
                return holder * holder;
            else
                return holder * holder * a;
        }
    }

    public static ArrayList<Integer> perfectPower(int limit){
        ArrayList<Integer> result = new ArrayList<>();
        powerNode tmp;
        int val, lastElement = -1;
        PriorityQueue<powerNode> tmpPQ = new PriorityQueue<>(100, powerNodeComparator);
        // Initialize the Priority Queue
//        tmpPQ.add(new powerNode(2, 2));
        int j = 2;
        while (j<=30){
            tmpPQ.add(new powerNode(2, j));
            j++;
        }
        while (true){
            tmp = tmpPQ.poll();
            val = pPow(tmp.base, tmp.power);
            if (val > limit)
                break;
            else{
                System.out.println(val);
                result.add(val);
//                if (tmp.base == 2) {
//                    tmpPQ.add(new powerNode(tmp.base, tmp.power+1));
//                    tmpPQ.add(new powerNode(tmp.base+1, tmp.power));
//                }
                if (tmp.base == 2)
                    tmpPQ.add(new powerNode(tmp.base+1, tmp.power));
            }
        }
        return result;
    }

    public static void printPrimes(List<Integer> elements, int limit){
        PriorityQueue<Integer> tmpPQ = new PriorityQueue<>();
        int tmp;
        int lastE = -1;
        for(Integer ele:elements)
            tmpPQ.add(ele);
        while(true){
            tmp = tmpPQ.poll();
            if (tmp > limit)
                break;
            if (tmp != lastE){
                System.out.println(tmp);
                lastE = tmp;
            }
            for(Integer ele:elements)
                tmpPQ.add(ele * tmp);
        }
    }

    public static void main(String[] args){
        // Test code below
        ArrayList<Integer> test = new ArrayList<>();
        int count = -1, n;
        // Initialize the prime number list
        if(args.length > 0){
            count = Integer.parseInt(args[0]);
            for(int i=0; i<count; i++){
                test.add(Integer.parseInt(args[1+i]));
            }
        }
        else{
            test.add(2);
            test.add(3);
            test.add(7);
        }
        // Initialize the value of N
        if (count == -1 || args.length < count+2){
            n = 100;
        }
        else
            n = Integer.parseInt(args[count+1]);

		if(args.length > 0)
			printPrimes(test, n);
        else
			perfectPower(100);
    }
}
