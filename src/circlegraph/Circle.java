package circlegraph;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Circle {
	
	String identifier;
	String label;
	Apfloat x;
	Apfloat y;
	Apfloat squaredRadius;
	
	Apfloat radius;
	
	String color;
	
	public Circle(String identifier, String label, Apfloat x, Apfloat y, Apfloat squaredRadius){
		this.identifier = identifier;
		this.label = label;
		this.x = x;
		this.y = y;
		this.squaredRadius = squaredRadius;
		this.radius = ApfloatMath.sqrt(squaredRadius);
		this.color = "black";
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
		
		String str = this.getLabel()+": ( "+this.x+", "+this.y+") r^2:"+this.squaredRadius;

		return str;
		
	}
	
	public String toEquation(){
		String str = this.getLabel()+": ( x-"+this.x+")^2+(y-"+this.y+")^2="+this.squaredRadius;

		return str;
	}
	
	public Apfloat getSquaredRadius(){
		return this.squaredRadius;
	}
	
	
	public Apfloat getRadius(){
		return this.radius;
	}
	
	public boolean hasRadiusZero(){
		return  (this.getSquaredRadius().compareTo(new Apfloat("0"))==0);

	}
	
	public String serializeToD3(){
		
		String str = "";
		
		//	{ "x": 30, "y": 30, "radius": 20, "color" : "green" },
		str = "{  "
				+ " \"id\" : \""+this.getLabel()+"\","
				+ "\"label\" :\""+this.getLabel()+"\","
				+ " \"x\" :\"" + this.getX().toString(true) + "\","
				+ " \"y\" :\"" + this.getY().toString(true) + "\","
				+ " \"radius\" :" + this.getRadius().toString(true) +","
				+ " \"color\" : \""+this.color+"\""
			+ "}";		
		
		return str;
		
	}
	
	public void setColor(String color){
		this.color = color;
	}

	
	
}
