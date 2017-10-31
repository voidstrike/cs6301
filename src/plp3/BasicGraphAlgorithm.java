package plp3;

/**
 * Created by Alan Lin on 10/6/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BasicGraphAlgorithm<V extends BasicGraphAlgorithm.BVertex, G extends Graph>
        extends GraphAlgorithm<V, G> {
    static class BVertex{
        int sccNum, pre; // pre is used for path-based SCC algorithm
        boolean seen; // used for search algorithm, indicate whether this vertex is visited before
        BVertex(){
            seen = false;
            sccNum = -1;
            pre = -1;
        }
    }

    protected int countTimer; // Global parameter used in SCC algorithm
    protected int sccCount; // Global parameter used in SCC algorithm

    //Constructor
    BasicGraphAlgorithm(V[] nodeList, G g){
        super(g);
        node = nodeList;
        countTimer = 1;
        sccCount = 0;
    }

    protected void reInit(){
        V current;
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
        V current;
        for (Graph.Vertex u : g){
            current = getVertex(u);
            current.seen = false;
        }
    }


    /** Standard DFS Algorithm, return the vertices in topology order if no cycle, in descending fin time otherwise */
    public List<Graph.Vertex> dfs(Iterator<Graph.Vertex> in){
        if (in == null)
            return null;
        // Initialize
        dfsInit(); // Initialize the graph, set all vertex to unseen
        List<Graph.Vertex> result = new ArrayList<>();

        // Visit Phase
        while(in.hasNext()){
            Graph.Vertex cur = in.next();
            if (!getVertex(cur).seen)
                dfsVisit(cur, result);
        }
        // Reverse the order
        Collections.reverse(result);
        return result;
    }

    /** DFS start from specific vertex, do not guarantee visited all vertex in that Graph, return the vertices in DFS tree */
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

    /** Gabow SCC algorithm, assign each vertex corresponding sccNum, and return the number of SCC of this Graph */
    public int assignSCC(Iterator<Graph.Vertex> in){
        ArrayDeque<Graph.Vertex> pStack = new ArrayDeque<>();
        ArrayDeque<Graph.Vertex> sStack = new ArrayDeque<>();
        reInit();

        // Visit Phase
        while(in.hasNext()){
            Graph.Vertex cur = in.next();

            if (!getVertex(cur).seen) {
                sccVisit(cur, pStack, sStack);
            }
        }
        return this.sccCount;
    }

    protected void sccVisit(Graph.Vertex cur, ArrayDeque<Graph.Vertex> p, ArrayDeque<Graph.Vertex> s)
    {
        // Initialize phase
        Graph.Vertex other, tmp, cTracker;
        V curB = getVertex(cur);

        // Visit this node with modification
        curB.pre = countTimer++;
        curB.seen = true;
        p.push(cur);
        s.push(cur);

        // Check each edge from cur -> other
        for (Graph.Edge e : cur){
            other = e.otherEnd(cur);
            if (getVertex(other).pre == -1){
                // Other node didn't be visited before
                sccVisit(other, p, s);
            }
            else{
                // other has a pre assigned
                if (getVertex(other).sccNum == -1){
                    // No component number assigned to this node
                    while(!p.isEmpty()) {
                        tmp = p.peek();
                        if (getVertex(tmp).pre > getVertex(other).pre)
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
        String fileName = "F:/TestGraph/newTest.txt";
        File inputFile = new File(fileName);
        in = new Scanner(inputFile);

        Graph g = Graph.readDirectedGraph(in);

        BVertex[] tmp = new BVertex[g.size()];
        for (Graph.Vertex u : g){
            tmp[u.getName()] = new BVertex();
        }

        BasicGraphAlgorithm<BVertex, Graph> test = new BasicGraphAlgorithm<>(tmp, g);
        test.assignSCC(g.iterator());
        System.out.println(test.sccCount);
        for(Graph.Vertex u : g){
            System.out.println(u.toString() + "  SCC:" + test.node[u.getName()].sccNum);
        }
    }
}
