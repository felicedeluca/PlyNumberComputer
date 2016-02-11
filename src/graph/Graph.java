package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

	public Set<Vertex> vertices;
	
	private Map<Vertex, Set<Vertex>> adjsMap;
	
	
	public Graph(Set<Vertex> vertices){
		this.vertices = vertices;
		this.adjsMap = new HashMap<Vertex, Set<Vertex>>();
		
		for(Vertex v : this.vertices){
			
			Set<Vertex> adjsSet = new HashSet<Vertex>();
			adjsMap.put(v, adjsSet);
			
		}
		
		
	}	
	
	
	public Set<Vertex> getAdjsOfVertex(Vertex vertex){

		Set<Vertex> adjs = this.adjsMap.get(vertex);
		
		if(adjs == null) adjs = new HashSet<Vertex>();
		
		return adjs;

	}
	
	public void setAdjsVertices(Vertex source, Vertex target, boolean directed){
		
		Set<Vertex> sourceVertexAdjsSet = this.adjsMap.get(source);
		if(sourceVertexAdjsSet == null) sourceVertexAdjsSet = new HashSet<Vertex>();
		sourceVertexAdjsSet.add(target);
		this.adjsMap.put(source, sourceVertexAdjsSet);

		if(directed) return;
		
		Set<Vertex> targetVertexAdjsSet = this.adjsMap.get(target);
		if(targetVertexAdjsSet == null) targetVertexAdjsSet = new HashSet<Vertex>();
		targetVertexAdjsSet.add(source);
		this.adjsMap.put(target, targetVertexAdjsSet);
		
	}
	
}
