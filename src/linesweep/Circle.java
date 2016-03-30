package linesweep;

public class Circle {
	
	double x;
	double y;
	double radius;
	
	public Circle(int identifier, double x, double y, double radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
}
