package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import graph.Edge;
import graph.Graph;
import graph.Vertex;

public class PlyGraphGenerator{
		
//	Map<Integer, Double> circlesRadiiMap;
		
	public PlyGraphGenerator(){
		
//		this.circlesRadiiMap = new HashMap<Integer, Double>();		
		
	}


	private void computePlyCircles(Graph graph, double radiusRatio){
				
		this.computeEdgesDistances(graph);
		
//		this.circlesRadiiMap = new HashMap<Integer, Double>();	
		
		for(Vertex currVertex : graph.getVertices()){
						
			double maxRadiusLength = 0;
			
			Set<Edge> adjEdges = graph.getIncidentEdges(currVertex);
			
			for(Edge currEdge : adjEdges){
				double dist = currEdge.getLenth();

				maxRadiusLength = Math.max(maxRadiusLength, dist*radiusRatio);
			}
			
			currVertex.circleRadius = maxRadiusLength;
//			this.circlesRadiiMap.put(currVertex.identifier, maxRadiusLength);
			
			
		}
		
	}


	public Graph generatePlyGraph(Graph graph, double radiusRatio){
		
		computePlyCircles(graph, radiusRatio);

		Map<Integer, Vertex> verticesMap = graph.getVerticesMap();
		Map<Integer, Edge> edgesMap = new HashMap<Integer, Edge>();

			
		ArrayList<Vertex> orderedVerticesList = new ArrayList<Vertex>(verticesMap.values());
		
		int edgeId = 0;
		
		for(int i=0; i<orderedVerticesList.size(); i++){
			
			Vertex firstVertex = orderedVerticesList.get(i);
			
			for(int j=i+1; j<orderedVerticesList.size(); j++){
				
				Vertex secondVertex = orderedVerticesList.get(j);
				
				if(doCirclesIntesect(firstVertex, secondVertex)){
										
					Edge currIntersectionEdge = new Edge(edgeId, firstVertex.identifier, secondVertex.identifier);
					edgesMap.put(edgeId, currIntersectionEdge);
					edgeId++;
						
				}
		
			}
			
		}
		
		Graph plyGraph = new Graph(verticesMap, edgesMap);
		
		return plyGraph;
	}
	
	private boolean doCirclesIntesect(Vertex v1, Vertex v2){
		
		double distance = distance(v1, v2);
		
//		double v1CircleRadius = this.circlesRadiiMap.get(v1.identifier);
//		double v2CircleRadius = this.circlesRadiiMap.get(v2.identifier);
		
		double v1CircleRadius = v1.circleRadius;
		double v2CircleRadius = v2.circleRadius;
		
		return ((v1CircleRadius+v2CircleRadius)>distance);
		
	}
	
	
	//ComputeDistances
	
	private void computeEdgesDistances(Graph graph){
		
		Set<Edge> edges = graph.getEdges();
		Map<Integer, Vertex> verticesMap = graph.getVerticesMap();
		
	
		for(Edge edge : edges){
			
			Vertex source = verticesMap.get(edge.getSourceIdentifier());
			Vertex target = verticesMap.get(edge.getTargetIdentifier());
			
			double currDist = this.distance(source, target);
			edge.setLength(currDist);
			
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
