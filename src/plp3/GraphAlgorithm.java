
package plp3;


public class GraphAlgorithm<T, G extends Graph> {
	protected G g; // changed to protected
	protected T[] node; // changed to protected
	// Algorithm uses a parallel array for storing information about vertices

	public GraphAlgorithm(G g) {
		this.g = g;
	}

	public T getVertex(Graph.Vertex u) {
		return Graph.Vertex.getVertex(node, u);
	}
}
