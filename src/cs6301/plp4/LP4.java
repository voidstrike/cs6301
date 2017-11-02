package cs6301.plp4;

import cs6301.plp4.Graph.Vertex;
import cs6301.plp4.Graph.Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Alan Lin on 11/1/2017.
 *
 */
public class LP4 extends BasicGraphAlgorithm{
    /** Nested extended BVertex for following algorithms */
    class LPVertex extends BasicGraphAlgorithm.BVertex{
        int weight, spCount;
        boolean visited;

        // Constructors
        LPVertex(Vertex u){
            super(u);
            this.weight = Integer.MAX_VALUE;
            this.spCount = 0;
            this.visited = false;
        }

        LPVertex(Vertex u, int weight){
            this(u);
            this.weight = weight;
        }

        void setWeight(int weight){
            this.weight = weight;
        }
    }
    //private Graph g; // Graph for this Program
    private Vertex src; // Source node for upcoming algorithm
    private int topoCount; // Global parameter used to count the topological order

    // Common constructor
    public LP4(LPGraph g, Vertex s){
        super(g);
        this.src = s;
        this.topoCount = 0;
        node = new LPVertex[g.size()];
        for(Vertex u : g)
            node[u.getName()] = new LPVertex(u);
    }

    // Part a. Return number of topological orders of g
    // Precondition: g must be DAG -- return -1 if the passing graph is not DAG
    public long countTopologicalOrders(){
        this.topoCount = 0;
        Vertex[] currentVertex = new Vertex[g.size()];
        for(Vertex u : g)
            currentVertex[u.getName()] = u;
        heapPermutation(currentVertex, g.size(), false);
        return topoCount;
    }

    // Part b. Print all topological orders of g, one per line
    // and return the number of topological orders of g
    public long enumerateTopologicalOrders(){
        this.topoCount = 0;
        Vertex[] currentVertex = new Vertex[g.size()];
        for(Vertex u : g)
            currentVertex[u.getName()] = u;
        heapPermutation(currentVertex, g.size(), true);
        return topoCount;
    }

    // Part c. Return the number of shortest paths from s to t
    // Return -1 if the graph has a negative or zero cycle
    public long countShortestPaths(Vertex tar){
        //TODO
        return 0L;
    }

    // Part d. Print all shortest paths from src to tar, one per line,
    // and return number of shortest paths from s to t.
    // Return -1 if the graph has a negative or zero cycle;
    public long enumerateShortestPaths(Vertex tar){
        //TODO
        return 0L;
    }

    // Part e. Return the weight of shortest path from src to tar using at most k edges
    public int constrainedShortestPath(Vertex tar, int k){
        //TODO
        return 0;
    }

    // Auxiliary methods
    /** Method to check whether a given vertices order is in topological order */
    private boolean isTopologicalOrder(Vertex[] tmp){
        resetTopo();
        Vertex other;
        for(Vertex u : tmp){
            ((LPVertex)getVertex(u)).visited = true; // Set this vertex to visited
            // Check outgoing edges
            for(Edge e : u){
                other = e.otherEnd(u);
                if (((LPVertex) getVertex(other)).visited)
                    return false;
            }
//            Iterator reverseIter = u.reverseIterator();
//            while(reverseIter.hasNext()){
//                Edge e = (Edge) reverseIter.next();
//                other = e.otherEnd(u);
//                if (!((LPVertex) getVertex(other)).visited)
//                    return false;
//            }
        }
        return true;
    }

    /** Auxiliary method of isTopologicalOrder, used to reset the parameter, visited, of every vertex */
    private void resetTopo(){
        LPVertex tmp;
        for(Vertex u : g){
            tmp = (LPVertex) getVertex(u);
            tmp.visited = false;
        }
    }

    /** Method to enumerate through the permutation of a given array */
    private void heapPermutation(Vertex[] arr, int remain, boolean printFlag){
        if (remain == 1) {
            if (isTopologicalOrder(arr)){
                topoCount++;
                if (printFlag){
                    // Print needed
                    for(Vertex u : arr)
                        System.out.print(u + " ");
                    System.out.println();
                }
            }
        }
        else{
            for(int i=0; i<remain-1; i++){
                heapPermutation(arr, remain-1, printFlag);
                if (remain % 2 == 0)
                    swap(arr, i, remain-1);
                else
                    swap(arr, 0, remain-1);
            }
            heapPermutation(arr, remain-1, printFlag);
        }
    }

    /** Auxiliary method to swap two element in a given Vertex array */
    private void swap(Vertex[] arr, int i, int j){
        Vertex tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner in;
        if (args.length > 0) {
            File inputFile = new File(args[0]);
            in = new Scanner(inputFile);
        } else {
            in = new Scanner(System.in);
        }

        //int start = in.nextInt();  // root node of the MST
        Graph g = Graph.readDirectedGraph(in);
        Vertex startVertex = g.getVertex(1);
        LPGraph enhancedG = new LPGraph(g);
        LP4 tester = new LP4(enhancedG, startVertex);
        long result = tester.countTopologicalOrders();
        result = tester.enumerateTopologicalOrders();
        System.out.println(result);
    }
}
