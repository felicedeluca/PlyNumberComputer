package statemachine;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Set;

import org.apfloat.Apfloat;

import algorithms.PlyGraphGenerator;
import gateway.GMLExporter;
import gateway.GraphConverter;
import gateway.GraphImporter;
import graph.Graph;
import graph.Vertex;
import linesweep.Circle;
import linesweep.CirclesMng;
import linesweep.LineSweepAlgorithm;
import maxclique.MaxClique;

public class StateMachine {

	
	public static double startPlyComputation(File inputFile, Apfloat radiusRatio) throws Exception{

		Graph inputGraph = GraphImporter.readInput(inputFile);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Graph plyGraph = pgg.generatePlyIntersectionGraph(inputGraph, radiusRatio);
		
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
	
	
	public static double computePlyUsingLineSweep(File inputFile, Apfloat radiusRatio) throws Exception{
		
		Graph inputGraph = GraphImporter.readInput(inputFile);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Set<Vertex> vertices = pgg.computePlyCircles(inputGraph, radiusRatio);
		Set<Circle> circles = CirclesMng.sharedInstance().convertVerticesToCirlces(vertices);
		
		LineSweepAlgorithm lsa = new LineSweepAlgorithm();
		lsa.startOnCircles(circles);
		
		return 0;
		
		
	}
	
}
