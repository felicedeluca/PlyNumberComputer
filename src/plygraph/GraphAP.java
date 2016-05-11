package plygraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphAP {

	private Map<Integer, VertexAP> verticesMap;
	private Map<Integer, EdgeAP> edgesMap;
	private Map<Integer, Set<Integer>> vertexEdgesMap;
	
	
	public GraphAP(Map<Integer, VertexAP> verticesMap, Map<Integer, EdgeAP> edgesMap){
		
		this.verticesMap = verticesMap;
		this.edgesMap = edgesMap;
		
		this.vertexEdgesMap = new HashMap<Integer, Set<Integer>>();
		
		for(VertexAP currVertex : verticesMap.values()){
			
			Integer identifier = currVertex.identifier;
			Set<Integer> currEdges = new HashSet<Integer>();
			
			this.vertexEdgesMap.put(identifier, currEdges);
		}
		
		this.organizeEdges();
		
	}	
	
	
	public Set<VertexAP> getAdjsOfVertex(VertexAP vertex){

		Set<VertexAP> adjs = new HashSet<VertexAP>();
		
		Set<Integer> edgesIds = this.vertexEdgesMap.get(vertex.identifier);
		
		for(Integer edgeId : edgesIds){
			
			EdgeAP currEdge = edgesMap.get(edgeId);
			Integer source = currEdge.getSourceIdentifier();
			Integer target = currEdge.getTargetIdentifier();
			
			VertexAP vertexToAdd = null;
			
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

		for(EdgeAP currEdge : edgesMap.values()){
						
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
	
	public Set<EdgeAP> getIncidentEdges(VertexAP vertex){
		
		Set<EdgeAP> edges = new HashSet<EdgeAP>();
		
		Set<Integer> inc = this.vertexEdgesMap.get(vertex.identifier);
		
		for(Integer eId : inc){
			
			edges.add(this.edgesMap.get(eId));
			
		}
		
		
		return edges;
	}
	
	public Set<VertexAP> getVertices(){
		
		
		return new HashSet<VertexAP>(this.verticesMap.values());
		
	}
	
	public Set<EdgeAP> getEdges(){
		
		
		return new HashSet<EdgeAP>(this.edgesMap.values());
		
	}
	
	public Map<Integer, VertexAP> getVerticesMap(){
		
		return this.verticesMap;
		
	}
	
	
	
}
