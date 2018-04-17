package cs6301.psp6;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import cs6301.g00.Timer;

/**
 * Created by Alan Lin on 10/3/2017.
 */
public class PrimMST extends GraphAlgorithm<PrimMST.PVertex> {

    static final int Infinity = Integer.MAX_VALUE;
    static class PVertex{
        boolean seen;
        int comingWight;
        Graph.Vertex parent;
        PVertex(){
            seen = false;
            parent = null;
            comingWight = 0;
        }
    }

    public static Comparator<Graph.Edge> edgeComparator = new Comparator<Graph.Edge>() {
        @Override
        public int compare(Graph.Edge o1, Graph.Edge o2) {
            if (o1.weight > o2.weight)
                return 1;
            else if (o1.weight == o2.weight)
                return 0;
            else
                return -1;
        }
    };

    PrimMST(Graph g){
        super(g);
        node = new PVertex[g.size()];
        for (Graph.Vertex u : g){
            node[u.getName()] = new PVertex();
        }
    }

    public int prim1(Graph.Vertex s) {
        int wmst = 0;
        // SP6.Q4: Prim's algorithm using PriorityQueue<Edge>:
        if(g.size() <= 0)
        {
            System.out.println("Error");
        }
        node[s.getName()].seen = true;

        PriorityQueue<Graph.Edge> tmpPQ = new PriorityQueue<>(100, edgeComparator);
        // Initialize the Priority Queue
        for(Graph.Edge e:s.adj){
            tmpPQ.add(e);
        }
        int seenVertex = 1;
        Graph.Edge tmpEdge;
        Graph.Vertex v1, v2;
        while (seenVertex < node.length && !tmpPQ.isEmpty()){
            tmpEdge = tmpPQ.poll();
            v1 = tmpEdge.from;
            v2 = tmpEdge.to;
            if (node[v1.getName()].seen && !node[v2.getName()].seen){
                wmst += tmpEdge.weight;
                node[v2.getName()].seen = true;
                node[v2.getName()].comingWight = tmpEdge.weight;
                for (Graph.Edge e:v2){
                    if(!node[e.otherEnd(v2).getName()].seen)
                        tmpPQ.add(e);
                }
                node[v2.getName()].parent = v1;
                seenVertex += 1;
            }
            else if (!node[v1.getName()].seen && node[v2.getName()].seen){
                wmst += tmpEdge.weight;
                node[v1.getName()].comingWight = tmpEdge.weight;
                node[v1.getName()].seen = true;
                for (Graph.Edge e:v1){
                    if(!node[e.otherEnd(v1).getName()].seen)
                        tmpPQ.add(e);
                }
                node[v1.getName()].parent = v2;
                seenVertex += 1;
            }
        }

        return wmst;
    }

    public int prim2(Graph.Vertex s) {
        int wmst = 0;
        return wmst;
    }

    public void printMST(){
        PVertex tmp;
        int totalWeight = 0;
        for (Graph.Vertex u : g){
            tmp = node[u.getName()];
            if (tmp.parent != null){
                totalWeight += tmp.comingWight;
                System.out.println(tmp.parent.toString() + "-->" +
                u.toString() + " : " + tmp.comingWight);
            }
        }
        System.out.println("The total weight of this MST is " + totalWeight);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;

        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

        Graph g = Graph.readGraph(in);
        Graph.Vertex s = g.getVertex(1);

        Timer timer = new Timer();
        PrimMST mst = new PrimMST(g);
        int wmst = mst.prim1(s);
        timer.end();
        System.out.println(wmst);
    }
}
