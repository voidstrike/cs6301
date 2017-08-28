/** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan : based on Professor's graph.java
 *  Ver 1.0: 2017/08/27
 */

package cs6301.g42;

import cs6301.g00.Graph;

import java.util.Queue;
//import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

//import Graph;

public class Sp1qestion2 {

	/*
	 * implement BFS to find the diameter of the tree 1. select a random node A
	 * and run BFS to find furthermost node from A, name it S 2. then run BFS
	 * from S, find furthermost node from S, name it D. 3. diameter is S to D.
	 */
	public static LinkedList<Graph.Vertex> diameter(Graph g) {
		LinkedList<Graph.Vertex> result = new LinkedList<>();

		Queue<Graph.Vertex> queue = new LinkedList<>();

		Graph.Vertex root = g.getVertex(0); // select a random node A to run
											// BFS, here just pick index 0
		queue.add(root);

		while (!queue.isEmpty()) {
			// dequeue a vertex from queue
			Graph.Vertex s = queue.poll();

			// get all adj vertices of vertex s
			// if a adj has not been visited, mark it visited and enqueue it
			Iterator<Graph.Edge> iter = s.iterator();
			while (iter.hasNext()) {
				Graph.Edge next = iter.next();
				Graph.Vertex to = next.otherEnd(s);
				queue.add(to);
			}

			// if queue is empty after this step, then the previous s
			// is the furthermost point from root
			if (queue.isEmpty()) {
				root = s;
			}

		}

		// step 2 start from here.
		// run BFS start from S, find furthermost node from S
		queue.clear();
		queue.add(root); // node s from step 1

		HashMap<Graph.Vertex, Graph.Vertex> parent = new HashMap<>(); // < child vertex, parent vertex>
		while (!queue.isEmpty()) {
			// dequeue a vertex from queue
			Graph.Vertex d = queue.poll();

			// get all adj vertices of vertex d(parent)
			// if a adj has not been visited, mark it visited and enqueue it
			Iterator<Graph.Edge> iter = d.iterator();
			while (iter.hasNext()) {
				Graph.Edge next = iter.next();
				Graph.Vertex to = next.otherEnd(d);
				parent.put(to, d); // put the parent of 'to' in the hashmap
				queue.add(to);
			}

			// if queue is empty after this step, then the 'd' that just poll
			// out of the queue
			// is the furthermost point from root
			// do the backtrace
			if (queue.isEmpty()) {
				// root = d;
				result.add(d);
				while (root != result.getFirst()) {
					result.add(0, parent.get(d));
				}
			}

		}

		return result;
	}

}
