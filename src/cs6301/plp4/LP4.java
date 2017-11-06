package cs6301.plp4;

import cs6301.plp4.Graph.Vertex;
import cs6301.plp4.Graph.Edge;

import java.util.*;

/**
 * Created by Alan Lin on 11/1/2017.
 *@ author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class LP4 extends BasicGraphAlgorithm{
    /** Nested Vertex class extended BVertex for following LP4 algorithms */
    class LPVertex extends BasicGraphAlgorithm.BVertex implements Index{
        int weight, preWeight; // Parameters used to store the information of shortest paths this iteration and previous iteration
        int spCount; // Parameter used to indicates the # of shortest paths into this vertex
        int inDegree; // Parameter used to indicates the in degree of this vertex
        int index; // Parameter used to implement the Index interface
        boolean visited; // Auxiliary parameter used in count&enumerate Topological Orders & Shortest Paths
        Vertex pBack, thisVertex;

        // Constructor
        LPVertex(Vertex u){
            super(u);
            weight = Integer.MAX_VALUE;
            preWeight = Integer.MAX_VALUE; // Initially, the weight&preWeight of each vertex is Infinity
            visited = false;
            spCount = 0; index = -1; inDegree = u.revAdj.size();
            thisVertex = u; pBack = null;
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

        @Override
        public void putIndex(int index) {
            this.index = index;
        }

        @Override
        public int getIndex() {
            return index;
        }
    }

    /** Nested Path class used to store the SP and its corresponding reward */
    class PathNode{
        LinkedList<Vertex> path; // vertices form this path
        int reward; // The total reward we can got from this path

        // Constructors
        PathNode(){
            path = new LinkedList<>();
            reward = 0;
        }

        PathNode(List<Vertex> path, HashMap<Vertex, Integer> rMap){
            this();
            addPath(path, rMap);
        }

        void addPath(List<Vertex> path, HashMap<Vertex, Integer> rMap){
            for(Vertex u : path){
                this.path.add(u);
                reward += rMap.get(u);
            }
        }
    }

    private Vertex src, target; // Source node for upcoming algorithm
    private int topoCount; // Global parameter used to count the # of topological ordering
    private PriorityQueue<PathNode> pathPQ; // Auxiliary PQ used to store the pathNode

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
        Comparator<PathNode> rewardCom = new Comparator<PathNode>() {
            @Override
            public int compare(PathNode o1, PathNode o2) {
                if (o1.reward == o2.reward)
                    return 0;
                else if (o1.reward > o2.reward)
                    return -1;
                else return 1;
            }
        }; // Create comparator for pathPQ
        this.pathPQ = new PriorityQueue<>(g.size() * 4, rewardCom);
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

    // Part f. Reward collection problem
    // Reward for vertices is passed as a parameter in a hash map
    // tour is empty list passed as a parameter, for output tour
    // Return total reward for tour
    public int reward(HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour) {
        int result = 0;
        try{
            dijkstraSP(); // Use Dijkstra algorithm to generate shortest Paths
        }catch (BinaryHeap.HeapFullException e) {
            e.printStackTrace();
        }
        eliminateGraph(); // Eliminate non-tight edges

        // Generate all shortest path, ordered by the reward it can get
        for(Vertex u : g){
            target = u;
            rewardSPs(vertexRewardMap);
        }
        ((LPGraph) g).resetGraph();

        // Test each Path -- form the tour
        PathNode tmpPN;
        Vertex tail;
        boolean findFlag;
        ArrayDeque<Vertex> visitedList = new ArrayDeque<>();
        ArrayDeque<Vertex> helper = new ArrayDeque<>();

        while(!pathPQ.isEmpty()){
            tmpPN = pathPQ.poll();
            tail = tmpPN.path.getLast();
            pathUpdatePath(tmpPN.path, true); // Visit those vertices

            findFlag = findPathBack(tail, visitedList, helper);
            if (findFlag){
                result = tmpPN.reward;
                rebuildTour((LPVertex)getVertex(tail), tmpPN.path, tour);
                break;
            }

            pathUpdatePath(tmpPN.path, false); // Unvisited those vertices
        }
        return result;
    }

    // Comparators
    private Comparator<LPVertex> privateCom = new Comparator<LPVertex>() {
        @Override
        public int compare(LPVertex o1, LPVertex o2) {
            if (o1.weight == o2.weight)
                return 0;
            else if (o1.weight > o2.weight)
                return 1;
            else
                return -1;
        }
    };


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

    /** Auxiliary method for Reward problem with Shortest Paths */
    private void rewardSPs(HashMap<Vertex, Integer> rMap){
        LPGraph.LPVertex tmp = ((LPGraph) g).getVertex(src); // start from src
        LinkedList<Vertex> result = new LinkedList<>();
        auxRewardSP(tmp, result, rMap);
    }

    /** Inner auxiliary method for Reward problem with Shortest Paths -- Visit each path and its reward */
    private void auxRewardSP(Vertex u, LinkedList<Vertex> res, HashMap<Vertex, Integer> rMap){
        Vertex current;
        ((LPVertex)getVertex(u)).visited = true;
        res.add(u);

        if (u == target)// Visit this path
            pathPQ.add(new PathNode(res, rMap));
        else{
            for (Edge e : u){
                current = e.otherEnd(u);
                auxRewardSP(current, res, rMap);
            }
        }
        // Reverse this visit operation
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

    // Shortest Path Algorithms
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

    /** Private auxiliary Method used to performs Dijkstra Algorithm */
    private void dijkstraSP() throws BinaryHeap.HeapFullException{
        resetRewardCollection();
        LPVertex[] auxList = new LPVertex[g.size()];
        Vertex ver;
        IndexedHeap<LPVertex> auxHeap = new IndexedHeap<>(auxList, privateCom);

        LPVertex tmp = (LPVertex) getVertex(src), other;
        tmp.weight = 0;
        auxHeap.add(tmp);

        // Assign Shortest Paths using Dijkstra Algorithm
        while(!auxHeap.isEmpty()){
            tmp = auxHeap.remove();
            tmp.visited = true;

            for (Edge e : tmp.thisVertex){
                ver = e.otherEnd(tmp.thisVertex);
                other = (LPVertex) getVertex(ver);
                if (!other.visited){
                    // This vertex hasn't been visited
                    if (other.index == -1)
                        auxHeap.add((LPVertex) getVertex(ver)); // Observe this vertex first time

                    if (other.weight > e.weight + tmp.weight){
                        // Observed this vertex, update it's priority if applicable
                        other.weight = e.weight + tmp.weight;
                        auxHeap.update(other.index);
                    }
                }
            }
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

    // Graph & Structure auxiliary methods
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

    /** Method to reset the parameters for reward collection problem */
    private void resetRewardCollection(){
        LPVertex tmp;
        for(Vertex u : g){
            tmp = (LPVertex) getVertex(u);
            tmp.index = -1;
            tmp.visited = false;
            tmp.weight = Integer.MAX_VALUE; // Pending
        }
    }

    /** Method to set the vertices in the path as visited or not */
    private void pathUpdatePath(List<Vertex> path, boolean mode){
        LPVertex tmp;
        for(Vertex u : path){
            tmp = (LPVertex) getVertex(u);
            tmp.visited = mode;
        }
    }

    /** Part--F Method to rebuild the tour from scratch */
    private void rebuildTour(LPVertex fin, List<Vertex> coming, List<Vertex> res){
        // Add coming path
        ArrayDeque<Vertex> auxHeap = new ArrayDeque<>();
        res.addAll(coming);

        // Add back path
        Vertex thisVertex = src;
        LPVertex tmp = (LPVertex) getVertex(thisVertex);
        while(thisVertex.getName() != fin.thisVertex.getName()){
            auxHeap.push(thisVertex);
            thisVertex = tmp.pBack;
            tmp = (LPVertex) getVertex(thisVertex);
        }
        while(!auxHeap.isEmpty()){
            res.add(auxHeap.pop());
        }
    }

    /** Part--F Method to find a path back from specific vertex using BFS */
    private boolean findPathBack(Vertex str, ArrayDeque<Vertex> visitedList, ArrayDeque<Vertex> helper){
        visitedList.clear();
        helper.clear();
        visitedList.add(str);
        LPVertex tmp;
        Vertex thisV, otherV;
        Vertex srcLp = ((LPGraph) g).getVertex(src);
        boolean findFlag = false;

        // BFS
        while(!visitedList.isEmpty()){
            thisV = visitedList.remove();
            if (thisV == srcLp) {
                findFlag = true;
                break;
            }

            for (Edge e : thisV){
                otherV = e.otherEnd(thisV);
                tmp = (LPVertex) getVertex(otherV);
                if (!tmp.visited || tmp.thisVertex == src) {
                    visitedList.add(otherV);
                    helper.add(otherV);
                    tmp.visited = true;
                    tmp.pBack = thisV;
                }
            }
        }

        if (!findFlag){
            // Cannot reach src from this vertex -- reverse the changed of the Graph
            while(!helper.isEmpty()){
                thisV = helper.remove();
                tmp = (LPVertex) getVertex(thisV);
                tmp.visited = false;
            }
            return false;
        }
        else
            return true;
    }

    /** Auxiliary method to print a list of vertices */
    private void printOrder(List<Vertex> target){
        for (Vertex u : target)
            System.out.print(u + " ");
        System.out.println();
    }
}
