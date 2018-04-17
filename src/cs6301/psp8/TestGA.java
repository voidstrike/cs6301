package cs6301.psp8;

/**
 * Created by Alan Lin on 10/24/2017.
 *
 */

import cs6301.psp8.Graph.Vertex;
import cs6301.psp8.Graph.Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

public class TestGA extends GraphAlgorithm<TestGA.PQVertex> {
    static class PQVertex implements Index{
        boolean seen;
        Vertex parent, thisV;
        int weight; // -1 means no value
        int index; // -1 means no value

        PQVertex(Vertex u, int weight, int in){
            seen = false;
            thisV = u;
            parent = null;
            this.weight = weight;
            this.index = in;
        }

        @Override
        public void putIndex(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }
    }

    private Comparator<PQVertex> privateCom = new Comparator<PQVertex>() {
        @Override
        public int compare(PQVertex o1, PQVertex o2) {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        }
    };

    TestGA(Graph g){
        super(g);
        node =  new PQVertex[g.size()];
        for(Vertex u : g)
            node[u.getName()] = new PQVertex(u, Integer.MAX_VALUE, -1);
    }

    public int testPrim(Vertex src) throws BinaryHeap.HeapFullException{
        PQVertex tmp;
        int wmst = 0;
        for (Vertex u : g){
            tmp = getVertex(u);
            tmp.seen = false;
            tmp.parent = null;
            tmp.weight = Integer.MAX_VALUE;
        }
        node[src.getName()].weight = 0;

        // Create Binary Heap
        PQVertex[] auxList = new PQVertex[g.size()];
        IndexedHeap<PQVertex> auxHeap = new IndexedHeap<>(auxList, privateCom);
        auxHeap.add(getVertex(src));

        while(!auxHeap.isEmpty()){
            tmp = auxHeap.remove();
            tmp.seen = true;
            wmst += tmp.weight;

            for(Edge e : tmp.thisV){
                Vertex v = e.otherEnd(tmp.thisV);
                if (!getVertex(v).seen){
                    // This vertex hasn't been visited
                    PQVertex otherPQ = getVertex(v);
                    if (otherPQ.index == -1)
                        auxHeap.add(getVertex(v)); // Observe this vertex first time

                    if (otherPQ.weight > e.weight){ // Observed this vertex, and update it's priority if applicable
                        otherPQ.weight = e.weight;
                        otherPQ.parent = tmp.thisV;
                        auxHeap.update(otherPQ.index);
                    }
                }
            }
        }
        return wmst;
    }

    public static void main(String[] args) throws FileNotFoundException{
        Scanner in;
        String fileName = "F:/TestGraph/newTest.txt";
        File inputFile = new File(fileName);
        in = new Scanner(inputFile);

        Graph g = Graph.readGraph(in);

        TestGA newHolder = new TestGA(g);

        int result = 0;
        try {
            result = newHolder.testPrim(g.getVertex(1));
            System.out.print(result);
        } catch (BinaryHeap.HeapFullException e){
            e.printStackTrace();
        }

    }
}
