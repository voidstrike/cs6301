
// change following line to your group number
package cs6301.lp2;

import java.util.List;
import java.util.Iterator;
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
        int usedEdge;
        List<Graph.Edge> vertexTour;

        EVertex(Graph.Vertex u){
            inDegree = u.revAdj.size();
            outDegree = u.adj.size();
            usedEdge = 0;
            vertexTour = null;
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
        int tourNum = findTours();
        if(VERBOSE > 9) { printTours(); }
	    stitchTours(tourNum);
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
    private int findTours() {
        int tourNum = 0;

        Graph.Vertex currentVertex;
        EVertex tmp;

        // Start the first sub tour with startVertex
        List<Graph.Edge> currentTour = new ArrayList<>();
        tourVisit(startVertex, currentTour);
        node[startVertex.getName()].vertexTour = currentTour;
        tourNum++;

        // Find tour by tourVisit
        for (int i=1; i<=g.size(); i++){

            currentVertex = g.getVertex(i);
            tmp = node[currentVertex.getName()];
            if (currentVertex == startVertex || tmp.outDegree <= tmp.usedEdge ){
                continue;
            }

            currentTour = new ArrayList<>();
            tourVisit(currentVertex, currentTour);
            tmp.vertexTour = currentTour;
            tourNum++;
        }

        return tourNum;
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
    void printTours() {
        EVertex tmp;

        for (Graph.Vertex u : g){
            tmp = node[u.getName()];
            if (tmp.vertexTour != null){
                System.out.print(u.toString() + ": ");
                for (Graph.Edge e : tmp.vertexTour){
                    System.out.print((e.toString()));
                }
                System.out.println();
            }
        }
    }

    // Stitch tours into a single tour using the algorithm discussed in class
    void stitchTours(int toursNum) {
        EVertex tmpE;

        if (node[startVertex.getName()].vertexTour == null){
            System.out.print("Error");
            return;
        }

        // Initialize the tour with the first sub tour start by startVertex
        List<Graph.Edge> tmp = node[startVertex.getName()].vertexTour;
        Graph.Vertex currentVertex;
        tour = tmp;
        node[startVertex.getName()].vertexTour = null;

        int replacePoint;
        int tourSize = tour.size();
        for (int i=0; i<tourSize; i++){
            // Iterate through each (edge.to) in current tour
            currentVertex = tour.get(i).to;
            tmpE = node[currentVertex.getName()];
            if (tmpE.vertexTour != null){
                // If this vertex has a sub tour
                toursNum--;
                replacePoint = i+1;

                tour.addAll(replacePoint, tmpE.vertexTour);
                tourSize = tour.size();
                tmpE.vertexTour = null;
                if (toursNum <= 0)
                    break; // No more sub tours
            }
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

        // Search Phase DFS
        while (in.hasNext()){
            currentNode = in.next();
            //System.out.println(currentNode.toString());
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
        // Set this vertex statue to seen
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

    private void tourVisit(Graph.Vertex u, List<Graph.Edge> currentTour){
        Graph.Vertex tmpHead = u;
        Graph.Edge tmpEdge;
        EVertex currentVertex = node[tmpHead.getName()];

        // Go through the tour
        while(currentVertex.usedEdge < currentVertex.outDegree ){
            // Update Phase
            tmpEdge = tmpHead.adj.get(currentVertex.usedEdge);
            currentVertex.usedEdge++;
            currentTour.add(tmpEdge);
            // Swap Phase
            tmpHead = tmpEdge.to;
            currentVertex = node[tmpHead.getName()];
        }
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
}
