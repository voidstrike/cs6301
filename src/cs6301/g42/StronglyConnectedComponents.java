/**
 *  Short Project 3, Q2
 *  Group: 42
 */

package cs6301.g42;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import cs6301.g42.Graph.Vertex;

//import cs6301.g00.Graph;
//import cs6301.g00.GraphAlgorithm;
//import cs6301.g00.Graph.Vertex;

public class StronglyConnectedComponents extends GraphAlgorithm<StronglyConnectedComponents.SCCVertex>{
	
	static class SCCVertex {
		Graph.Vertex element;
		boolean seen;
		int dis; // dfs discovery time
		int cno; // connected component number
		int fin; // dfs finish time
		Graph.Vertex parent;

		public SCCVertex(Graph.Vertex u) {
			this.element = u;
			this.seen = false;
			this.dis = 0;
			this.cno = 0;
			this.fin = 0;
			this.parent = null;
		}
		
		public String toString(){
			String str = "Vertex: " + element + ", "
					     + "dis: " + dis + ", "
					     + "cno: " + cno + ", "
					     + "fin: " + fin;
			return str;
		}
	}

	int time;
	
	public StronglyConnectedComponents(Graph g) {
		super(g);
		node = new SCCVertex[g.size()]; // from parent class
												// GraphAlgorithm
		for (Graph.Vertex u : g) {
			node[u.getName()] = new SCCVertex(u);
		}
		time = 0;
	}
	
	int stronglyConnectedComponents() {
		Iterator<Vertex> it = g.iterator();
		
		// To find SCC in a directed graph:
		// 1- Run DFS to find the finish time order
		List<Graph.Vertex> decFinList = new LinkedList<>();
		int cno = 0;
		DFS(it, cno, decFinList);
        
		// 2- Reverse edges: Exchange (swap) adj and revAdj of each vertex
		reverseEdges(g);
		
		// 3- Run DFS again in decreasing finish time order
		//    (i.e., iterate over decFinList)
		Iterator<Vertex> itDecFinList = decFinList.iterator();
		List<Graph.Vertex> decFinListReverse = new LinkedList<>();
		time = 0;
		cno = 0;
		reinitialize(); // reset verticies
		int numSCC = DFS(itDecFinList, cno, decFinListReverse);

		return numSCC;
	}
	
	int DFS(Iterator<Vertex> it, int cno, List<Graph.Vertex> decFinList){
		while (it.hasNext()){
			Graph.Vertex u = it.next();
			if (!seen(u)){
				cno++;
				DFSVisit(u, cno, decFinList);
			}
		}
		
		return cno;
	}
	
	void DFSVisit(Graph.Vertex u, int cno, List<Graph.Vertex> decFinList) {
		visit1(u, cno); // set u.seen, u.dis, u.cno
		//System.out.println(getVertex(u));
		for (Graph.Edge e : u.adj) {
			Graph.Vertex v = e.otherEnd(u);
			if (!seen(v)) { // here we check the parallel graph DFSVertex[],
							// which is node[] in the parent class.
				//setParent(u, v);
				DFSVisit(v, cno, decFinList);
			} 
		}
		visit2(u); // set u.fin, u.top
		//System.out.println(getVertex(u));
		decFinList.add(0, u); // addFirst
	}
	
	// set u.seen, u.dis, u.cno
	void visit1(Graph.Vertex u, int cno) {
		SCCVertex scc2u = getVertex(u);
		scc2u.seen = true;
		scc2u.dis = ++time;
		scc2u.cno = cno;
	}

	// set u.fin, u.top
	void visit2(Graph.Vertex u) {
		SCCVertex scc2u = getVertex(u);
		scc2u.fin = ++time;
	}

	void setParent(Graph.Vertex parent, Graph.Vertex child) {
		SCCVertex scc2u = getVertex(child); // remember: every time we
														// need to map to the
														// parallel graph
		scc2u.parent = parent;
	}
	
	// reinitialize
	void reinitialize() {
		for (Graph.Vertex u : g) {
			SCCVertex scc2u = getVertex(u);
			scc2u.seen = false;
			scc2u.dis = 0;
			scc2u.cno = 0;
			scc2u.fin = 0;
			scc2u.parent = null;
		}
	}

	boolean seen(Graph.Vertex u) {
		SCCVertex scc2u = getVertex(u);
		return scc2u.seen;
	}
	
	void reverseEdges(Graph g){
		Iterator<Vertex> it = g.iterator();
		while (it.hasNext()){
			Graph.Vertex u = it.next();
			
			// swap adj and revAdj for each vertex
			// to reverse edges
			List<Graph.Edge> tempAdj = u.adj;
			u.adj = u.revAdj;
			u.revAdj = tempAdj;
		}
	}
	
	public static int stronglyConnectedComponents(Graph g) {
		StronglyConnectedComponents scc = new StronglyConnectedComponents(g);
		return scc.stronglyConnectedComponents();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		if (args.length > 0) {
			String fileName = System.getProperty("user.dir") + "/graphFiles/" + args[0];
			System.out.println("File name: " + fileName);
			File inputFile = new File(fileName);
			in = new Scanner(inputFile);
		} else {
			in = new Scanner(System.in);
		}
		Graph g = Graph.readDirectedGraph(in);
		
		//g.printGraph();
		
		int numSCC = stronglyConnectedComponents(g);

		System.out.println("Number of SCC: " + numSCC);

		System.out.println();
	}

}
