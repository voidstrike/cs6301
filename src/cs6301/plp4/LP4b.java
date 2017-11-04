package cs6301.plp4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Alan Lin on 11/3/2017.
 */
public class LP4b {
    static int VERBOSE = 0;
    public static void main(String[] args) throws FileNotFoundException{
        if(args.length > 0) { VERBOSE = Integer.parseInt(args[0]); }
        System.setIn(new FileInputStream("F:\\TestGraph\\lp4-data\\lp4-0-abcde.txt"));
        Graph g = Graph.readDirectedGraph(new java.util.Scanner(System.in));
        cs6301.g00.Timer t = new cs6301.g00.Timer();
        LP4 handle = new LP4(g, null);
        long result = handle.enumerateTopologicalOrders();
        if(VERBOSE > 0) { LP4.printGraph(g, null, null, null, 0); }
        System.out.println(result + "\n" + t.end());
    }
}