package linesweep;

import java.util.HashSet;
import java.util.Set;

import graph.Vertex;

public class CirclesMng {
	
	private static CirclesMng instance = null;
	
	private CirclesMng(){}
	
	public static CirclesMng sharedInstance(){
		
		if(instance==null) instance = new CirclesMng();
		
		return instance;
		
	}
	
	public Set<Circle> convertVerticesToCirlces(Set<Vertex> vertices){
		
		Set<Circle> circlesSet = new HashSet<Circle>();
		
		for(Vertex v : vertices){
			
			Circle c = new Circle(v.identifier, v.x, v.y, v.circleRadius);
			
			circlesSet.add(c);
			
		}
		
		return circlesSet;
		
		
	}
	

}
