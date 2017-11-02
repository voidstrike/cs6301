package cs6301.plp4;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */

import cs6301.plp4.Graph.Edge;
import cs6301.plp4.Graph.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BasicGraphAlgorithm extends GraphAlgorithm<BasicGraphAlgorithm.BVertex> {
    static class BVertex{
        int sccNum, pre; // pre is used for path-based SCC algorithm
        boolean seen; // used for search algorithm, indicate whether this vertex is visited before
        boolean process;
        BVertex(Vertex u){
            seen = false;
            process = false;
            sccNum = -1;
            pre = -1;
        }
    }

    protected int countTimer;
    protected int sccCount;

    //Constructor
    BasicGraphAlgorithm(Graph g){
        super(g);
        node = new BVertex[g.size()];
        for(Vertex u : g)
            node[u.getName()] = new BVertex(u);
        countTimer = 1;
        sccCount = 0;
    }

    BasicGraphAlgorithm(Graph g, int size){
        super(g);
        node = new BVertex[size];
        for(Vertex u : g)
            node[u.getName()] = new BVertex(u);
        countTimer = 1;
        sccCount = 0;
    }

    protected void reInit(){
        BVertex current;
        countTimer = 1;
        sccCount = 0;
        for(Vertex u : g){
            current = node[u.getName()];
            current.seen = false;
            current.process = false;
            current.pre = -1;
            current.sccNum = -1;
        }
    }

    protected void dfsInit(){
        BVertex current;
        for (Vertex u : g){
            current = getVertex(u);
            current.seen = false;
        }
    }


    // DFS return the topology order if no cycle
    public List<Vertex> dfs(Iterator<Vertex> in){
        if (in == null)
            return null;
        // Initialize
        Vertex cur;
        dfsInit(); // Initialize the graph, set all vertex to unseen
        List<Vertex> result = new ArrayList<>();
        // Visit Phase
        while(in.hasNext()){
            cur = in.next();
            if (!getVertex(cur).seen)
                dfsVisit(cur, result);
        }
        Collections.reverse(result);
        return result;
    }

    /** Method to return the topological order of this graph, the list would be null if the graph is not a DAG */
    public List<Vertex> dfs(Vertex u){
        // dfs that start from specific node
        boolean hasCycle;
        if (u == null)
            return null;
        dfsInit(); // Initialize the graph, set all vertex to unseen
        List<Vertex> result = new ArrayList<>();
        hasCycle = dfsVisitEF(u, result);
        if (hasCycle)
            return null; // Has cycle
        Collections.reverse(result);
        return result;
    }

    protected void dfsVisit(Vertex cur, List<Vertex> res){
        Vertex tar;
        getVertex(cur).seen = true;
        for (Edge e : cur){
            tar = e.otherEnd(cur);
            if (!getVertex(tar).seen)
                dfsVisit(tar, res);
        }
        res.add(cur);
    }

    protected boolean dfsVisitEF(Vertex cur, List<Vertex> res){
        Vertex tar;
        getVertex(cur).seen = true;
        getVertex(cur).process = true;
        boolean errorFlag = false;
        for (Edge e : cur){
            tar = e.otherEnd(cur);
            if (!getVertex(tar).seen)
                errorFlag = errorFlag || dfsVisitEF(tar, res); // Didn't arrive this vertex before
            else
                errorFlag = errorFlag || getVertex(tar).process; // Arrived this vertex before, if this vertex is currently under processing, then it's a back edge, raise error flag
        }
        res.add(cur);
        getVertex(cur).process = false;
        return errorFlag;
    }

    // assignSCC will generate the SCC of this graph and assign each vertex corresponding sccNum
    public int assignSCC(Iterator<Vertex> in){
        ArrayDeque<Vertex> pStack = new ArrayDeque<>();
        ArrayDeque<Vertex> sStack = new ArrayDeque<>();
        reInit();

        // Initialize
        Vertex cur;

        // Visit Phase
        while(in.hasNext()){
            cur = in.next();

            if (!node[cur.getName()].seen) {
                sccVisit(cur, pStack, sStack);
            }
        }
        return this.sccCount;
    }
    protected void sccVisit(Vertex cur, ArrayDeque<Vertex> p, ArrayDeque<Vertex> s)
    {
        // Initialize phase
        Vertex other, tmp, cTracker;
        BVertex curB = node[cur.getName()];

        // Visit this node with modification
        curB.pre = countTimer++;
        curB.seen = true;
        p.push(cur);
        s.push(cur);
        // Check each edge from cur -> other
        for (Edge e : cur){
            other = e.otherEnd(cur);
            if (node[other.getName()].pre == -1){
                // Other node didn't be visited before
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

    //Test main function
    public static void main(String[] args)
            throws FileNotFoundException{
        Scanner in;
        String fileName = "F:/TestGraph/testGraph3.txt";
        File inputFile = new File(fileName);
        in = new Scanner(inputFile);

        Graph g = Graph.readDirectedGraph(in);

        BasicGraphAlgorithm test = new BasicGraphAlgorithm(g);
        //test.assignSCC(g.iterator());
        List<Vertex> result = test.dfs(g.getVertex(1));
        if (result == null)
            System.out.println("Not DAG");
        else{
            for (Vertex u : result)
                System.out.print(u + " ");
        }
    }
}
