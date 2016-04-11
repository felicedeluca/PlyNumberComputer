package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import linesweep.Circle;
import utilities.Configurator;

public class PlyGraphGenerator{
		
//	Map<Integer, Double> circlesRadiiMap;
		
	public PlyGraphGenerator(){
		
//		this.circlesRadiiMap = new HashMap<Integer, Double>();		
		
	}


	public Set<Circle> computePlyCircles(Graph graph, Apfloat radiusRatio){
				
		this.computeEdgesDistances(graph);
		
		Set<Circle> circles = new HashSet<Circle>();
		
		Apfloat squaredRadiusRatio = ApfloatMath.pow(radiusRatio, 2);
				
		for(Vertex currVertex : graph.getVertices()){
						
			Apfloat maxSquaredRadiusLength = new Apfloat("0");
			
			Set<Edge> adjEdges = graph.getIncidentEdges(currVertex);
			
			for(Edge currEdge : adjEdges){
				
				Apfloat squaredLength = currEdge.getSquaredLength();
				Apfloat currSquaredRadius = squaredLength.multiply(squaredRadiusRatio);
				
				maxSquaredRadiusLength = (maxSquaredRadiusLength.compareTo(currSquaredRadius)== 1) ? maxSquaredRadiusLength : currSquaredRadius;
				// ApfloatMath.max(maxRadiusLength, currRadius);
			}
			
			currVertex.setSquaredCircleRadius(maxSquaredRadiusLength);
//			this.circlesRadiiMap.put(currVertex.identifier, maxRadiusLength);
			
			Circle c = new Circle(currVertex.identifier+"", currVertex.label, currVertex.x, currVertex.y, currVertex.getSquaredCircleRadius());
			//System.out.println(currVertex.x + " " + currVertex.y);
			circles.add(c);
			
		}
		
		
		circles = new HashSet<Circle>();

		Circle a = new Circle("1", "A", new Apfloat("0.0", Configurator.apfloatPrecision()), new Apfloat("-1.0", Configurator.apfloatPrecision()),new Apfloat("1.0", Configurator.apfloatPrecision()));
		Circle b = new Circle("2", "B", new Apfloat("2.0", Configurator.apfloatPrecision()), new Apfloat("0.0", Configurator.apfloatPrecision()),new Apfloat("4.0", Configurator.apfloatPrecision()));
		Circle c = new Circle("3", "C", new Apfloat("0.0", Configurator.apfloatPrecision()), new Apfloat("1.0", Configurator.apfloatPrecision()),new Apfloat("1.0", Configurator.apfloatPrecision()));
	
		circles.add(a);
		circles.add(b);
		circles.add(c);
		
		
		
		return circles;
		
	}


	public Graph generatePlyIntersectionGraph(Graph graph, Apfloat radiusRatio){
		
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
		
		Apfloat squaredDistance = squaredDistance(v1, v2);
		Apfloat distance = ApfloatMath.sqrt(squaredDistance);
		
		Apfloat v1CircleRadius = v1.getCircleRadius();
		Apfloat v2CircleRadius = v2.getCircleRadius();
		
		Apfloat radiiSum = ApfloatMath.sum(v1CircleRadius, v2CircleRadius);
		
		int compare = (radiiSum.compareTo(distance));
		
		return (compare > 0);
		
	}
	
	
	//ComputeDistances
	
	private void computeEdgesDistances(Graph graph){
		
		Set<Edge> edges = graph.getEdges();
		Map<Integer, Vertex> verticesMap = graph.getVerticesMap();
		
	
		for(Edge edge : edges){
			
			Vertex source = verticesMap.get(edge.getSourceIdentifier());
			Vertex target = verticesMap.get(edge.getTargetIdentifier());
			
			Apfloat currSquaredDistance = squaredDistance(source, target);
			edge.setSquaredLength(currSquaredDistance);		
		}
	
	}
	
	private Apfloat squaredDistance(Vertex firstVertex, Vertex secondVertex){
		
		Apfloat x1 = firstVertex.x;
		Apfloat y1 = firstVertex.y;
		Apfloat x2 = secondVertex.x;
		Apfloat y2 = secondVertex.y;
			
		Apfloat xDist = ApfloatMath.pow(ApfloatMath.abs(x1.subtract(x2)), 2);
		Apfloat yDist = ApfloatMath.pow(ApfloatMath.abs(y1.subtract(y2)), 2);

		
		Apfloat squaredDist = xDist.add(yDist);
		//Apfloat  dist = ApfloatMath.sqrt(xDist.add(yDist));
		
		return squaredDist;
	}

}
