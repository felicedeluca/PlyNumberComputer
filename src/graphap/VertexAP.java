package graphap;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class VertexAP {

	public Integer identifier;
	public Apfloat x;
	public Apfloat y;
	public String label;
	private Apfloat squaredCircleRadius;

	public VertexAP(Integer identifier, Apfloat x, Apfloat y, String label){

		this.identifier = identifier;
		this.label = label;
		this.x = x;
		this.y = y;
		this.label = identifier +"";
		this.squaredCircleRadius = new Apfloat("0");

	}

	
	public Apfloat getCircleRadius(){
		return ApfloatMath.sqrt(this.squaredCircleRadius);
	}
	
	public void setSquaredCircleRadius(Apfloat squaredCircleRadius){
		this.squaredCircleRadius = squaredCircleRadius;
	}
	
	public Apfloat getSquaredCircleRadius(){
		return this.squaredCircleRadius;
	}
}
