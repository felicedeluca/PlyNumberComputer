package graph;

public class Vertex {

	public Integer identifier;
	public double x;
	public double y;
	public String label;
	public double circleRadius;

	public Vertex(Integer identifier, double x, double y){

		this.identifier = identifier;
		this.x = x;
		this.y = y;
		this.label = identifier +"";
		this.circleRadius = 0;

	}


}
