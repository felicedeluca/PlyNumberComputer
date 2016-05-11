package graphap;

import org.apfloat.Apfloat;

public class EdgeAP {
	
	Integer identifier, source, target;
	//Apfloat length;
	boolean directed = false;
	public String label;
	Apfloat squaredLength;
	
	public EdgeAP(VertexAP source, VertexAP target){
		
		this.source = source.identifier;
		this.target = target.identifier;
		this.squaredLength = new Apfloat("0");
		
	}
	
	public EdgeAP(int identifier, Integer source, Integer target){
		
		this.identifier = identifier;
		this.source = source;
		this.target = target;
		
	}

	/*
	public Edge(int identifier, Integer source, Integer target){//, Apfloat length){
		
		this.identifier = identifier;
		this.source = source;
		this.target = target;
		this.length = length;
		
	}
	
	*/
	
	public Integer getIdentifier(){
		return this.identifier;
	}
	
	public int getSourceIdentifier(){
		return this.source;
	}
	
	public int getTargetIdentifier(){
		return this.target;
	}
	
	public boolean getIsDirected(){
		return directed;
	}
	
	public void setSquaredLength(Apfloat squaredLength){
		this.squaredLength = squaredLength;
	}
	
	public Apfloat getSquaredLength(){
		
		return this.squaredLength;
		
	}
//	public Apfloat getLenth(){
	//	return this.length;
//	}
	
	public String toString(){
		
		return this.getIdentifier()+ ": " + this.getSourceIdentifier() + "-" + this.getTargetIdentifier();
		
	}
}
