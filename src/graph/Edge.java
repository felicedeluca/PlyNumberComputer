package graph;

public class Edge {
	
	Integer identifier, source, target;
	double length =-1;
	boolean directed = false;
	
	public Edge(Vertex source, Vertex target){
		
		this.source = source.identifier;
		this.target = target.identifier;
		
	}
	
	public Edge(int identifier, Integer source, Integer target){
		
		this.identifier = identifier;
		this.source = source;
		this.target = target;
		
	}
	
	public Edge(int identifier, Integer source, Integer target, double length){
		
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

	public void setLength(double length){
		this.length = length;
	}
	
	public double getLenth(){
		return this.length;
	}
}
