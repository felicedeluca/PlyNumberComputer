package main;

import java.io.File;

import statemachine.StateMachine;

public class Main {

	public static void main(String[] args) throws Exception {	

		if(args.length!=2) throw new IllegalArgumentException("Please check input values: filePath radiusratio."); 
		
		File inputGraphFileName = new File(args[0]);
		double radiusRatio = Double.parseDouble(args[1]); 
				
		double plyNumber = StateMachine.startPlyComputation(inputGraphFileName, radiusRatio);
		
		System.out.println("Max Ply No: "+ plyNumber);
		

	}

}
