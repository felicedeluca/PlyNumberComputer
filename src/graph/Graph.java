package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {

	private Map<Integer, Vertex> verticesMap;
	private Map<Integer, Edge> edgesMap;
	private Map<Integer, Set<Integer>> vertexEdgesMap;
	
	
	public Graph(Map<Integer, Vertex> verticesMap, Map<Integer, Edge> edgesMap){
		
		this.verticesMap = verticesMap;
		this.edgesMap = edgesMap;
		
		this.vertexEdgesMap = new HashMap<Integer, Set<Integer>>();
		
		for(Vertex currVertex : verticesMap.values()){
			
			Integer identifier = currVertex.identifier;
			Set<Integer> currEdges = new HashSet<Integer>();
			
			this.vertexEdgesMap.put(identifier, currEdges);
		}
		
		this.organizeEdges();
		
	}	
	
	
	public Set<Vertex> getAdjsOfVertex(Vertex vertex){

		Set<Vertex> adjs = new HashSet<Vertex>();
		
		Set<Integer> edgesIds = this.vertexEdgesMap.get(vertex.identifier);
		
		for(Integer edgeId : edgesIds){
			
			Edge currEdge = edgesMap.get(edgeId);
			Integer source = currEdge.getSourceIdentifier();
			Integer target = currEdge.getTargetIdentifier();
			
			Vertex vertexToAdd = null;
			
			if (source != vertex.identifier){
				vertexToAdd = this.verticesMap.get(source);
			}else{
				vertexToAdd = this.verticesMap.get(target);
			}
			adjs.add(vertexToAdd);
			
		}
		
		return adjs;
		
	}
	
	private void organizeEdges(){

		for(Edge currEdge : edgesMap.values()){
						
			Integer source = currEdge.getSourceIdentifier();
			Integer target = currEdge.getTargetIdentifier();

			
			Set<Integer> sourceEdgesSet = vertexEdgesMap.get(source);
			if(sourceEdgesSet == null ) sourceEdgesSet = new HashSet<Integer>();
			sourceEdgesSet.add(currEdge.getIdentifier());
			this.vertexEdgesMap.put(source, sourceEdgesSet);
			
			
			if (currEdge.getIsDirected()) continue;
			
			Set<Integer> targetEdgesSet = vertexEdgesMap.get(target);
			if(targetEdgesSet == null ) targetEdgesSet = new HashSet<Integer>();
			targetEdgesSet.add(currEdge.getIdentifier());
			this.vertexEdgesMap.put(target, targetEdgesSet);
			
			
		}
		
		
	}
	
	public Set<Edge> getIncidentEdges(Vertex vertex){
		
		Set<Edge> edges = new HashSet<Edge>();
		
		Set<Integer> inc = this.vertexEdgesMap.get(vertex.identifier);
		
		for(Integer eId : inc){
			
			edges.add(this.edgesMap.get(eId));
			
		}
		
		
		return edges;
	}
	
	public Set<Vertex> getVertices(){
		
		
		return new HashSet<Vertex>(this.verticesMap.values());
		
	}
	
	public Set<Edge> getEdges(){
		
		
		return new HashSet<Edge>(this.edgesMap.values());
		
	}
	
	public Map<Integer, Vertex> getVerticesMap(){
		
		return this.verticesMap;
		
	}
	
	
	
}
