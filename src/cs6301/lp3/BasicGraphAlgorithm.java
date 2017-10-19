package cs6301.lp3;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class BasicGraphAlgorithm extends GraphAlgorithm<BasicGraphAlgorithm.BVertex>{
    static class BVertex{
        int sccNum, pre; // pre is used for path-based SCC algorithm
        boolean seen; // used for search algorithm, indicate whether this vertex is visited before
        BVertex(Graph.Vertex u){
            seen = false;
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
        for(Graph.Vertex u : g)
            node[u.getName()] = new BVertex(u);
        countTimer = 1;
        sccCount = 0;
    }

    BasicGraphAlgorithm(Graph g, int size){
        super(g);
        node = new BVertex[size];
        for(Graph.Vertex u : g)
            node[u.getName()] = new BVertex(u);
        countTimer = 1;
        sccCount = 0;
    }

    protected void reInit(){
        BVertex current;
        countTimer = 1;
        sccCount = 0;
        for(Graph.Vertex u : g){
            current = node[u.getName()];
            current.seen = false;
            current.pre = -1;
            current.sccNum = -1;
        }
    }

    protected void dfsInit(){
        BVertex current;
        for (Graph.Vertex u : g){
            current = getVertex(u);
            current.seen = false;
        }
    }


    // DFS return the topology order if no cycle, otherwise ordered by fin time
    public List<Graph.Vertex> dfs(Iterator<Graph.Vertex> in){
        if (in == null)
            return null;
        // Initialize
        Graph.Vertex cur;
        dfsInit(); // Initialize the graph, set all vertex to unseen
        List<Graph.Vertex> result = new ArrayList<>();
        // Visit Phase
        while(in.hasNext()){
            cur = in.next();
            if (!node[cur.getName()].seen)
                dfsVisit(cur, result);
        }
        // Reverse the order
        Collections.reverse(result);
        return result;
    }

    public List<Graph.Vertex> dfs(Graph.Vertex u){
        // dfs for start from specific nodes
        if (u == null)
            return null;
        dfsInit(); // Initialize the graph, set all vertex to unseen
        List<Graph.Vertex> result = new ArrayList<>();
        dfsVisit(u, result);
        Collections.reverse(result);
        return result;
    }
    protected void dfsVisit(Graph.Vertex cur, List<Graph.Vertex> res){
        Graph.Vertex tar;
        node[cur.getName()].seen = true;
        for (Graph.Edge e : cur){
            tar = e.otherEnd(cur);
            if (!node[tar.getName()].seen)
                dfsVisit(tar, res);
        }
        res.add(cur);
    }

    // assignSCC will generate the SCC of this graph and assign each vertex corresponding sccNum
    public int assignSCC(Iterator<Graph.Vertex> in){
        ArrayDeque<Graph.Vertex> pStack = new ArrayDeque<>();
        ArrayDeque<Graph.Vertex> sStack = new ArrayDeque<>();
        reInit();

        // Initialize
        Graph.Vertex cur;

        // Visit Phase
        while(in.hasNext()){
            cur = in.next();

            if (!node[cur.getName()].seen) {
                sccVisit(cur, pStack, sStack);
            }
        }
        return this.sccCount;
    }
    protected void sccVisit(Graph.Vertex cur, ArrayDeque<Graph.Vertex> p, ArrayDeque<Graph.Vertex> s)
    {
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
        test.assignSCC(g.iterator());
        System.out.println(test.sccCount);
        for(Graph.Vertex u : g){
            System.out.println(u.toString() + "  SCC:" + test.node[u.getName()].sccNum);
        }
    }
}
