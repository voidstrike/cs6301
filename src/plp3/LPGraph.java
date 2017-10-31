package plp3;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alan Lin on 10/16/2017.
 *
 */
public class LPGraph extends Graph {
    static class LPVertex extends Vertex{
        boolean disabled; // status of current vertex
        List<LEdge> lAdj; // List of outgoing edges
        List<LEdge> lRevAdj; // List of incoming edges

        // Constructor
        LPVertex(Vertex u){
            super(u);
            disabled = false;
            lAdj = new LinkedList<>();
            lRevAdj = new LinkedList<>();
        }

        // return the status of current vertex
        boolean isDisabled(){
            return disabled;
        }

        // disable current vertex
        void disable(){
            disabled = true;
        }

        // enable current vertex
        void enable(){
            disabled = false;
        }

        @Override
        public Iterator<Edge> iterator() {
            return new LPVertexIterator(this, true);
        }

        @Override
        public Iterator<Edge> reverseIterator() {
            return new LPVertexIterator(this, false);
        }

        class LPVertexIterator implements Iterator<Edge>{
            LEdge cur;
            Iterator<LEdge> it;
            boolean ready;

            LPVertexIterator(LPVertex u, boolean mode){
                if (mode)
                    this.it = u.lAdj.iterator();
                else
                    this.it = u.lRevAdj.iterator();
                ready = false;
            }

            @Override
            public boolean hasNext() {
                if (ready) return true;
                if (!it.hasNext()) return false;
                while(cur.isDisabled() && it.hasNext())
                    cur = it.next();
                ready = true;
                return !cur.isDisabled();
            }

            @Override
            public Edge next() {
                if (!ready){
                    if (!hasNext())
                        throw new java.util.NoSuchElementException();
                }
                ready = false;
                return cur;
            }
        }
    }

    static class LEdge extends Edge{
        boolean disabled;
        LEdge mapEdge;

        // Constructor
        LEdge(LPVertex from, LPVertex to, int weight){
            super(from, to, weight);
            disabled = false;
            mapEdge = null;
        }

        LEdge(LPVertex from, LPVertex to, int weight, LEdge ori){
            super(from, to, weight);
            mapEdge = ori;
        }

        boolean isDisabled(){
            LPVertex lFrom = (LPVertex) from;
            LPVertex lTo = (LPVertex) to;
            return disabled || lFrom.isDisabled() || lTo.isDisabled();
        }
    }

    LPVertex[] lv; // vertices of graph
    int curLoad, maxLoad, disCount; // additional parameters for this graph
    public LPGraph(Graph g){
        super(g);
        lv = new LPVertex[2 * g.size()];
        // make copy of nodes
        for (Vertex u : g){
            lv[u.getName()] = new LPVertex(u);
        }
        // make copy of edges
        for (Vertex u : g){
            // Copy out going edges
            LPVertex x1 = getVertex(u);
            for (Edge e : u){
                Vertex v = e.otherEnd(u);
                LPVertex x2 = getVertex(v);
                x1.lAdj.add(new LEdge(x1, x2, e.weight));
            }
            // Copy incoming edges
            Iterator iter = u.reverseIterator();
            while(iter.hasNext()){
                Edge e = (Edge) iter.next();
                Vertex v = e.otherEnd(u);
                LPVertex x2 = getVertex(v);
                x1.lRevAdj.add(new LEdge(x2, x1, e.weight));
            }
        }

        // Set parameters
        curLoad = g.size();
        maxLoad = 2 * curLoad;
        disCount = 0;
    }

    @Override
    public Vertex getVertex(int n) {
        return lv[n-1];
    }

    public LPVertex getVertex(Vertex u){
        return Vertex.getVertex(lv, u);
    }

    public int enableSize(){
        return n - disCount;
    }
}
