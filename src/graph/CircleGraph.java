package graph;

import java.util.Set;

import org.apfloat.Apfloat;

import linesweep.Circle;

public class CircleGraph {

	String name = "";
	
	Set<Circle> circles;
	Set<Circle> maxPlyCircles;
	
	int ply;
	Apfloat xMaxPly;
	
	public CircleGraph(Set<Circle> circles, Set<Circle> maxPlyCircles, Apfloat xMaxPly){
		
		this.circles = circles;
		this.maxPlyCircles = maxPlyCircles;
		this.ply = this.maxPlyCircles.size();
		this.xMaxPly = xMaxPly;
		
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getPly(){
		return this.ply;
	}
	
	
	public String serializeToD3(){
		
		String str = "{";
	
		for(Circle c : this.maxPlyCircles){
			c.setColor("red");
		}
		str += "\"name\" : \"" + this.name + "\",\n";

		str += "\"ply\" : " + this.xMaxPly.toString(true) + ",\n";
		str += "\"circles\" : "; 
		int i=0;
		
		str += "[\n";
		for(Circle c : this.circles){
			if(i>0) str += ",\n";
			str += c.serializeToD3();
			i++;
		}
			
		str += "]\n";

		
		str += "}";
		
		return str;
		
		
	}
	
}
