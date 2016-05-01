package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;
import plygraph.Edge;
import plygraph.Graph;
import plygraph.Vertex;
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
			//if(currVertex.getSquaredCircleRadius().compareTo(Configurator.zero)==0) continue;
			Circle c = new Circle(currVertex.identifier+"", currVertex.label, currVertex.x, currVertex.y, currVertex.getSquaredCircleRadius());
			//System.out.println(currVertex.x + " " + currVertex.y);
			circles.add(c);
			
		}
		
		/*

		Circle a = new Circle("1", "A", new Apfloat("3", Configurator.apfloatPrecision()), new Apfloat("1", Configurator.apfloatPrecision()),new Apfloat("1.25", Configurator.apfloatPrecision()));
		Circle b = new Circle("2", "B", new Apfloat("5", Configurator.apfloatPrecision()), new Apfloat("2", Configurator.apfloatPrecision()),new Apfloat("1.26", Configurator.apfloatPrecision()));
		Circle c = new Circle("3", "C", new Apfloat("1", Configurator.apfloatPrecision()), new Apfloat("0", Configurator.apfloatPrecision()),new Apfloat("11.23921", Configurator.apfloatPrecision()));
		Circle d = new Circle("4", "D", new Apfloat("3.96", Configurator.apfloatPrecision()), new Apfloat("1.56", Configurator.apfloatPrecision()),new Apfloat("0.0005", Configurator.apfloatPrecision()));
		Circle e = new Circle("5", "E", new Apfloat("0", Configurator.apfloatPrecision()), new Apfloat("8", Configurator.apfloatPrecision()),new Apfloat("57", Configurator.apfloatPrecision()));
		Circle f = new Circle("6", "F", new Apfloat("14.9", Configurator.apfloatPrecision()), new Apfloat("-15.2", Configurator.apfloatPrecision()),new Apfloat("400", Configurator.apfloatPrecision()));
		Circle g = new Circle("7", "G", new Apfloat("3.972", Configurator.apfloatPrecision()), new Apfloat("1.55", Configurator.apfloatPrecision()),new Apfloat("0.00000500000003", Configurator.apfloatPrecision()));
		Circle h = new Circle("8", "h", new Apfloat("3.9745", Configurator.apfloatPrecision()), new Apfloat("1.548", Configurator.apfloatPrecision()),new Apfloat("0.00000500000003", Configurator.apfloatPrecision()));
		Circle j = new Circle("9", "j", new Apfloat("3.974833", Configurator.apfloatPrecision()), new Apfloat("1.548", Configurator.apfloatPrecision()),new Apfloat("0.00000500000003", Configurator.apfloatPrecision()));
		Circle k = new Circle("9", "k", new Apfloat("3.9703683120019", Configurator.apfloatPrecision()), new Apfloat("1.54769882046482", Configurator.apfloatPrecision()),new Apfloat("0.00000500000003", Configurator.apfloatPrecision()));

		circles.add(a);
		circles.add(b);
		circles.add(c);
		circles.add(d);
		circles.add(e);
		circles.add(f);
		circles.add(g);
		circles.add(h);
		circles.add(j);
		circles.add(k);
		
			
		
		circles = new HashSet<Circle>();
		
		Circle a = new Circle("1", "A", new Apfloat("2", Configurator.apfloatPrecision()), new Apfloat("0", Configurator.apfloatPrecision()),new Apfloat("4", Configurator.apfloatPrecision()));
		Circle b = new Circle("2", "B", new Apfloat("0", Configurator.apfloatPrecision()), new Apfloat("1", Configurator.apfloatPrecision()),new Apfloat("1", Configurator.apfloatPrecision()));
		Circle c = new Circle("3", "C", new Apfloat("0", Configurator.apfloatPrecision()), new Apfloat("-1", Configurator.apfloatPrecision()),new Apfloat("1", Configurator.apfloatPrecision()));
		circles.add(a);
		circles.add(b);
		circles.add(c);
				
	*/
		
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
		
		return squaredDist;
	}

}
