package statemachine;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.apfloat.Apfloat;

import algorithms.PlyGraphGenerator;
import circlegraph.Circle;
import circlegraph.CircleGraph;
import gateway.GMLExporter;
import gateway.GraphConverter;
import gateway.GraphImporter;
import linesweep.LineSweepAlgorithm;
import maxclique.MaxClique;
import plygraph.GraphAP;
import utilities.Logger;

public class StateMachine {

	
	public static double startPlyComputation(File inputFile, Apfloat radiusRatio) throws Exception{

		GraphAP inputGraph = GraphImporter.readInput(inputFile);
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		GraphAP plyGraph = pgg.generatePlyIntersectionGraph(inputGraph, radiusRatio);
		
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
	
	
	public static int computePlyUsingLineSweep(File inputFile, Apfloat radiusRatio) throws Exception{
		
		GraphAP inputGraph = GraphImporter.readInput(inputFile);
		
		Logger.logln(inputFile.getName());
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Set<Circle> circles = pgg.computePlyCircles(inputGraph, radiusRatio);
		
		Logger.logln("Circles: " + circles.size());
		
		LineSweepAlgorithm lsa = new LineSweepAlgorithm();
		
		int ply = lsa.computePly(circles);
		
		if(lsa.plyCircleGraph!=null){
			CircleGraph cg = lsa.plyCircleGraph;
			cg.setName(inputFile.getName());
			Logger.logln("Storing Visual Json");
			storeD3VisualBackupJSON(inputFile, cg);
		}		
		
		return ply;
		
		
	}
	
	private static void storeD3VisualBackupJSON(File inputGraphFileName, CircleGraph cg){
	
		String fileName = inputGraphFileName.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}

		String content = cg.serializeToD3();

		try {

			File outputFile = new File("results"+File.separator+fileName+".json");

			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			Files.write(outputFile.toPath(), content.getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			// exception handling left as an exercise for the reader
		}

	}
	
	
}
