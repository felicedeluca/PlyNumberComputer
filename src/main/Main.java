package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import statemachine.StateMachine;

public class Main {

	public static void main(String[] args) throws Exception {

		if (args.length != 2)
			throw new IllegalArgumentException("Please check input values: filePath radiusratio.");

		File inputGraphFileName = new File(args[0]);
		double radiusRatio = Double.parseDouble(args[1]);

		double plyNumber = StateMachine.startPlyComputation(inputGraphFileName, radiusRatio);

		String fileName = inputGraphFileName.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}

		String row = fileName + ";" + plyNumber+"\n";

		try {

			File outputFile = new File("ply.csv");

			if (!outputFile.exists()) {
				outputFile.createNewFile();
			}

			Files.write(outputFile.toPath(), row.getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			// exception handling left as an exercise for the reader
		}

	}

}
