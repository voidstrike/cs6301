
package cs6301.g42;

public class GraphAlgorithm<T> {
	public Graph g; // changed to public
	// Algorithm uses a parallel array for storing information about vertices
	public T[] node; // changed to public

	public GraphAlgorithm(Graph g) {
		this.g = g;
	}

	public T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}
}
