package graph;

import org.apfloat.Apfloat;

public class Vertex {

	public Integer identifier;
	public Apfloat x;
	public Apfloat y;
	public String label;
	public Apfloat circleRadius;
	public Apfloat circleRadiusSquared;

	public Vertex(Integer identifier, Apfloat x, Apfloat y, String label){

		this.identifier = identifier;
		this.label = label;
		this.x = x;
		this.y = y;
		this.label = identifier +"";
		this.circleRadius = new Apfloat("0");

	}


}
