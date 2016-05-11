package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apfloat.Apfloat;

import statemachine.StateMachine;
import utilities.PlyConfigurator;
import utilities.PlyLogger;

public class Main {

	public static void main(String[] args) throws Exception {

	if (args.length != 2)
		throw new IllegalArgumentException("Please check input values: filePath radiusratio.");

		File inputGraphFileName = new File(args[0]);
		Apfloat radiusRatio = new Apfloat(args[1], PlyConfigurator.apfloatPrecision());
		
		PlyConfigurator.getInstance().setRadiusRatio(radiusRatio);
		PlyLogger.logln("Inputgraph: " + inputGraphFileName.getName());
		PlyLogger.logln("Radius Ratio: " + radiusRatio.toString(true));
		
//		double plyNumber = StateMachine.startPlyComputation(inputGraphFileName, radiusRatio);
		double plyNumber = StateMachine.computePlyUsingLineSweep(inputGraphFileName, radiusRatio);

		
		String fileName = inputGraphFileName.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}

		String row = fileName + ";" + plyNumber+System.getProperty("line.separator");

		try {

			File outputFile = new File("results"+File.separator+"ply_results.csv");

			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			Files.write(outputFile.toPath(), row.getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			// exception handling left as an exercise for the reader
		}

	}

}
