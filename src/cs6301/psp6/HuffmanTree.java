package cs6301.psp6;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by Alan Lin on 10/3/2017.
 */
public class HuffmanTree {
    static class treeNode{
        String value;
        double priority;
        treeNode lchild, rchild;

        treeNode(String val, double pr, treeNode left, treeNode right){
            value = val;
            priority = pr;
            lchild = left;
            rchild = right;
        }
    }

    public static Comparator<treeNode> treeNodeComparator = new Comparator<treeNode>() {
        @Override
        public int compare(treeNode o1, treeNode o2) {
            if(o1.priority >= o2.priority)
                return 1;
            else if (o1.priority == o2.priority)
                return 0;
            else
                return -1;
        }
    };
    public static treeNode buildTree(String[] elements, double[] pros){
        treeNode root=null;
        treeNode tmp1, tmp2;
        if (elements.length != pros.length)
            return null;
        // Initialize the Priority Queue
        PriorityQueue<treeNode> tmpPQ = new PriorityQueue<>(10, treeNodeComparator);
        for(int i = 0; i<elements.length; i++){
            tmpPQ.add(new treeNode(elements[i], pros[i], null,null));
        }
        // Build Huffman Tree
        while(tmpPQ.size() > 1){
            tmp1 = tmpPQ.poll();
            tmp2 = tmpPQ.poll();
            tmpPQ.add(new treeNode("", tmp1.priority+tmp2.priority, tmp1, tmp2));
        }
        root = tmpPQ.poll();
        return root;
    }
    public static void printNodes(treeNode root){
        String tmp = "";
        pAux(tmp, root);
    }
    private static void pAux(String pre, treeNode in){
        if(in.lchild == null && in.rchild == null)
            // Reach the leaf node, print the code
            System.out.println(in.value + " : " + pre);
        else{
            pAux(pre+"0", in.lchild);
            pAux(pre+"1", in.rchild);
        }
    }

    public static void main(String[] args){
        // Test code below
        String[] elements = {"a","b","c","d","e"};
        double[] pros = {0.1, 0.2, 0.3, 0.25, 0.15};
        treeNode HuffmanRoot = buildTree(elements, pros);
        printNodes(HuffmanRoot);
    }
}
