package cs6301.plp4;

/**
 * Created by Alan Lin on 10/22/2017.
 *
 */

import cs6301.plp4.Graph.Edge;
import cs6301.plp4.Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ShortestPath extends BasicGraphAlgorithm{
    /** Nested class for shortest path problem, extended class of BVertex of BGAlgorithm */
    static class SPEntry extends BVertex implements Index {
        int weight, index, count;
        Vertex thisVertex, parent;

        // Constructors
        SPEntry(Vertex u){
            super(u);
            thisVertex = u;
            parent = null;
            count = 0;
        }

        SPEntry(Vertex u, int weight){
            this(u);
            this.weight = weight;
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

    private Vertex src;
    private Comparator<SPEntry> privateCom = new Comparator<SPEntry>() {
        @Override
        public int compare(SPEntry o1, SPEntry o2) {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        }
    };

    /** Constructor */
    ShortestPath(Graph g, Vertex x){
        super(g);
        src = x;
        node = new SPEntry[g.size()];
        for(Vertex u : g)
            node[u.getName()] = new SPEntry(u);
    }

    @Override
    protected void reInit() {
        super.reInit();
        for (Vertex u : g){
            SPEntry tmp = (SPEntry) getVertex(u);
            tmp.parent = null;
            tmp.index = -1;
            tmp.count = 0;
            tmp.weight = Integer.MAX_VALUE;
        }
    }

    /** Method for SP8 Q1, standard bfs
     * Pre-condition of this algorithm is the weight of edges are same */
    public void bfs(){
        bfs(src);
    }
    public int bfs(Vertex tar){
        reInit(); // Reset the Graph before execute the algorithm
        ArrayDeque<Vertex> visitList = new ArrayDeque<>();
        Vertex tmp, other;

        visitList.add(src);
        getVertex(src).seen = true;
        ((SPEntry) getVertex(src)).weight = 0;

        while(!visitList.isEmpty()){
            tmp = visitList.remove();
            for (Edge e : tmp){
                other = e.otherEnd(tmp);
                SPEntry otherSP = (SPEntry)getVertex(other);
                if (!otherSP.seen) {
                    visitList.add(other);
                    otherSP.weight = ((SPEntry)getVertex(tmp)).weight + e.weight;
                    otherSP.seen = true;
                }
            }
        }
        return ((SPEntry)getVertex(tar)).weight;
    }

    /** Method for SP8 Q2, DAG shortest path
     *  Pre-condition of this algorithm is the input is a DAG */
    public void dagShortestPaths(){
        dagSP(src);
        for(BVertex u : node){
            System.out.println(((SPEntry) u).thisVertex + " " + ((SPEntry) u).weight);
        }
    }
    public int dagSP(Vertex tar){
        reInit(); // Reset the Graph before execute the algorithm
        List<Vertex> topo = dfs(src);  // Get topology order of the graph
        SPEntry srcSP = (SPEntry) getVertex(src);
        srcSP.weight = 0;

        for(Vertex u : topo){
            SPEntry start = (SPEntry) getVertex(u);
            for (Edge e : u){
                SPEntry end = (SPEntry) getVertex(e.otherEnd(u));
                if (e.weight + start.weight < end.weight)
                    end.weight = e.weight + start.weight;
            }
        }

        // return the weight of shortest from src to tar
        return ((SPEntry)getVertex(tar)).weight;
    }

    /** Method for SP8 Q3, Dijkstra Algorithm */
    public void dijkstra(){
        try{
            dijkstra(src);
        } catch (BinaryHeap.HeapFullException e){
            e.printStackTrace();
        }
        for(BVertex u : node){
            System.out.println(((SPEntry) u).thisVertex + " " + ((SPEntry) u).weight);
        }
    }
    public int dijkstra(Vertex tar) throws BinaryHeap.HeapFullException{
        reInit(); // Reset the Graph before execute the algorithm
        SPEntry[] auxList = new SPEntry[g.size()];
        IndexedHeap<SPEntry> auxHeap = new IndexedHeap<>(auxList, privateCom);

        ((SPEntry) getVertex(src)).weight = 0; // Initialize the src node
        auxHeap.add((SPEntry) getVertex(src));

        SPEntry tmp;
        while(!auxHeap.isEmpty()){
            tmp = auxHeap.remove();
            tmp.seen = true;

            for(Edge e : tmp.thisVertex){
                Vertex v = e.otherEnd(tmp.thisVertex);
                if (!getVertex(v).seen){
                    // This vertex hasn't been visited
                    SPEntry otherSP = (SPEntry) getVertex(v);
                    if (otherSP.index == -1)
                        auxHeap.add((SPEntry) getVertex(v)); // Observe this vertex first time

                    if (otherSP.weight > e.weight + tmp.weight){ // Observed this vertex, and update it's priority if applicable
                        otherSP.weight = e.weight + tmp.weight;
                        otherSP.parent = tmp.thisVertex;
                        auxHeap.update(otherSP.index);
                    }
                }
            }
        }
        return ((SPEntry)getVertex(tar)).weight;
    }

    /** Method for SP8 Q4, Bellmen-Ford Algorithm */
    public boolean bellmanFord(){
        int result = bfSP(src);
        for(BVertex u : node){
            System.out.println(((SPEntry) u).thisVertex + " " + ((SPEntry) u).weight);
        }
        return result < Integer.MAX_VALUE;
    }
    public int bfSP(Vertex tar){
        reInit(); // Reset the Graph before execute the algorithm
        ArrayDeque<Vertex> auxQueue = new ArrayDeque<>();

        SPEntry tmp = (SPEntry) getVertex(src);
        tmp.weight = 0;
        tmp.seen = true;
        auxQueue.add(src);

        while(!auxQueue.isEmpty()){
            Vertex current = auxQueue.remove();
            tmp = (SPEntry) getVertex(current);
            tmp.seen = false;
            tmp.count += 1;
            if (tmp.count >= g.size())
                return Integer.MAX_VALUE; // MAX_VALUE means this Graph has negative cycle
            for (Edge e : current){
                Vertex otherVertex = e.otherEnd(current);
                SPEntry otherSide = (SPEntry) getVertex(otherVertex);
                if (otherSide.weight > tmp.weight + e.weight){
                    otherSide.weight = tmp.weight + e.weight;
                    otherSide.parent = current;
                    if (!otherSide.seen){
                        auxQueue.add(otherVertex);
                        otherSide.seen = true;
                    }
                }
            }
        }

        return ((SPEntry)getVertex(tar)).weight;
    }

    /** Method for SP8 Q5, fastest SP Algorithm*/
    public boolean fastestShortestPaths(){
        int mode = graphCheck();
        switch (mode){
            case (1):{
                bfs();
                break;
            }
            case (2):{
                dagShortestPaths();
                break;
            }
            case (3):{
                dijkstra();
                break;
            }
            case (4):{
                return bellmanFord();
            }
        }
        return true;
    }

    // Auxiliary methods
    private int graphCheck(){
        int count = 0;
        Integer tracker = null;
        for (Vertex u : g){
            for (Edge e : u) {
                if (e.weight < 0)
                    return 4; // Bellmen Ford
                else{
                    if (tracker == null)
                        tracker = e.weight;
                    else if (!tracker.equals(e.weight)){
                        count++;
                    }
                }
            }
        }
        if (count == 0)
            return 1; // BFS
        else{
            // All edges are positive this case
            // Check DAG
            return dfs(src) == null ? 3 : 2;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Test code below
        Scanner in;
        String fileName = "F:/TestGraph/newTest.txt";
        File inputFile = new File(fileName);
        in = new Scanner(inputFile);

        //Graph g = Graph.readGraph(in);
        Graph g = Graph.readDirectedGraph(in);
        ShortestPath tester = new ShortestPath(g, g.getVertex(1));
        tester.fastestShortestPaths();
    }
}
