package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;
import graphap.EdgeAP;
import graphap.GraphAP;
import graphap.VertexAP;

public class PlyGraphGenerator{
				
	public PlyGraphGenerator(){}


	public Set<Circle> computePlyCircles(GraphAP graph, Apfloat radiusRatio){
				
		this.computeEdgesDistances(graph);
		
		Set<Circle> circles = new HashSet<Circle>();
		
		Apfloat squaredRadiusRatio = ApfloatMath.pow(radiusRatio, 2);
				
		for(VertexAP currVertex : graph.getVertices()){
						
			Apfloat maxSquaredRadiusLength = new Apfloat("0");
			
			Set<EdgeAP> adjEdges = graph.getIncidentEdges(currVertex);
			
			for(EdgeAP currEdge : adjEdges){
				
				Apfloat squaredLength = currEdge.getSquaredLength();
				Apfloat currSquaredRadius = squaredLength.multiply(squaredRadiusRatio);
				
				maxSquaredRadiusLength = (maxSquaredRadiusLength.compareTo(currSquaredRadius)== 1) ? maxSquaredRadiusLength : currSquaredRadius;
			}
			
			currVertex.setSquaredCircleRadius(maxSquaredRadiusLength);
			
			Circle c = new Circle(currVertex.identifier+"", currVertex.label, currVertex.x, currVertex.y, currVertex.getSquaredCircleRadius());

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


	public GraphAP generatePlyIntersectionGraph(GraphAP graph, Apfloat radiusRatio){
		
		computePlyCircles(graph, radiusRatio);

		Map<Integer, VertexAP> verticesMap = graph.getVerticesMap();
		Map<Integer, EdgeAP> edgesMap = new HashMap<Integer, EdgeAP>();

			
		ArrayList<VertexAP> orderedVerticesList = new ArrayList<VertexAP>(verticesMap.values());
		
		int edgeId = 0;
		
		for(int i=0; i<orderedVerticesList.size(); i++){
			
			VertexAP firstVertex = orderedVerticesList.get(i);
			
			for(int j=i+1; j<orderedVerticesList.size(); j++){
				
				VertexAP secondVertex = orderedVerticesList.get(j);
				
				if(doCirclesIntesect(firstVertex, secondVertex)){
										
					EdgeAP currIntersectionEdge = new EdgeAP(edgeId, firstVertex.identifier, secondVertex.identifier);
					edgesMap.put(edgeId, currIntersectionEdge);
					edgeId++;
						
				}
		
			}
			
		}
		
		GraphAP plyGraph = new GraphAP(verticesMap, edgesMap);
		
		return plyGraph;
	}
	
	/***************************************************************************
     *  test intersection
     ***************************************************************************/
	
	private boolean doCirclesIntesect(VertexAP v1, VertexAP v2){
		
		Apfloat squaredDistance = squaredDistance(v1, v2);
		Apfloat distance = ApfloatMath.sqrt(squaredDistance);
		
		Apfloat v1CircleRadius = v1.getCircleRadius();
		Apfloat v2CircleRadius = v2.getCircleRadius();
		
		Apfloat radiiSum = ApfloatMath.sum(v1CircleRadius, v2CircleRadius);
		
		int compare = (radiiSum.compareTo(distance));
		
		return (compare > 0);
		
	}
	
	
	/***************************************************************************
     *  Distances
     ***************************************************************************/	
	private void computeEdgesDistances(GraphAP graph){
		
		Set<EdgeAP> edges = graph.getEdges();
		Map<Integer, VertexAP> verticesMap = graph.getVerticesMap();
		
	
		for(EdgeAP edge : edges){
			
			VertexAP source = verticesMap.get(edge.getSourceIdentifier());
			VertexAP target = verticesMap.get(edge.getTargetIdentifier());
			
			Apfloat currSquaredDistance = squaredDistance(source, target);
			edge.setSquaredLength(currSquaredDistance);		
		}
	
	}
	
	private Apfloat squaredDistance(VertexAP firstVertex, VertexAP secondVertex){
		
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
