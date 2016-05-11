package gateway;

import java.io.File;

import org.apfloat.Apfloat;

import statemachine.StateMachine;
import utilities.PlyConfigurator;

public class PlyNumber {
	
	static public int computePlyNumber(File inputGraphFileName, String radiusRatioStr) throws Exception{
		
		Apfloat radiusRatio = new Apfloat(radiusRatioStr, PlyConfigurator.apfloatPrecision());
		
		PlyConfigurator.getInstance().setRadiusRatio(radiusRatio);
		
		int ply = StateMachine.computePlyUsingLineSweep(inputGraphFileName, radiusRatio);

		/*
		String fileName = inputGraphFileName.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}
		*/
		
		return ply;
	}

}
