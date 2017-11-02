
package cs6301.plp4;

import cs6301.plp4.Graph;

public class GraphAlgorithm<T> {
	protected Graph g; // changed to protected
	protected T[] node; // changed to protected
	// Algorithm uses a parallel array for storing information about vertices

	public GraphAlgorithm(Graph g) {
		this.g = g;
	}

	public T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}
}
