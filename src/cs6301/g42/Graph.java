/** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan : based on Professor's graph.java
 *  Ver 1.0: 2017/08/27
 */


package cs6301;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Scanner;

//Implement BFS, find the diameter of a tree (should be undirected graph) 


public class Graph implements Iterable<Graph.Vertex> {
	Vertex[] v; // vertices of graph
	int n; // number of verices in the graph
	boolean directed; // true if graph is directed, false otherwise

	/**
	 * Nested class to represent a vertex of a graph
	 */

	public static class Vertex implements Iterable<Edge> {
		int name; // name of the vertex
		List<Edge> adj, revAdj; // adjacency list; use LinkedList or ArrayList

		/**
		 * Constructor for the vertex
		 * 
		 * @param n
		 *            : int - name of the vertex
		 */
		Vertex(int n) {
			name = n;
			adj = new LinkedList<Edge>();
			revAdj = new LinkedList<Edge>(); /* only for directed graphs */
		}

		public Iterator<Edge> iterator() {
			return adj.iterator();
		}

		/**
		 * Method to get vertex number. +1 is needed because [0] is vertex 1.
		 */
		public String toString() {
			return Integer.toString(name + 1);
		}
	}

	/**
	 * Nested class that represents an edge of a Graph
	 */

	public static class Edge {
		Vertex from; // head vertex
		Vertex to; // tail vertex
		int weight;// weight of edge

		/**
		 * Constructor for Edge
		 * 
		 * @param u
		 *            : Vertex - Vertex from which edge starts
		 * @param v
		 *            : Vertex - Vertex on which edge lands
		 * @param w
		 *            : int - Weight of edge
		 */
		Edge(Vertex u, Vertex v, int w) {
			from = u;
			to = v;
			weight = w;
		}

		/**
		 * Method to find the other end end of an edge, given a vertex reference
		 * This method is used for undirected graphs
		 * 
		 * @param u
		 *            : Vertex
		 * @return : Vertex - other end of edge
		 */
		public Vertex otherEnd(Vertex u) {
			assert from == u || to == u;
			// if the vertex u is the head of the arc, then return the tail else
			// return the head
			if (from == u) {
				return to;
			} else {
				return from;
			}
		}

		/**
		 * Return the string "(x,y)", where edge goes from x to y
		 */
		public String toString() {
			return "(" + from + "," + to + ")";
		}

		public String stringWithSpaces() {
			return from + " " + to + " " + weight;
		}
	}

	/**
	 * Constructor for Graph
	 * 
	 * @param n
	 *            : int - number of vertices
	 */
	Graph(int n) {
		this.n = n;
		this.v = new Vertex[n];
		this.directed = false; // default is undirected graph
		// create an array of Vertex objects
		for (int i = 0; i < n; i++)
			v[i] = new Vertex(i);
	}

	/**
	 * Find vertex no. n
	 * 
	 * @param n
	 *            : int
	 */
	Vertex getVertex(int n) {
		return v[n - 1];
	}

	/**
	 * Method to add an edge to the graph
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	void addEdge(Vertex from, Vertex to, int weight) {
		Edge e = new Edge(from, to, weight);
		if (this.directed) {
			from.adj.add(e);
			to.revAdj.add(e);
		} else {
			from.adj.add(e);
			to.adj.add(e);
		}
	}

	int size() {
		return n;
	}

	/**
	 * Method to create iterator for vertices of graph
	 */
	public Iterator<Vertex> iterator() {
		return new ArrayIterator<Vertex>(v);
	}

	// read a directed graph using the Scanner interface
	public static Graph readDirectedGraph(Scanner in) {
		return readGraph(in, true);
	}

	// read an undirected graph using the Scanner interface
	public static Graph readGraph(Scanner in) {
		return readGraph(in, false);
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph

		// create a graph instance
		Graph g = new Graph(n);
		g.directed = directed;
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			g.addEdge(g.getVertex(u), g.getVertex(v), w);
		}
		return g;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	/*     implement BFS to find the diameter of the tree
	 *     1. select a random node A and run BFS to find furthermost node from A, name it S
	 *     2. then run BFS from S, find furthermost node from S, name it D.
	 *     3. diameter is S to D.
	 */
	public static LinkedList<Graph.Vertex> diameter(Graph g){
		LinkedList<Graph.Vertex> result = new LinkedList<>();
		
		Queue<Graph.Vertex> queue = new LinkedList<>();
		
		Graph.Vertex root = g.getVertex(0); //select a random node A to run BFS, here just pick index 0
		queue.add(root);
		
		while(!queue.isEmpty()){
			// dequeue a vertex from queue
			Graph.Vertex s = queue.poll();
			
			// get all adj vertices of vertex s
			// if a adj has not been visited, mark it visited and enqueue it
			Iterator<Edge> iter = s.iterator();
			while(iter.hasNext()){
				Graph.Edge next = iter.next();
				Graph.Vertex to = next.otherEnd(s);
				queue.add(to);
			}
			
			// if queue is empty after this step, then the previous s 
			// is the furthermost point from root
			if(queue.isEmpty()){
				root = s;
			}
			
		}
		
		
		// step 2 start from here.
		// run BFS start from S, find furthermost node from S
		queue.clear();
		queue.add(root);  // node s from step 1
		while(!queue.isEmpty()){
			// dequeue a vertex from queue
			Graph.Vertex d= queue.poll();
			
			// get all adj vertices of vertex d
			// if a adj has not been visited, mark it visited and enqueue it
			Iterator<Edge> iter = d.iterator();
			while(iter.hasNext()){
				Graph.Edge next = iter.next();
				Graph.Vertex to = next.otherEnd(d);
				queue.add(to );
			}
			
			// if queue is empty after this step, then the previous d
			// is the furthermost point from root
			if(queue.isEmpty()){
				root = d;
			}
			
		}
		
		
		return result;
	}

}
