package linesweep;

import org.apfloat.Apfloat;

public class Circle {
	
	Apfloat x;
	Apfloat y;
	Apfloat radius;
	
	public Circle(int identifier, Apfloat x, Apfloat y, Apfloat radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public Apfloat getX(){
		return this.x;
	}
	
	public Apfloat getY(){
		return this.y;
	}
	
	public String toString(){
		
		String str = "c: ( "+this.x+", "+this.y+") r:"+this.radius;
		
		return str;
		
	}
	
}
