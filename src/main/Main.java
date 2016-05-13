package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apfloat.Apfloat;

import statemachine.PlyResult;
import statemachine.PlyStateMachine;
import utilities.PlyConfigurator;
import utilities.PlyLogger;

public class Main {

	public static void main(String[] args) throws Exception {

		
	if (args.length == 0)
		throw new IllegalArgumentException("Please check input values: filePath [, radiusratio=0.5]");

		File inputGraphFileName = new File(args[0]);
		
		Apfloat radiusRatio = new Apfloat("0.5", PlyConfigurator.apfloatPrecision());
		
		if(args.length > 1)
			radiusRatio = new Apfloat(args[1], PlyConfigurator.apfloatPrecision());
		
		//Log
		PlyLogger.logln("File: " + inputGraphFileName.getName());
		PlyLogger.logln("Radius Ratio: " + radiusRatio.toString(true));
		
		
		PlyConfigurator.getInstance().setRadiusRatio(radiusRatio);
		PlyResult res = PlyStateMachine.computePly(inputGraphFileName, radiusRatio);

		String row = res.toCSV()+System.getProperty("line.separator");
		
		System.out.println(row);
		
		try {

			File outputFile = new File("results"+File.separator+"ply_results.csv");

			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			Files.write(outputFile.toPath(), row.getBytes(), StandardOpenOption.APPEND);
			
		} catch (Exception e) {}

	}

}
