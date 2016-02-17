package statemachine;

import java.io.File;

import algorithms.PlyGraphGenerator;
import gateway.GraphConverter;
import gateway.Importer;
import graph.Graph;
import maxclique.MaxClique;

public class StateMachine {

	
	public static double startPlyComputation(File inputFile, double radiusRatio) throws Exception{

		Graph inputGraph = Importer.readInput(inputFile);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Graph plyGraph = pgg.generatePlyGraph(inputGraph, radiusRatio);
		
		byte[][] adjMat = GraphConverter.convertToAdjMatrix(plyGraph);
		
		MaxClique mc = new MaxClique(adjMat);
		double maxClique = mc.findSolution();
		
		return maxClique;
	}
	
	
}
