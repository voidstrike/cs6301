/** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan : based on Professor's graph.java
 *  Ver 1.0: 2017/08/27
 */

package cs6301.g42;

import cs6301.g00.Graph;
import java.util.Queue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Sp1qestion2ver2 {

    /*
     * implement BFS to find the diameter of the tree 
     * 1. select a random node A and run BFS to find furthermost node from A, name it S 
     * 2. then run BFS from S, find furthermost node from S, name it D. 
     * 3. diameter is S to D.
     */
    public static LinkedList<Graph.Vertex> diameter(Graph g) {
    	
    	//System.out.println("running diameter()...");
    	
    	boolean visited[] = new boolean[g.size()];
    	
        LinkedList<Graph.Vertex> result = new LinkedList<>();

        Queue<Graph.Vertex> queue = new LinkedList<>();

        Graph.Vertex root = g.getVertex(1); // select a random node A to run BFS, here just pick index 0
        queue.add(root);

        while (!queue.isEmpty()) {
            // dequeue a vertex from queue
            Graph.Vertex s = queue.poll();

            visited[s.getName()] = true;

            // get all adj vertices of vertex s
            // if a adj has not been visited, mark it visited and enqueue it
            Iterator<Graph.Edge> iter = s.iterator();
            while (iter.hasNext()) {
                Graph.Edge next = iter.next(); 
                Graph.Vertex to = next.otherEnd(s);
                if(visited[to.getName()] == false){
                	visited[to.getName()] = true;
                    queue.add(to);
                }
            }

            // if queue is empty after this step, then the previous s
            // is the furthermost point from root
            if (queue.isEmpty()) {
                root = s;
            }

        }
        //System.out.println("step 2:");
        // step 2 start from here.
        // run BFS start from S, find furthermost node from S
        
        queue.clear();
        queue.add(root); // node s from step 1
        boolean visit[] = new boolean[g.size()];
        // use hashmap to trace the parent vertex of current vertex
        HashMap<Graph.Vertex, Graph.Vertex> parent = new HashMap<>(); // < child vertex, parent vertex>
        while (!queue.isEmpty()) {
            // dequeue a vertex from queue
            Graph.Vertex d = queue.poll();
            visit[d.getName()] = true;
            // get all adj vertices of vertex d(parent)
            // if a adj has not been visited, mark it visited and enqueue it
            Iterator<Graph.Edge> iter = d.iterator();
            while (iter.hasNext()) {
                Graph.Edge next = iter.next();
                Graph.Vertex to = next.otherEnd(d);
                if(visit[to.getName()] == false){
                	visit[to.getName()] = true;
                    parent.put(to, d); // put the parent of 'to' in the hashmap
                    queue.add(to);
                }
            }

            // if queue is empty after this step, then the 'd' that just poll out of the queue
            // is the furthermost point from root
            // do the backtrace
            
            if (queue.isEmpty()) {
                // root = d;
            	//System.out.println("backtrace");
                result.add(d);
                while (root != result.getFirst()) {
                    result.add(0, parent.get(d));
                    d = parent.get(d);
                }
            }

        }

        return result;
    }
    
    public static void main(String[] args) {
    	boolean directed = false;
    	
    	Scanner in = new Scanner(System.in);
    	//Graph.readGraph(in, directed);
    	
    	LinkedList<Graph.Vertex> result = new LinkedList<>();
    	System.out.println("running diameter()...");
        result = diameter(Graph.readGraph(in, directed));
    	//result = diameter(g);
    	
    	System.out.println("printing result:");
    	System.out.println(result.toString());
    	/*while( result != null){
    		System.out.print(result.getFirst().toString() + "->");
    		result = result.getNext();
    		
    	}*/
    }
    
}
