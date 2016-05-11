package circlegraph;

import java.util.HashSet;
import java.util.Set;

import graphap.VertexAP;

public class CirclesMng {
	
	private static CirclesMng instance = null;
	
	private CirclesMng(){}
	
	public static CirclesMng sharedInstance(){
		
		if(instance==null) instance = new CirclesMng();
		
		return instance;
		
	}
	
	public Set<Circle> convertVerticesToCirlces(Set<VertexAP> vertices){
		
		Set<Circle> circlesSet = new HashSet<Circle>();
		
		for(VertexAP v : vertices){
			
			Circle c = new Circle(v.identifier+"", v.label, v.x, v.y, v.getSquaredCircleRadius());
			
			circlesSet.add(c);
			
		}
		
		return circlesSet;
		
		
	}
	

}
