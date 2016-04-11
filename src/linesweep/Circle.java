package linesweep;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Circle {
	
	String identifier;
	String label;
	Apfloat x;
	Apfloat y;
	Apfloat squaredRadius;
	
	public Circle(String identifier, String label, Apfloat x, Apfloat y, Apfloat squaredRadius){
		this.identifier = identifier;
		this.label = label;
		this.x = x;
		this.y = y;
		this.squaredRadius = squaredRadius;
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
		
		String str = this.getLabel()+": ( "+this.x+", "+this.y+") r:"+this.squaredRadius;
		
		return str;
		
	}
	
	public Apfloat getSquaredRadius(){
		return this.squaredRadius;
	}
	
	
	public Apfloat getRadius(){
		return ApfloatMath.sqrt(squaredRadius);
	}
	
	
	
}
