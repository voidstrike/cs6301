package cs6301.psp6;

import cs6301.psp8.Index;

import java.util.Comparator;

/**
 * Created by Alan Lin on 10/4/2017.
 */
public class PrimVertex implements Comparator<PrimVertex>, Index {
    int index, val;
    Graph.Vertex current;

    PrimVertex(Graph.Vertex ver, int value, int in){
        current = ver;
        val = value;
        index = in;
    }

    public int compare(PrimVertex o1, PrimVertex o2){
        if(o1.val > o2.val)
            return 1;
        else if (o1.val == o2.val)
            return 0;
        else
            return -1;
    }
    public void putIndex(int i){index = i;}
    public int getIndex() {return index;}
}
