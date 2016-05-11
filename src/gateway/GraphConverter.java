package gateway;

import java.util.Set;

import graphap.EdgeAP;
import graphap.GraphAP;

public class GraphConverter {
	
	public static byte[][] convertToAdjMatrix(GraphAP graph){
		
		int numVertices = graph.getVertices().size();
		
		byte[][] adjMatrix = new byte[numVertices][numVertices];
		
		Set<EdgeAP> edges = graph.getEdges();
		
		for (EdgeAP e : edges){
			
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
