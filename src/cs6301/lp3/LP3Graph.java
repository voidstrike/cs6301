package cs6301.lp3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import cs6301.lp3.Graph.Vertex;
import cs6301.lp3.Graph.Edge;
import cs6301.lp3.LPGraph.LPVertex;
import cs6301.lp3.LPGraph.LEdge;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

public class LP3Graph extends BasicGraphAlgorithm{
    /** Nested extended Vertex class */
    static class LVertex extends BVertex{
        boolean superNode, rootNode; // boolean parameters indicate whether this vertex is a superVertex & rootVertex
        Vertex supervisor; // indicates which superVertex this vertex belongs, null if it's not a component of any super vertex
        LinkedList<Vertex> corpus; // indicates the vertices belong to this superVertex if this node is a super Vertex

        // Constructors
        LVertex(Vertex u){
            super(u);
            superNode = false;
            rootNode = false;
            supervisor = null;
            corpus = new LinkedList<>();
        }

        /** Method to add vertex into corpus */
        void addVertex(Vertex u){
            corpus.add(u);
        }

        /** Method to check whether this vertex is root */
        boolean isRoot(){
            return this.rootNode;
        }

        /** Method to set this vertex as root */
        void setRoot(){
            rootNode = true;
        }

        /** Method to set the supervise vertex of this vertex */
        void setSuperVisor(Vertex tar){
            supervisor = tar;
        }
    }

    /** Additional parameters for this LP3Graph Algorithm, used to avoid HashMap */
    private LEdge[] auxWeightArray; // Parallel LEdge array stores the information of super Vertex edges merge
    private ArrayDeque<Vertex> operatedVertex; // Parallel Vertex stack stores the information of super Vertex eges merge

    /** Constructor */
    LP3Graph(LPGraph g){
        super(g, 2 * g.size());
        node = new LVertex[2 * g.size()]; // re-initialize the node array
        auxWeightArray = new LPGraph.LEdge[2 * g.size()];
        operatedVertex = new ArrayDeque<>();
        for (Vertex u : g){
            node[u.getName()] = new LVertex(u);
        }
    }



    public int directedMST(Vertex start, List<Edge> dmst){
        LVertex src = (LVertex) getVertex(start);
        Vertex remS = start;
        src.rootNode = true;
        List<Vertex> dfsTree;
        int wmst = 0, tmpW;
        int pivot = g.size(); // parameter used to reset the graph after several shrink
        int[] SCCount = new int[g.size()+1]; // Auxiliary int array to record the # of vertices in corresponding SCC

        while(true){
            // Reduce the incoming weight of non-root vertices
            for(Vertex u : g){
                LVertex tmp = (LVertex) getVertex(u);
                if (!tmp.rootNode){
                    tmpW = reduceIncomingWeight(u);
                    wmst += tmpW;
                }
                else
                    start = u;
            }

            // Run 0-weight visit DFS
            dfsTree = dfs(start);
            LPGraph lg = (LPGraph) g;
            if (dfsTree.size() == lg.availableSize())
                break;

            // In this case, we cannot arrive all available vertices from root vertex
            // Shrink the Graph
            shrinkGraph(SCCount);

        }

        // Expand the Graph, generate dfs Tree
        LPGraph lg = (LPGraph) g;
        lg.patchReset(pivot);
        dfsT(remS, dmst);

        return wmst;
    }

    // Override method from parent Class, using 0 weight edges
    @Override
    // Override to perform 0-edge visit
    protected void dfsVisit(Vertex cur, List<Vertex> res) {
        Graph.Vertex tar;
        node[cur.getName()].seen = true;
        for (Graph.Edge e : cur){
            tar = e.otherEnd(cur);
            if (!getVertex(tar).seen && e.weight == 0)
                // Only visit next node if it has 0 weight
                dfsVisit(tar, res);
        }
        res.add(cur);
    }

    @Override
    // Override to perform 0-edge visit
    protected void sccVisit(Vertex cur, ArrayDeque<Vertex> p, ArrayDeque<Vertex> s) {
        // Initialize phase
        Graph.Vertex other, tmp, cTracker;
        BVertex curB = node[cur.getName()];

        // Visit this node with modification
        curB.pre = countTimer++;
        curB.seen = true;
        p.push(cur);
        s.push(cur);
        // Check each edge from cur -> other
        for (Graph.Edge e : cur){
            if (e.weight == 0){
                other = e.otherEnd(cur);
                if (node[other.getName()].pre == -1){
                    // Other node didn't be visited before
                    if(e.weight == 0)
                        // Only visit next node if this edge has weight 0
                        sccVisit(other, p, s);
                }
                else{
                    // other has a pre assigned
                    if (node[other.getName()].sccNum == -1){
                        // No component number assigned to this node
                        while(!p.isEmpty()) {
                            tmp = p.peek();
                            if (node[tmp.getName()].pre > node[other.getName()].pre)
                                p.pop();
                            else break;
                        }
                    }
                }
            }
        }

        tmp = p.peek();
        if (tmp == cur){
            p.pop();
            sccCount++;
            while(true){
                cTracker = s.pop();
                if (cTracker == cur){
                    node[cTracker.getName()].sccNum = sccCount;
                    break;
                }
                else{
                    node[cTracker.getName()].sccNum = sccCount;
                }
            }
        }
    }

    // Auxiliary methods for direct graph MST
    /** Method to update the incoming edges for specific Vertex*/
    private int reduceIncomingWeight(Vertex u){
        int min = Integer.MAX_VALUE;
        LPGraph lg = (LPGraph) g;
        LPVertex currentLVertex = lg.getVertex(u);

        // Find minimum incoming weight
        Iterator reverse = currentLVertex.reverseIterator();
        while(reverse.hasNext()){
            Edge e = (Edge) reverse.next();
            if (e.weight < min)
                min = e.weight;
        }

        // Update incoming weight
        reverse = currentLVertex.reverseIterator();
        while(reverse.hasNext()){
            LEdge newEdge = (LEdge) reverse.next();

            newEdge.weight -= min;
            if (newEdge.hasMapEdge())
                newEdge.mapEdge.weight -= min; // Update the corresponding edges if available
        }
        return min;
    }

    /** Method to add vertex into Graph*/
    public void addVertex(Vertex u){
        LPGraph lg = (LPGraph) g;
        lg.addVertex(u);
        node[u.getName()] = new LVertex(u);
    }

    /** Method to process super vertices*/
    private void addSuperVertices(int nodeSize, int[] sccCount){

        // S1 : add n superVertex into graph
        int current = g.size();
        LPGraph lg = (LPGraph) g;
        for(int i=0; i<nodeSize;i++){
            Vertex tmp = new Vertex(current + i);
            addVertex(tmp);
            node[current+i] = new LVertex(tmp);
            ((LVertex)node[current+i]).superNode = true;
        }

        // S2 : Add vertices to super Vertex
        for(Vertex u : g){
            LVertex thisVertex = (LVertex) getVertex(u);
            int sccNumber = thisVertex.sccNum;
            if (sccNumber!=-1 && sccCount[sccNumber] < 0){
                thisVertex.setSuperVisor(lg.getVertex(current - sccCount[sccNumber]));
                LVertex tmp = (LVertex) node[current-sccCount[sccNumber]-1];
                if (thisVertex.isRoot())
                    tmp.setRoot();
                tmp.addVertex(u);
                lg.disable(u.getName()+1);
            }
        }

        // S3 : Handle edges
        // Handling incoming edges
        for (int i=0; i<nodeSize; i++){
            Vertex currentVertex = g.getVertex(current+i+1);  // (current+i+1) is the position of currently processing superVertex
            LVertex currentLVertex = (LVertex) getVertex(currentVertex);

            // Exam all incoming edges of each component
            for (Vertex u : currentLVertex.corpus){
                Iterator iterator = ((LPVertex) u).lRevAdj.iterator();
                while (iterator.hasNext()){
                    Edge e = (Edge) iterator.next();
                    Vertex otherEnd = e.otherEnd(u);
                    if (lg.getVertex(otherEnd).isDisabled()){
                        // otherEnd is in some superVertex created this iteration, ignore it
                        // the edge of superVertex to superVertex will be handled in outgoing edges part
                        continue;
                    }

                    // add edge information to auxiliary parallel array & stack
                    if (auxWeightArray[otherEnd.getName()] == null){
                        auxWeightArray[otherEnd.getName()] = (LEdge) e;
                        operatedVertex.add(otherEnd);
                    }
                    else{
                        // the edge from this vertex is encountered before, try to update
                        if (e.weight < auxWeightArray[otherEnd.getName()].weight)
                            auxWeightArray[otherEnd.getName()] = (LEdge) e;
                    }
                }
            }

            // Add new generated edges and reset the auxiliary stack
            while (!operatedVertex.isEmpty()){
                Vertex otherEnd = operatedVertex.pop();
                LEdge tmpEdge = auxWeightArray[otherEnd.getName()];
                lg.addEdge(otherEnd, currentVertex, tmpEdge.weight, tmpEdge);
                auxWeightArray[otherEnd.getName()] = null;
            }
        }

        // handling outgoing edges
        for (int i=0; i<nodeSize; i++){
            Vertex currentVertex = g.getVertex(current+i+1);  // (current+i+1) is the position of processing superVertex
            LVertex currentLVertex = (LVertex) getVertex(currentVertex);

            // Exam all outgoing edges of each component
            for (Vertex u : currentLVertex.corpus){
                Iterator iterator = ((LPVertex) u).lAdj.iterator();
                while (iterator.hasNext()){
                    Edge e = (Edge) iterator.next();
                    Vertex otherEnd = e.otherEnd(u);

                    // if otherEnd is disabled, the only reason is it belong to some super Vertex created this iteration
                    if (lg.getVertex(otherEnd).isDisabled()){
                        LVertex oE = (LVertex) getVertex(otherEnd);
                        if (oE.supervisor == currentVertex)
                            continue; // otherEnd  is the component of this super Vertex
                        else
                            otherEnd = oE.supervisor; // otherEnd is the component of other super Vertex
                    }

                    // add edge information to auxiliary parallel array & stack
                    if (auxWeightArray[otherEnd.getName()] == null){
                        // the edge to this vertex is never encountered before
                        auxWeightArray[otherEnd.getName()] = (LEdge) e;
                        operatedVertex.add(otherEnd);
                    }
                    else{
                        // the edge to this vertex is encountered before, try to update
                        if (e.weight < auxWeightArray[otherEnd.getName()].weight)
                            auxWeightArray[otherEnd.getName()] = (LEdge) e;
                    }
                }
            }

            // Add new generated edges and reset the auxiliary stack
            while (!operatedVertex.isEmpty()){
                Vertex otherEnd = operatedVertex.pop();
                LEdge tmpEdge = auxWeightArray[otherEnd.getName()];
                lg.addEdge(currentVertex, otherEnd, tmpEdge.weight, tmpEdge);
                auxWeightArray[otherEnd.getName()] = null;
            }
        }
    }

    /** Method to shrink zero cycle to super vertex and handle edges change*/
    private void shrinkGraph(int[] auxArr){
        int zeroCycle=0, indexCounter=0; // Initial the zero-cycle of this iteration and indexCounter is the trick for further work
        LPGraph lg = (LPGraph) g; // make current Graph LPGraph due to the code below uses data structure in LPGraph

        // Run assignSCC to get SCC assignment
        int maxC = assignSCC(lg.iterator());

        // Initialize the auxiliary array
        initSCCArray(auxArr, 1, maxC);

        // Update the auxArr, where the superVertex of each vertex is added
        for(Vertex u : lg){
            int sccNumber = node[u.getName()].sccNum;
            if (auxArr[sccNumber] == 0)
                auxArr[sccNumber]++;
            else if( auxArr[sccNumber] == 1) {
                zeroCycle++;
                auxArr[sccNumber] = --indexCounter;
            }
        }
        addSuperVertices(zeroCycle, auxArr);
    }

    // Helper method for data structure process
    /** Method to reset the arr[s - e] to zero*/
    private void initSCCArray(int[] arr, int s, int e){
        for(int i=s; i<=e; i++){
            arr[i] = 0;
        }
    }

    // Additional DFS to generate the dfs tree from one specific vertex
    /** DFS for this algorithm only, build dfs tree*/
    public void dfsT(Vertex u, List<Edge> res){
        // dfs that start from the specified node
        if (u == null)
            return;
        dfsInit(); // Initialize the graph, set all vertex to unseen
        dfsTVisit(u, res);
    }

    /** Auxiliary method like dfsVisit for dfsT */
    private void dfsTVisit(Vertex cur, List<Edge> res) {
        Graph.Vertex tar;
        node[cur.getName()].seen = true;
        for (Graph.Edge e : cur){
            tar = e.otherEnd(cur);
            if (!node[tar.getName()].seen && e.weight == 0){
                // Only visit next node if it has 0 weight
                res.add(e);
                dfsTVisit(tar, res);
            }
        }
    }

    // Main method, for test
    public static void main(String[] args)
            throws FileNotFoundException{
        //Test code below
        Scanner in;
        String fileName = "F:/TestGraph/newTest.txt";
        File inputFile = new File(fileName);
        in = new Scanner(inputFile);

        in.nextInt();
        Graph g = Graph.readDirectedGraph(in);
        LPGraph lg = new LPGraph(g);

        LP3Graph tester = new LP3Graph(lg);
        Vertex src = lg.getVertex(1);
        List<Edge> resSet = new LinkedList<>();
        int result = tester.directedMST(src, resSet);

        System.out.println(result);
        System.out.println("____________________________________________________________");
        System.out.println(resSet.size());
    }
}