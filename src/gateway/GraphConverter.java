package gateway;

import java.util.Set;

import plygraph.Edge;
import plygraph.Graph;

public class GraphConverter {
	
	public static byte[][] convertToAdjMatrix(Graph graph){
		
		int numVertices = graph.getVertices().size();
		
		byte[][] adjMatrix = new byte[numVertices][numVertices];
		
		Set<Edge> edges = graph.getEdges();
		
		for (Edge e : edges){
			
			int source = e.getSourceIdentifier();
			int target = e.getTargetIdentifier();
			
			adjMatrix[source][target] = 1;
			
			if (!e.getIsDirected()){
				adjMatrix[target][source] = 1;
			}
			
			
		}
		
		return adjMatrix;
	
	}

}
