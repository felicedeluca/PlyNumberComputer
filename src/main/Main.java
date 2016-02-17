package main;

import java.io.File;

import algorithms.PlyGraphGenerator;
import gateway.GraphConverter;
import gateway.Importer;
import graph.Graph;
import maxclique.MaxClique;

public class Main {

	public static void main(String[] args) throws Exception {	
		
		File inputGraphFileName = new File("test.gml");

		Graph inputGraph = Importer.readInput(inputGraphFileName);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator(0.5);
		
		Graph plyGraph = pgg.generatePlyGraph(inputGraph, 0.5);
		
		byte[][] adjMat = GraphConverter.convertToAdjMatrix(plyGraph);
		
		MaxClique mc = new MaxClique(adjMat);
		double maxClique = mc.findSolution();
		
		System.out.println("Max Ply No: "+ maxClique);
		

	}

}
