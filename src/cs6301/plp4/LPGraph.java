package cs6301.plp4;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alan Lin on 10/15/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class LPGraph extends Graph{
    /**
     * Extended Vertex class */
    public static class LPVertex extends Vertex{
        boolean disabled; // current status of this vertex
        List<LEdge> lAdj; // the list of out coming edges, using class LEdge
        List<LEdge> lRevAdj; // the list of in coming edges, using class LEdge

        // Constructor
        LPVertex(Vertex u){
            super(u);
            disabled = false;
            lAdj = new LinkedList<>();
            lRevAdj = new LinkedList<>();

        }

        // return the status of current node
        boolean isDisabled(){
            return disabled;
        }

        // disable current vertex
        void disable(){
            disabled = true;
        }

        // Enable current vertex
        void enable(){
            disabled = false;
        }

        @Override
        public Iterator<Edge> iterator() {
            // New version of iterator, ignore the disabled Edges, for out going edges
            return new LVertexIterator(this, true);
        }

        @Override
        public Iterator<Edge> reverseIterator() {
            // New version of iterator, ignore the disabled Edges, for in coming edges
            return new LVertexIterator(this, false);
        }

        // New iterator for vertex's edges
        class LVertexIterator implements Iterator<Edge>{
            LEdge cur;
            Iterator<LEdge> it;
            boolean ready;

            LVertexIterator(LPVertex u, boolean mode){
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
                cur = it.next();
                while(cur.isDisabled() && it.hasNext()){
                    cur = it.next();
                }
                ready = true;
                return !cur.isDisabled();
            }

            @Override
            public Edge next() {
                if(!ready){
                    if (!hasNext()){
                        throw new java.util.NoSuchElementException();
                    }
                }
                ready = false;
                return cur;
            }
        }
    }

    /**
     * Extended Edge class */
    static class LEdge extends Edge{
        boolean disabled;
        LEdge mapEdge;

        // Constructors
        LEdge(LPVertex from, LPVertex to, int weight){
            super(from, to, weight);
            disabled = false;
            mapEdge = null;
        }

        // Alternative constructor, allow mapping edge specification
        LEdge(LPVertex from, LPVertex to, int weight, LEdge ori){
            this(from, to, weight);
            mapEdge = ori;
        }

        boolean isDisabled(){
            LPVertex xFrom = (LPVertex) from;
            LPVertex xTo = (LPVertex) to;
            return disabled || xFrom.isDisabled() || xTo.isDisabled();
        }

        void setMapEdge(LEdge ori){
            mapEdge = ori;
        }

        boolean hasMapEdge(){
            return mapEdge != null;
        }

        void disable(){
            disabled = true;
        }

        void enable(){
            disabled = false;
        }
    }

    private LPVertex[] lv; // vertices of graph
    private int currentNodes, maxLoad;
    private int disCount;

    // Extended Graph constructor
    public LPGraph(Graph g){
        super(g);
        lv = new LPVertex[2 * g.size()]; // Extra space is allocate in array for nodes to be added later
        currentNodes = g.size();
        maxLoad = 2 * currentNodes;
        disCount = 0;
        for(Vertex u : g){
            lv[u.getName()] = new LPVertex(u);
        }

        // Make copy of edges
        for (Vertex u : g){
            for (Edge e : u){
                Vertex v = e.otherEnd(u);
                LPVertex x1 = getVertex(u);
                LPVertex x2 = getVertex(v);
                LEdge tmp = new LEdge(x1, x2, e.weight);
                x1.lAdj.add(tmp);
                x2.lRevAdj.add(tmp);
            }
        }
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new LPGraphIterator(this);
    }

    class LPGraphIterator implements Iterator<Vertex>{
        Iterator<LPVertex> it;
        LPVertex xCur;

        LPGraphIterator(LPGraph lg){
            this.it = new ArrayIterator<>(lg.lv, 0, lg.size() - 1);
        }

        @Override
        public boolean hasNext() {
            if (!it.hasNext())
                return false;
            xCur = it.next();
            while(xCur.isDisabled() && it.hasNext())
                xCur = it.next();

            return !xCur.isDisabled();
        }

        @Override
        public Vertex next() {
            return xCur;
        }
    }

    @Override
    public Vertex getVertex(int n) {
        return lv[n-1];
    }

    LPVertex getVertex(Vertex u){
        return Vertex.getVertex(lv, u);
    }

    void disable(int i){
        LPVertex u = (LPVertex) getVertex(i);
        disCount++;
        u.disable();
    }

    void disableVertex(Vertex u){
        LPVertex tmp = getVertex(u);
        disCount++;
        tmp.disable();
    }

    // Add new vertex to this graph
    public void addVertex(Vertex u){
        currentNodes++;
        lv[n++] = new LPVertex(u);
    }

    public int availableSize(){
        return n-disCount;
    }

    /** Method to enable all nodes before nodePivot and disable all nodes after that*/
    public void patchReset(int nodePivot){
        // input the name of endNode
        for(int i=0; i<nodePivot; i++)
            lv[i].enable();
        for(int i = nodePivot; i<n; i++)
            lv[i].disable();
        disCount = n - nodePivot + 1;
    }

    public void resetGraph(){
        LPVertex tmp;
        for(int i=0; i<currentNodes; i++){
            tmp = lv[i];
            tmp.enable();
            for(LEdge e : tmp.lAdj)
                e.enable();
        }
    }

    @Override
    public Edge addEdge(Vertex from, Vertex to, int weight, int name) {
        Edge addedE = super.addEdge(from, to, weight, name);
        LEdge tmp = new LEdge(getVertex(from), getVertex(to), weight);
        if (directed){
            getVertex(from).lAdj.add(tmp);
            getVertex(to).lRevAdj.add(tmp);
        }
        else{
            getVertex(from).lAdj.add(tmp);
            getVertex(to).lAdj.add(tmp);
        }
        return addedE;
    }

    public Edge addEdge(Vertex from, Vertex to, int weight, LEdge ori){
        Edge addedE = super.addEdge(from, to, weight);
        LEdge tmp = new LEdge(getVertex(from), getVertex(to), weight, ori);
        if (directed){
            getVertex(from).lAdj.add(tmp);
            getVertex(to).lRevAdj.add(tmp);
        }
        else{
            getVertex(from).lAdj.add(tmp);
            getVertex(to).lAdj.add(tmp);
        }
        return addedE;
    }

    public static void main(String[] args)
            throws FileNotFoundException {
        //Test code below
    }
}
