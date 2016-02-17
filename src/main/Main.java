package main;

import java.io.File;

import statemachine.StateMachine;

public class Main {

	public static void main(String[] args) throws Exception {	


		File inputGraphFileName = new File("test.gml");

		double plyNumber = StateMachine.startPlyComputation(inputGraphFileName, 0.5);
		
		System.out.println("Max Ply No: "+ plyNumber);
		

	}

}
