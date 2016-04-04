package linesweep;

import org.apfloat.Apfloat;

public class Circle {
	
	String identifier;
	String label;
	Apfloat x;
	Apfloat y;
	Apfloat radius;
	
	public Circle(String identifier, String label, Apfloat x, Apfloat y, Apfloat radius){
		this.identifier = identifier;
		this.label = label;
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public String getIdentifier(){
		return this.identifier;
	}

	public Apfloat getX(){
		return this.x;
	}
	
	public Apfloat getY(){
		return this.y;
	}
	
	public String toString(){
		
		String str = this.getLabel()+": ( "+this.x+", "+this.y+") r:"+this.radius;
		
		return str;
		
	}
	
}
