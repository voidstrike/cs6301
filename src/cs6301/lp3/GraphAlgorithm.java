
package cs6301.lp3;

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
