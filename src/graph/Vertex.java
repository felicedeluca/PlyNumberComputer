package graph;

import org.apfloat.Apfloat;

public class Vertex {

	public Integer identifier;
	public Apfloat x;
	public Apfloat y;
	public String label;
	public Apfloat circleRadius;

	public Vertex(Integer identifier, Apfloat x, Apfloat y){

		this.identifier = identifier;
		this.x = x;
		this.y = y;
		this.label = identifier +"";
		this.circleRadius = new Apfloat("0");

	}


}
