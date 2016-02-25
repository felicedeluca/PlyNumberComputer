package statemachine;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import algorithms.PlyGraphGenerator;
import gateway.GMLExporter;
import gateway.GraphConverter;
import gateway.GraphImporter;
import graph.Graph;
import maxclique.MaxClique;

public class StateMachine {

	
	public static double startPlyComputation(File inputFile, double radiusRatio) throws Exception{

		Graph inputGraph = GraphImporter.readInput(inputFile);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Graph plyGraph = pgg.generatePlyGraph(inputGraph, radiusRatio);
		
		File plyGMLFile = new File("results"+File.separator+"Ply Graph.gml");
		if(!plyGMLFile.exists()) plyGMLFile.createNewFile();
		
		 Writer writer = new FileWriter(plyGMLFile);
		 GMLExporter gmle = new GMLExporter();
		 gmle.export(writer, plyGraph);
		 
		
		byte[][] adjMat = GraphConverter.convertToAdjMatrix(plyGraph);
		
		MaxClique mc = new MaxClique(adjMat);
		double maxClique = mc.findSolution();
		
		return maxClique;
	}
	
	
}
