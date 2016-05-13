package statemachine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Set;

import org.apfloat.Apfloat;

import algorithms.PlyGraphGenerator;
import circlegraph.Circle;
import circlegraph.CircleGraph;
import gateway.PlyGMLImporter;
import graphap.GraphAP;
import linesweep.LineSweepAlgorithm;
import utilities.PlyLogger;

public class PlyStateMachine {
	
	
	public static int computePlyUsingLineSweep(File inputFile, Apfloat radiusRatio) throws Exception{
		
		GraphAP inputGraph = PlyGMLImporter.readInput(inputFile);
		
		PlyLogger.logln(inputFile.getName());
		
		PlyGraphGenerator pgg = new PlyGraphGenerator();
		
		Set<Circle> circles = pgg.computePlyCircles(inputGraph, radiusRatio);
		
		PlyLogger.logln("Circles: " + circles.size());
		
		LineSweepAlgorithm lsa = new LineSweepAlgorithm();
		
		
		int ply = lsa.computePly(circles);
		
		if(lsa.plyCircleGraph!=null){
			CircleGraph cg = lsa.plyCircleGraph;
			cg.setName(inputFile.getName());
			PlyLogger.logln("Storing Visual Json");
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

		}

	}
	
	
}
