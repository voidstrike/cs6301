package cs6301.g42;

import cs6301.g00.*;

import java.util.Iterator;
import java.util.Scanner;

public class CheckDAG {
    public static boolean isDAG(Graph g){
    	boolean visited[] = new boolean[g.size()];
    	boolean recStack[] = new boolean[g.size()];
    	for(int i = 0; i < g.size(); i++){
    		if(visited[i] == false){
    			if( isCyclic(i, visited, recStack, g) == true){
    				return true;
    			}
    		}
    	}
        return false;
    }
    
    public static boolean isCyclic(int vertex, boolean[] visited, boolean[] recStack, Graph g){
    	// mark current node as visited and add it to recursion stack
    	visited[vertex] = true;
    	recStack[vertex] = true;
    	Graph.Vertex current = g.getVertex(vertex+1);
    	// run the dfs
    	Iterator<Graph.Edge> iter = current.iterator();
    	while (iter.hasNext()) {
            Graph.Edge next = iter.next(); 
            Graph.Vertex to = next.otherEnd(current);
            
            if(visited[to.getName()] == false){
            	if( isCyclic(to.getName(), visited, recStack, g) == true)
            		return true;
            }else if( recStack[to.getName()] == true){
            	return true;
            }
        }
    	 	
    	recStack[vertex] = false;
    	return false;
    }
    
    public static void main(String args[]){

    	Graph graph;
    	Scanner sc = new Scanner(System.in);

        System.out.println("Enter number of vertices and edges:");

        graph = Graph.readDirectedGraph(sc);
        	
        System.out.println("Running isGAG funciton...");
        	
        if( isDAG(graph) == true){
        	System.out.println("Graph has a cycle (The graph is not a DAG)");
        }else{
        	System.out.println("Graph has no cycle (The graph is a DAG)");
        }
        	
    	sc.close();
    }
}
