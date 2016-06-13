package algorithms;

import graphap.EdgeAP;
import graphap.GraphAP;
import graphap.VertexAP;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;

public class PlyGraphGenerator{
				
	public PlyGraphGenerator(){}


	public Set<Circle> computePlyCircles(GraphAP graph, Apfloat radiusRatio){
				
		this.computeEdgesDistances(graph);
		
		Set<Circle> circles = new HashSet<Circle>();
		
		Apfloat squaredRadiusRatio = ApfloatMath.pow(radiusRatio, 2);
				
		for(VertexAP currVertex : graph.getVertices()){
						
			Apfloat maxSquaredRadiusLength = new Apfloat("0");
			
			Set<EdgeAP> adjEdges = graph.getIncidentEdges(currVertex);
			
			
			EdgeAP maxEdge = null;
			
			for(EdgeAP currEdge : adjEdges){
				
				Apfloat squaredLength = currEdge.getSquaredLength();
				Apfloat currSquaredRadius = squaredLength.multiply(squaredRadiusRatio);
				
				if(maxSquaredRadiusLength.compareTo(currSquaredRadius)!= 1){
					
					maxEdge = currEdge;
					maxSquaredRadiusLength = currSquaredRadius;
					
				}
				
				//maxSquaredRadiusLength = (maxSquaredRadiusLength.compareTo(currSquaredRadius)== 1) ? maxSquaredRadiusLength : currSquaredRadius;
			}
			
			currVertex.setSquaredCircleRadius(maxSquaredRadiusLength);
			
			Circle c = new Circle(currVertex.identifier+"", currVertex.label, currVertex.x, currVertex.y, currVertex.getSquaredCircleRadius(), maxEdge);

			circles.add(c);
			
		}
		
		return circles;
		
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
