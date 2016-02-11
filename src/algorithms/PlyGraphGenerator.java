package algorithms;

import graph.Graph;
import graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlyGraphGenerator{
		
	Map<String, Double> circlesRadiiMap;
		
	public PlyGraphGenerator(double radiusRatio){
		
		this.circlesRadiiMap = new HashMap<String, Double>();		
		
	}


	public Graph generatePlyGraph(Graph graph, double radiusRatio){
		
		computePlyCircles(graph, radiusRatio);
	
		Graph plyGraph = new Graph(new HashSet<Vertex>(graph.vertices));
		
		ArrayList<Vertex> orderedVerticesList = new ArrayList<Vertex>(graph.vertices);
		
		for(int i=0; i<orderedVerticesList.size(); i++){
			
			Vertex firstVertex = orderedVerticesList.get(i);
			
			for(int j=i+1; j<orderedVerticesList.size(); j++){
				
				Vertex secondVertex = orderedVerticesList.get(j);
				
				if(doCirclesIntesect(firstVertex, secondVertex)){
					
					plyGraph.setAdjsVertices(firstVertex, secondVertex, false);
					
				}
		
			}
			
		}
		
		
		return plyGraph;
	}
	
	private boolean doCirclesIntesect(Vertex v1, Vertex v2){
		
		double distance = distance(v1, v2);
		
		double v1CircleRadius = this.circlesRadiiMap.get(v1.identifier);
		double v2CircleRadius = this.circlesRadiiMap.get(v2.identifier);
		
		return ((v1CircleRadius+v2CircleRadius)>distance);
		
	}
	
	private void computePlyCircles(Graph graph, double radiusRatio){
		
		this.circlesRadiiMap = new HashMap<String, Double>();	
		
		for(Vertex currVertex : graph.vertices){
			
			double maxRadiusLength = 0;
			
			Set<Vertex> adjVertices = graph.getAdjsOfVertex(currVertex);
			
			for(Vertex currAdj : adjVertices){
				double dist = distance(currVertex, currAdj);
				maxRadiusLength = Math.max(maxRadiusLength, dist*radiusRatio);
			}
			
			this.circlesRadiiMap.put(currVertex.identifier, maxRadiusLength);
			
			
		}
		
	}
	
	private double distance(Vertex firstVertex, Vertex secondVertex){
		
		double x1 = firstVertex.x;
		double y1 = firstVertex.y;
		double x2 = secondVertex.x;
		double y2 = secondVertex.y;
		
		double  dist = Math.sqrt(
		            Math.pow(x1 - x2, 2) +
		            Math.pow(y1 - y2, 2));
		return dist;
	}

}
