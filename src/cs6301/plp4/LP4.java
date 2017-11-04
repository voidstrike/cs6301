package cs6301.plp4;

import cs6301.plp4.Graph.Vertex;
import cs6301.plp4.Graph.Edge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Alan Lin on 11/1/2017.
 *@ author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class LP4 extends BasicGraphAlgorithm{
    /** Nested Vertex class extended BVertex for following LP4 algorithms */
    class LPVertex extends BasicGraphAlgorithm.BVertex{
        int weight, preWeight; // Parameters used to store the information of shortest paths this iteration and previous iteration
        int spCount; // Parameter used to indicates the # of shortest paths into this vertex
        int inDegree; // Parameter used to indicates the in degree of this vertex
        boolean visited; // Auxiliary parameter used in count&enumerate Topological Orders & Shortest Paths

        // Constructors
        LPVertex(Vertex u){
            super(u);
            this.weight = Integer.MAX_VALUE;
            this.preWeight = Integer.MAX_VALUE; // Initially, the weight&preWeight of each vertex is Infinity
            this.spCount = 0;
            this.visited = false;
            this.inDegree = u.revAdj.size();
        }

        void setWeight(int weight){
            this.weight = weight;
        }

        void decreaseInDegree(){
            inDegree--;
        }

        void increaseInDegree(){
            inDegree++;
        }
    }

    private Vertex src, target; // Source node for upcoming algorithm
    private int topoCount; // Global parameter used to count the # of topological ordering

    /** Standard Constructor for this class
     * @param g
     *          g must be extended Graph that implement Vertex&Edge disability
     * @param s
     *          s is the source vertex */
    public LP4(Graph g, Vertex s){
        super(new LPGraph(g));
        this.src = s;
        this.target = null;
        this.topoCount = 0;
        node = new LPVertex[g.size()];
        for(Vertex u : g)
            node[u.getName()] = new LPVertex(u);
    }

    // Part a. Return the # of topological orders of g
    // Precondition: g must be a DAG
    public long countTopologicalOrders(){
        LPVertex tmp;
        LinkedList<Vertex> result = new LinkedList<>();
        this.topoCount = 0;

        for(Vertex u : g){ // Iterate through all vertices that unvisited & in degree is zero
            tmp = (LPVertex) getVertex(u);
            if (!tmp.visited && tmp.inDegree == 0)
                auxEnumerateTP(u, result, false);
        }
        reInitialize(); // Restore the structure of this Graph
        return topoCount;
    }

    // Part b. Print all topological orders of g, one per line
    // and return the number of topological orders of g
    public long enumerateTopologicalOrders(){
        LPVertex tmp;
        LinkedList<Vertex> result = new LinkedList<>();
        this.topoCount = 0;

        for(Vertex u : g){ // Iterate through all vertices that unvisited & in degree is zero
            tmp = (LPVertex) getVertex(u);
            if (!tmp.visited && tmp.inDegree == 0)
                auxEnumerateTP(u, result, true);
        }
        reInitialize(); // Restore the structure of this Graph
        return topoCount;
    }

    /** Auxiliary method for topological order
     * @param u
     *      The vertex going to visit in this step
     * @param res
     *      The vertices visited in topological ordering so far
     * @param printFlag
     *      Flag parameter used to determine the behavior of print */
    private void auxEnumerateTP(Vertex u, LinkedList<Vertex> res, boolean printFlag){
        LPVertex tmp;
        ((LPVertex)getVertex(u)).visited = true; // Visit this vertex, add to res
        res.add(u);
        int nextNode = 0;

        for (Edge e : u) // All the vertices u points to decrease the in degree by 1
            ((LPVertex)getVertex(e.otherEnd(u))).decreaseInDegree();
        for (Vertex v : g){ // Find all vertices with 0 inDegree now
            tmp = (LPVertex) getVertex(v);
            if (!tmp.visited && tmp.inDegree == 0){
                auxEnumerateTP(v, res, printFlag);
                nextNode++;
            }
        }

        if (nextNode == 0){ // check if all node included
            if(printFlag)
                printOrder(res);
            topoCount++;
        }

        // Backtracking -- Unvisited this vertex
        ((LPVertex)getVertex(u)).visited = false;
        res.removeLast();
        for (Edge e : u) // All the vertices u points to increase the in degree by 1
            ((LPVertex)getVertex(e.otherEnd(u))).increaseInDegree();
    }

    // Part c. Return the number of shortest paths from s to t
    // Return Long.MIN_VALUE if the graph has a negative or zero cycle
    public long countShortestPaths(Vertex tar){
        LPVertex tmp;
        ((LPGraph) g).resetGraph(); // Reset the Graph
        reInitialize(); // Restore the structure of this Graph
        List<Vertex> topo = dfs(src); // Get topological order from intact graph
        this.target = ((LPGraph) g).getVertex(tar);

        if (topo == null){ // The input graph is not a DAG, Use Bellman-Ford Algorithm
            bellmanFordSP(g.size()); // At most g.size() steps
            eliminateGraph(); // remove unreachable vertices and non-tight edges
            tmp = (LPVertex) getVertex(tar);
            if (tmp.weight != tmp.preWeight) // target reachable from src, but negative or zero cycle encountered
                System.out.println("Non-positive cycle in graph.  Unable to solve problem");
            else if (tmp.weight == Integer.MAX_VALUE)
                System.out.println("Cannot reach target from src");
        }
        else{ // The input graph is a DAG, use DAG SP Algorithm
            dagSP(topo);
            eliminateGraph(); // remove unreachable vertices and non-tight edges
        }
        //return countTopologicalOrders();
        return countSPs(false);
    }

    // Part d. Print all shortest paths from src to tar, one per line,
    // and return number of shortest paths from s to t.
    // Return -1 if the graph has a negative or zero cycle;
    public long enumerateShortestPaths(Vertex tar){
        LPVertex tmp;
        ((LPGraph) g).resetGraph(); // Reset the Graph
        reInitialize(); // Restore the structure of this Graph
        List<Vertex> topo = dfs(src); // Get topological order from intact graph
        this.target = ((LPGraph) g).getVertex(tar);

        if (topo == null){ // Use Bellman Ford Algorithm
            bellmanFordSP(g.size());
            eliminateGraph();
            tmp = (LPVertex) getVertex(tar);
            if (tmp.weight != tmp.preWeight){
                System.out.println("Non-positive cycle in graph.  Unable to solve problem");
            }
            else if (tmp.weight == Integer.MAX_VALUE){
                System.out.println("Cannot reach from src");
            }
        }
        else{ // Use DAG SP Algorithm
            dagSP(topo);
            eliminateGraph();
        }
        return countSPs(true);
    }

    // Part e. Return the weight of shortest path from src to tar using at most k edges
    public int constrainedShortestPath(Vertex tar, int k){
        // Bellman-Ford Algorithm
        resetSP();
        bellmanFordSP(k);
        return ((LPVertex) getVertex(tar)).weight;
    }

    /** Auxiliary method for Shortest Paths */
    private long countSPs(boolean printFlag){
        this.topoCount = 0;
        LPGraph.LPVertex tmp = ((LPGraph) g).getVertex(src);
        LinkedList<Vertex> result = new LinkedList<>();
        auxEnumerateSP(tmp, result, printFlag);
        return topoCount;
    }

    /** Auxiliary method for shortest paths
     * @param u
     *      The vertex going to visit in this step
     * @param res
     *      The vertices visited in shortest path so far
     * @param printFlag
     *      Flag parameter used to determine the behavior of print */
    private void auxEnumerateSP(Vertex u, LinkedList<Vertex> res, boolean printFlag){
        Vertex current;
        ((LPVertex)getVertex(u)).visited = true;
        res.add(u);
        if (u == target){
            // Visit this
            if (printFlag)
                printOrder(res);
            topoCount++;
        }
        else{
            for (Edge e : u){
                current = e.otherEnd(u);
                auxEnumerateSP(current, res, printFlag);
            }
        }
        ((LPVertex)getVertex(u)).visited = false;
        res.removeLast();
    }

    static void printGraph(Graph g, HashMap<Vertex,Integer> map, Vertex s, Vertex t, int limit) {
        System.out.println("Input graph:");
        for(Vertex u: g) {
            if(map != null) {
                System.out.print(u + "($" + map.get(u) + ")\t: ");
            } else {
                System.out.print(u + "\t: ");
            }
            for(Edge e: u) {
                System.out.print(e + "[" + e.weight + "] ");
            }
            System.out.println();
        }
        if(s != null) { System.out.println("Source: " + s); }
        if(t != null) { System.out.println("Target: " + t); }
        if(limit > 0) { System.out.println("Limit: " + limit + " edges"); }
        System.out.println("___________________________________");
    }

    /** Private auxiliary Method used to performs Bellman Ford Algorithm */
    private void bellmanFordSP(int maxIter){
        LPVertex currentVertex, otherVertex;
        LPVertex lpSrc = (LPVertex) getVertex(src);
        lpSrc.setWeight(0); lpSrc.preWeight = 0; lpSrc.spCount = 1;
        for (int iter = 1; iter <= maxIter; iter++){
            // Graph with no disabled edge or vertex
            for (Vertex u : g){
                currentVertex = (LPVertex) getVertex(u);
                for (Edge e : u){
                    otherVertex = (LPVertex) getVertex(e.otherEnd(u));
                    if (currentVertex.preWeight != Integer.MAX_VALUE){
                        if (otherVertex.weight > (currentVertex.preWeight + e.weight)) {
                            otherVertex.setWeight(currentVertex.preWeight + e.weight);
                        }
                    }
                }
            }
            // Update the preWeight of every Vertex
            for (Vertex u : g){
                currentVertex = (LPVertex) getVertex(u);
                currentVertex.preWeight = currentVertex.weight;
            }
        }
    }

    /** Private auxiliary Method used to performs DAG SP Algorithm */
    private void dagSP(List<Vertex> topo){
        // Define parameters
        LPVertex currentVertex, otherVertex;
        LPVertex lpSrc = (LPVertex) getVertex(src);
        lpSrc.setWeight(0); lpSrc.preWeight = 0; lpSrc.spCount = 1;
        // loop Part
        for (Vertex u : topo){
            currentVertex = (LPVertex) getVertex(u);
            for (Edge e : u){
                otherVertex = (LPVertex) getVertex(e.otherEnd(u));
                if (currentVertex.weight != Integer.MAX_VALUE){
                    if (currentVertex.weight + e.weight < otherVertex.weight)
                        otherVertex.setWeight(currentVertex.weight + e.weight);
                }
            }
        }
        // Update preWeight for this alg, In order for further use
        for (Vertex u : topo){
            currentVertex = (LPVertex) getVertex(u);
            currentVertex.preWeight = currentVertex.weight;
        }
    }

    /** Remove non-tight edges and unreachable vertex from the graph */
    private void eliminateGraph(){
        LPVertex currentVertex, otherVertex;
        for(Vertex u : g){
            currentVertex = (LPVertex) getVertex(u);

            for(Edge e : u){
                otherVertex = (LPVertex) getVertex(e.otherEnd(u));
                if (currentVertex.weight + e.weight != otherVertex.weight){
                    otherVertex.inDegree--;
                    ((LPGraph.LEdge) e).disable();
                }
            }

            if (currentVertex.weight == Integer.MAX_VALUE){
                ((LPGraph) g).disableVertex(u);
            }
        }
    }

    // Auxiliary methods
    /** Auxiliary method to reset the attribute for shortestPath */
    private void resetSP(){
        LPVertex tmp;
        for (Vertex u : g){
            tmp = (LPVertex) getVertex(u);
            tmp.preWeight = Integer.MAX_VALUE;
            tmp.weight = Integer.MAX_VALUE;
        }
        tmp = (LPVertex) getVertex(src);
        tmp.weight = 0; // Set the distance from src -> src 0
    }

    /** Method to reInitialize the node array */
    private void reInitialize(){
        LPVertex tmp;
        for(Vertex u : g){
            tmp = (LPVertex) getVertex(u);
            tmp.weight = Integer.MAX_VALUE;
            tmp.preWeight = Integer.MAX_VALUE; // Initially, the weight&preWeight of each vertex is Infinity
            tmp.spCount = 0;
            tmp.visited = false;
            tmp.inDegree = u.revAdj.size();
        }
    }

    /** Auxiliary method to print a list of vertices */
    private void printOrder(List<Vertex> target){
        for (Vertex u : target)
            System.out.print(u + " ");
        System.out.println();
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
        //Vertex startVertex = enhancedG.getVertex(1);
        LP4 tester = new LP4(enhancedG, startVertex);
        //long result = tester.countTopologicalOrders();
        //result = tester.enumerateTopologicalOrders();
        //result = tester.countShortestPaths(g.getVertex(5));
        long result = tester.enumerateShortestPaths(g.getVertex(5));
        System.out.println(result);
    }
}
