package graph;

import org.apfloat.Apfloat;

public class Edge {
	
	Integer identifier, source, target;
	Apfloat length;
	boolean directed = false;
	public String label;
	
	public Edge(Vertex source, Vertex target){
		
		this.source = source.identifier;
		this.target = target.identifier;
		length = new Apfloat("-1");
		
	}
	
	public Edge(int identifier, Integer source, Integer target){
		
		this.identifier = identifier;
		this.source = source;
		this.target = target;
		
	}
	
	public Edge(int identifier, Integer source, Integer target, Apfloat length){
		
		this.identifier = identifier;
		this.source = source;
		this.target = target;
		this.length = length;
		
	}
	
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

	public void setLength(Apfloat length){
		this.length = length;
	}
	
	public Apfloat getLenth(){
		return this.length;
	}
	
	public String toString(){
		
		return this.getIdentifier()+ ": " + this.getSourceIdentifier() + "-" + this.getTargetIdentifier();
		
	}
}
