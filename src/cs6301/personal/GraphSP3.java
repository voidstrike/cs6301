package cs6301.personal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Alan Lin on 9/11/2017.
 * Needed code files:
 * GraphAlgorithm.java in personal
 * Graph.java in personal
 * ArrayIterator.java in personal
 */
public class GraphSP3 extends GraphAlgorithm<GraphSP3.TVertex> {

    int disTimer; // for Bridges

    // TVertex denotes each vertex with in&outDegree
    static class TVertex{
        int outDegree;
        int inDegree; // used for topological order
        int dis, low; // used for bridges and cut vertices
        boolean cut;
        Graph.Vertex parent; // used for bridges and cut vertices
        TVertex(Graph.Vertex u){
            inDegree = u.revAdj.size();
            outDegree = u.adj.size();
            dis = -1;
            low = -1;
            cut = false;
            parent = null;
        }
    }

    // Constructor
    public GraphSP3(Graph g){
        super(g);
        disTimer = 0;
        node = new TVertex[g.size()];
        for (Graph.Vertex u : g){
            node[u.getName()] = new TVertex(u);
        }
    }

    public void initialize(){
        disTimer = 0;
        for (Graph.Vertex u : g){
            TVertex currentVertex = getVertex(u);
            currentVertex.outDegree = u.adj.size();
            currentVertex.inDegree = u.revAdj.size();
            currentVertex.dis = -1;
            currentVertex.low = -1;
            currentVertex.cut = false;
            currentVertex.parent = null;
        }
    }

    // common DFS procedure
    public List<Graph.Vertex> dfs(Iterator<Graph.Vertex> in){
        boolean errorSign = false;
        int[] seen = new int[g.size()];
        Graph.Vertex currentNode = null;
        List<Graph.Vertex> resultRev = new ArrayList<>();
        List<Graph.Vertex> result = new ArrayList<>();

        // Initialize seen/unseen auxiliary array
        for(int i = 0; i<seen.length; i++){
            seen[i] = 0;
        }
        // Search Phase
        while(in.hasNext()){
            currentNode = in.next();
            if (seen[currentNode.getName()] == 0) {
                // new DFS sub tree will be created in this phase
                dfsVisit(currentNode, seen, resultRev);

                // Check seen array, if there is a 3, which mean we found a back edge, which is illegal
                for (int i = 0; i<seen.length; i++){
                        if (seen[i] == 3){
                        errorSign = true;
                        break;
                    }
                }
            }

            if(errorSign) break; // if we already encounter a back edge, just end the process
        }

        for (int i = resultRev.size()-1; i>=0; i--){
            result.add(resultRev.get(i));
        }

        return errorSign ? null : result;
    }

    // Auxiliary procedure for dfs
    private void dfsVisit(Graph.Vertex u, int[] seenArray, List<Graph.Vertex> recFin){
        seenArray[u.getName()] = 2; // 2 means current path is under processing
        for (Graph.Edge edge : u.adj){
            if(seenArray[(edge.to).getName()] == 2){
                seenArray[u.getName()] = 3; // If there is a back edge, set current node's seen value to 3
            }
            if(seenArray[(edge.to).getName()] == 0){
                dfsVisit(edge.to, seenArray, recFin);
            }
        }
        seenArray[u.getName()] = seenArray[u.getName()] == 3 ? 3 : 1;
        recFin.add(u);
    }

    // SP3 Q1 TOPOLOGICAL ORDER
    public List<Graph.Vertex> topologicalOrder1(){
        // First algorithm, count the inDegree of each vertex
        int countNum = 0;
        Graph.Vertex currentNode = null;
        List<Graph.Vertex> result = new ArrayList<>();
        ArrayDeque<Graph.Vertex> operateQueue = new ArrayDeque<>();

        // Find Start Node
        for (Graph.Vertex u : g){
            if (node[u.getName()].inDegree == 0){
                operateQueue.add(u);
            }
        }

        // Start Operating Phase
        while(!operateQueue.isEmpty()){
            currentNode = operateQueue.poll();
            countNum++;
            result.add(currentNode);
            for (Graph.Edge edge : currentNode.adj){
                node[(edge.to).getName()].inDegree--;
                if (node[(edge.to).getName()].inDegree == 0)
                    operateQueue.add(edge.to);
            }
        }

        // Return
        if(countNum == g.size())
            return result;
        else
            return null;
    }

    public List<Graph.Vertex> topologicalOrder2(){
        // Second Algorithm, using dfs
        List<Graph.Vertex> result = new ArrayList<>();
        Iterator graphIter = g.iterator();
        result = dfs(graphIter);
        return  result;
    }

    // SP3 Q2 SCC
    public int stronglyConnectedComponents(){
        int componentNum = 0;
        GraphSP3 helper = new GraphSP3(g);
        List<Graph.Vertex> topo = helper.dfsNCheck(g.iterator());
        Iterator<Graph.Vertex> tmpIter;

        if (topo == null)
        {
            System.out.print("Error");
            return -1;
        }
        else{
            tmpIter = topo.iterator();
            componentNum = helper.revDfsSCC(tmpIter);
        }
        return componentNum;
    }

    // Auxiliary dfs procedure, without DAG check
    public List<Graph.Vertex> dfsNCheck(Iterator<Graph.Vertex> in){
        int[] seen = new int[g.size()];
        Graph.Vertex currentNode = null;
        List<Graph.Vertex> resultRev = new ArrayList<>();
        List<Graph.Vertex> result = new ArrayList<>();

        // Initialize seen/unseen auxiliary array
        for(int i = 0; i<seen.length; i++){
            seen[i] = 0;
        }
        // Search Phase
        while(in.hasNext()){
            currentNode = in.next();
            if (seen[currentNode.getName()] == 0) {
                // new DFS sub tree will be created in this phase
                dfsVisit(currentNode, seen, resultRev);
            }
        }

        for (int i = resultRev.size()-1; i>=0; i--){
            result.add(resultRev.get(i));
        }
        // ResultRev contains the vertices , ordered by finsh time
        return result;
    }

    //Auxiliary procedure for reversed graph
    private int revDfsSCC(Iterator<Graph.Vertex> in){
        int componentNumber = 0;
        int[] seen = new int[g.size()];
        Graph.Vertex currentNode = null;

        // Initialize seen/unseen auxiliary array
        for(int i = 0; i<seen.length; i++){
            seen[i] = 0;
        }

        // Search Phase
        while(in.hasNext()){
            currentNode = in.next();
            if (seen[currentNode.getName()] == 0) {
                dfsVisitRev(currentNode, seen);
                componentNumber++;
            }
        }

        return componentNumber;
    }

    //Auxiliary procedure for revDfsSCC
    private void dfsVisitRev(Graph.Vertex u, int[] seenArray){
        seenArray[u.getName()] = 1;
        for (Graph.Edge edge : u.revAdj){
            if(seenArray[(edge.from).getName()] == 0){
                dfsVisitRev(edge.from, seenArray);
            }
        }
    }

    // SP3 Q3 EULERIAN
    public static boolean testEulearin(Graph g){
        GraphSP3 tester = new GraphSP3(g);
        int componentNum = tester.stronglyConnectedComponents();

        if (componentNum != 1)
            return false;
        else{
            for(GraphSP3.TVertex node : tester.node){
                if (node.inDegree != node.outDegree)
                    return false;
            }
        }
        return true;
    }

    // SP3 Q4 ISDAG
    public static boolean isDAG(Graph g){
        GraphSP3 helper = new GraphSP3(g);
        return  helper.topologicalOrder2() != null;
        // If there is a back edge, the graph isn't a DAG
    }

    // SP3 Q5 BRIDGE & CUT POINT
    public List<Graph.Edge> findBridgeCut(){
        //TODO
        List<Graph.Edge> bridges = new ArrayList<>();
        TVertex currentVertex = null, parentVertex = null;
        Graph.Vertex src = null, target;
        int subtreeNum = 0;
        dfsBridge(g.iterator());

        // find src
        for(Graph.Vertex u : g){
            if(node[u.getName()].parent == null)
                src = u;
        }

        // find bridges & cut vertices
        for (Graph.Vertex u : g){
            currentVertex = node[u.getName()];
            // add bridge to result List
            if (currentVertex.low >= currentVertex.dis)
                if (currentVertex.parent != null)
                    bridges.add(new Graph.Edge(u, currentVertex.parent, 1));

            // mark cut vertices
            if (currentVertex.parent != null){
                // it's not the root vertex
                if (currentVertex.parent == src)
                    subtreeNum++;

                for (Graph.Edge child : u.adj){
                    target = child.from == u ? child.to : child.from;
                    if (currentVertex.dis <= node[target.getName()].low)
                        currentVertex.cut = true;
                }
            }
        }

        // root vertex
        if (subtreeNum > 1){
            node[src.getName()].cut = true;
        }

        return bridges;
    }

    public List<Graph.Edge> findBridgeCut(Graph g){
        GraphSP3 helper = new GraphSP3(g);
        return helper.findBridgeCut();
    }

    // Auxiliary method, dfsBridge
    public void dfsBridge(Iterator<Graph.Vertex> in){
        int[] seen = new int[g.size()];
        Graph.Vertex currentNode = null;
        List<Graph.Vertex> result = new ArrayList<>();

        // Initialize seen/unseen auxiliary array
        for(int i = 0; i<seen.length; i++){
            seen[i] = 0;
        }

        // Search Phase
        while(in.hasNext()) {
            currentNode = in.next();
            if (seen[currentNode.getName()] == 0) {
                // new DFS sub tree will be created in this phase
                node[currentNode.getName()].parent = null;
                dfsVisitBridge(currentNode, seen, currentNode);
            }
        }
    }

    // Auxiliary method of dfsBridge
    private void dfsVisitBridge(Graph.Vertex u, int[] seenArray, Graph.Vertex parent){
        seenArray[u.getName()] = 1;

        TVertex currentTNode = node[u.getName()];
        TVertex targetTNode = null;
        Graph.Vertex targetNode = null;

        // Set Node properties
//        currentTNode.parent = parent;
        currentTNode.dis = ++disTimer;
        currentTNode.low = node[u.getName()].dis;

        for (Graph.Edge edge : u.adj){
            targetNode = edge.from == u ? edge.to : edge.from;
            targetTNode = node[targetNode.getName()];

            if(seenArray[targetNode.getName()] == 0){
                // tree edge
                node[targetNode.getName()].parent = u;
                dfsVisitBridge(targetNode, seenArray, u);
                if(targetTNode.low < currentTNode.low)
                    currentTNode.low = targetTNode.low;
            }
            else{
                // back edge
                if (currentTNode.parent != targetNode){
                    if(targetTNode.dis < currentTNode.low)
                        currentTNode.low = targetTNode.dis;
                }
            }
        }
    }

    // MAIN
    public static void main(String[] args) throws FileNotFoundException{
        //TODO
        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        int pos = 1;
        //test.add(1, 5);
        for (int i=5; i<9; i++){
            test.add(pos, i);
            pos++;
        }
        for (Integer i : test){
            System.out.print(i);
        }
    }
}
