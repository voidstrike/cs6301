package cs6301.psp6;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Alan Lin on 10/3/2017.
 */
public class KMerge {
    static class holderNode{
        public List<Integer> currentList;
        holderNode(List<Integer> init){
            currentList = init;
        }
    }
    public static Comparator<holderNode> eleComparator = new Comparator<holderNode>() {
        @Override
        public int compare(holderNode o1, holderNode o2) {
            int a = o1.currentList.get(0), b = o2.currentList.get(0);
            if(a>b)
                return 1;
            else if (a == b)
                return 0;
            else
                return -1;
        }
    };

    public static ArrayList<Integer> KMerge(List<ArrayList<Integer>> kHolder){
        ArrayList<Integer> head = new ArrayList<>();
        holderNode tmp;
        PriorityQueue<holderNode> tmpPQ = new PriorityQueue<>(10, eleComparator);

        // Initialize the Priority Queue
        for(List<Integer> subList : kHolder){
            if (!subList.isEmpty())
                tmpPQ.add(new holderNode(subList));
        }
        // Perform the merge process
        while(!tmpPQ.isEmpty()){
            tmp = tmpPQ.poll();
            head.add(tmp.currentList.get(0));
            tmp.currentList.remove(0);
            if (!tmp.currentList.isEmpty())
                tmpPQ.add(tmp);
        }
        return head;
    }
    public static void main(String[] args){
        // Test code below
        ArrayList<ArrayList<Integer>> test = new ArrayList<>();
        ArrayList<Integer> a = new ArrayList<>();
        a.add(1); a.add(3); a.add(5);
        ArrayList<Integer> b = new ArrayList<>();
        b.add(2);b.add(7);b.add(33);
        ArrayList<Integer> c = new ArrayList<>();
        c.add(3);c.add(12);c.add(124);
        test.add(a);
        test.add(b);
        test.add(c);
        ArrayList<Integer> result = KMerge(test);
        for (Integer ele : result){
            System.out.println(ele);
        }

    }
}
