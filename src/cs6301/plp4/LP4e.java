package cs6301.plp4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Alan Lin on 11/3/2017.
 */
public class LP4e {
    static int VERBOSE = 0;
    public static void main(String[] args) throws FileNotFoundException{
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        System.setIn(new FileInputStream("F:\\TestGraph\\lp4-data\\lp4-9-acde.txt"));
        java.util.Scanner in = new java.util.Scanner(System.in);
        Graph g = Graph.readDirectedGraph(in);
        int source = in.nextInt();
        int target = in.nextInt();
        int maxEdges = in.nextInt();
        cs6301.g00.Timer t = new cs6301.g00.Timer();
        LP4 handle = new LP4(g, g.getVertex(source));
        int result = handle.constrainedShortestPath(g.getVertex(target), maxEdges);
        if(VERBOSE > 0) { LP4.printGraph(g, null, g.getVertex(source), null, maxEdges); }
        System.out.println(result + "\n" + t.end());
    }
}