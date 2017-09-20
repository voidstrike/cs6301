
// change following line to your group number
package cs6301.lp2;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

public class Euler extends GraphAlgorithm<Euler.EVertex>{
    int VERBOSE;
    Graph.Vertex startVertex;
    List<Graph.Edge> tour;

    // Auxiliary Vertex class for Euler Tour
    static class EVertex{
        int outDegree;
        int inDegree;
        List<Graph.Edge> visitedEdge;

        EVertex(Graph.Vertex u){
            inDegree = u.revAdj.size();
            outDegree = u.adj.size();
            visitedEdge = new ArrayList<>();
        }
    }

    // Constructor
    Euler(Graph g, Graph.Vertex start) {
        super(g);
        node = new EVertex[g.size()];
        startVertex = start;
	    VERBOSE = 1;
	    tour = new ArrayList<>();
	    for (Graph.Vertex u : g){
	        node[u.getName()] = new EVertex(u);
        }
    }

    // To do: function to find an Euler tour
    public List<Graph.Edge> findEulerTour() {
        List<List<Graph.Edge>> tourSet = findTours();
        if(VERBOSE > 9) { printTours(tourSet); }
	    stitchTours(tourSet);
	    return tour;
    }

    /* To do: test if the graph is Eulerian.
     * If the graph is not Eulerian, it prints the message:
     * "Graph is not Eulerian" and one reason why, such as
     * "inDegree = 5, outDegree = 3 at Vertex 37" or
     * "Graph is not strongly connected"
     */
    boolean isEulerian() {
        int sCCNumber = SCC();

        // Check the number of SCC of current Graph g
        if (sCCNumber != 1){
            System.out.println("Graph is not Eulerian");
            System.out.println("Reason: Graph is not strongly connected");
            return false;
        }

        // Check each vertex with its inDegree & outDegree
        EVertex tmp = null;
        for (Graph.Vertex u : g){
            tmp = node[u.getName()];
            if (tmp.inDegree != tmp.outDegree){
                System.out.println("Graph is not Eulerian");
                System.out.println("inDegree = " + tmp.inDegree + ", outDegree = " + tmp.outDegree + " at Vertex " + u.getName());
                return false;
            }
        }
	    return true;
    }

    // Find tours starting at vertices with unexplored edges
    private List<List<Graph.Edge>> findTours() {
        List<List<Graph.Edge>> tourSet = new ArrayList<>();
        ArrayDeque<Graph.Vertex> opSet = new ArrayDeque<>();
        Graph.Vertex currentVertex;

        // initialize the opSet
        opSet.add(startVertex);
        for (Graph.Vertex u : g){
            if (u != startVertex)
                opSet.add(u);
        }

        // Find tour by tourVisit
        while (!opSet.isEmpty()){
            currentVertex = opSet.getFirst();
            if (node[currentVertex.getName()].outDegree == 0){
                opSet.removeFirst();
                continue;
            }

            List<Graph.Edge> currentTour = new ArrayList<>();
            tourVisit(currentVertex, currentVertex, currentTour);

            tourSet.add(currentTour);
        }

        return tourSet;
    }

    /* Print tours found by findTours() using following format:
     * Start vertex of tour: list of edges with no separators
     * Example: lp2-in1.txt, with start vertex 3, following tours may be found.
     * 3: (3,1)(1,2)(2,3)(3,4)(4,5)(5,6)(6,3)
     * 4: (4,7)(7,8)(8,4)
     * 5: (5,7)(7,9)(9,5)
     *
     * Just use System.out.print(u) and System.out.print(e)
     */
    void printTours(List<List<Graph.Edge>> tourSet) {
        for (List<Graph.Edge> sub : tourSet){
            System.out.print(sub.get(0).from.toString() + ": ");
            for (Graph.Edge edge : sub){
                System.out.print(edge.toString());
            }
            System.out.println();
        }
    }

    // Stitch tours into a single tour using the algorithm discussed in class
    void stitchTours(List<List<Graph.Edge>> tourSet) {

        if (tourSet.size() == 0){
            System.out.print("Error");
            return;
        }

        // Initialize the tour with first sub tour from tourSet
        List<Graph.Edge> tmp = tourSet.remove(0);
        for (Graph.Edge e : tmp){
            tour.add(e);
        }

        int replaceTour = 0;
        int replacePoint = 0;
        while (!tourSet.isEmpty()){

            // Try to find a replace Vertex and its replace tour
            for (int i=0; i<tour.size(); i++){
                replaceTour = getReplaceTour(tour.get(i), tourSet);
                if (replaceTour != -1){
                    // Find a replace point
                    replacePoint = i+1;
                    break;
                }
            }

            // Merge this sub tour into final tour
            tmp = tourSet.remove(replaceTour);
            for (Graph.Edge e : tmp){
                tour.add(replacePoint,e);
                replacePoint++;
            }

            // Reset the auxiliary variables
            replacePoint = 0;
            replaceTour = 0;
        }
    }

    void setVerbose(int v) {
	VERBOSE = v;
    }

    // Auxiliary Methods

    // Run the DFS Algorithm and return the Graph.Vertex List ordered by finish time
    private List<Graph.Vertex> getFinRevList(Iterator<Graph.Vertex> in){
        boolean[] seen = new boolean[g.size()];
        Graph.Vertex currentNode = null;
        List<Graph.Vertex> result = new ArrayList<>();

        // Initialize seen array to false
        for (int i = 0; i < seen.length; i++){
            seen[i] = false;
        }

        // Search Phase
        while (in.hasNext()){
            currentNode = in.next();
            if (!seen[currentNode.getName()]){
                // This node is unseen, create new DFS sub tree
                dfsVisit(currentNode, seen, result);
            }
        }

        // Reverse the result
        Collections.reverse(result);
        return result;
    }

    // Get SCC number by ruing the DFS in revAdj
    private int getSCCNumber(Iterator<Graph.Vertex> in){
        int componentNumber = 0;
        boolean[] seen = new boolean[g.size()];
        Graph.Vertex currentVertex = null;

        // Initialize seen array to false
        for (int i = 0; i < seen.length; i++){
            seen[i] = false;
        }

        // Search Phase
        while (in.hasNext()){
            currentVertex = in.next();
            if (!seen[currentVertex.getName()]){
                // This node is unseen, create new DFS sub tree
                dfsVisitRev(currentVertex, seen);
                componentNumber++;
            }
        }

        return componentNumber;
    }

    // DFS Visit use adj
    private void dfsVisit(Graph.Vertex u, boolean[] seenArray, List<Graph.Vertex> finList){
        seenArray[u.getName()] = true;
        for (Graph.Edge edge : u.adj){
            if (!seenArray[(edge.to).getName()]){
                dfsVisit(edge.to, seenArray, finList);
            }
        }
        finList.add(u);
    }

    // DFS Visit use revAdj
    private void dfsVisitRev(Graph.Vertex u, boolean[] seenArray){
        seenArray[u.getName()] = true;
        for (Graph.Edge edge : u.revAdj){
            if (!seenArray[(edge.from).getName()]){
                dfsVisitRev(edge.from, seenArray);
            }
        }
    }

    private boolean tourVisit(Graph.Vertex u, Graph.Vertex src, List<Graph.Edge> currentTour){
        EVertex currentVertex = node[u.getName()];
        boolean exitSign = false;
        if ( u == src && currentTour.size() != 0) // encounter a circuit
            return true;
        else{
            for (Graph.Edge e : u.adj){
                if (!isVisited(e, currentVertex.visitedEdge)){
                    // This edge is never visited
                    currentVertex.visitedEdge.add(e);
                    currentVertex.outDegree--;
                    currentTour.add(e);
                    exitSign = tourVisit(e.to, src, currentTour);
                    if (exitSign) return true;
                }
            }
        }
        return exitSign;
    }

    // Check the number of SCC of current g
    private int SCC(){
        int componentNum = 0;
        List<Graph.Vertex> finRev = getFinRevList(g.iterator());

        if (finRev == null){
            System.out.print("Error occurs");
            return -1;
        }
        componentNum = getSCCNumber(finRev.iterator());
        return componentNum;
    }

    private boolean isVisited(Graph.Edge e, List<Graph.Edge> eSet){
        if (eSet.size() == 0) //no edge was visited from this node
            return false;

        for (Graph.Edge u : eSet){ // some edge(s) visited
            if (u == e)
                return true;
        }
        return false;
    }

    private int getReplaceTour(Graph.Edge e, List<List<Graph.Edge>> tours){
        int replacePoint = -1;
        for (int i=0; i<tours.size(); i++){
            if (tours.get(i).get(0).from == e.to){
                replacePoint = i;
                return replacePoint;
            }
        }
        return replacePoint;
    }

}
