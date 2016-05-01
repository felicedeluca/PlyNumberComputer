package gateway;

import java.io.File;

import org.apfloat.Apfloat;

import statemachine.StateMachine;
import utilities.Configurator;

public class PlyNumber {
	
	static public int computePlyNumber(File inputGraphFileName, String radiusRatioStr) throws Exception{
		
		
		Apfloat radiusRatio = new Apfloat(radiusRatioStr, Configurator.apfloatPrecision());
		
		Configurator.getInstance().setRadiusRatio(radiusRatio);
		//System.out.println("Inputgraph: " + inputGraphFileName.getName());
		//System.out.println("Radius Ratio: " + radiusRatio.toString(true));
		
		int ply = (int) StateMachine.computePlyUsingLineSweep(inputGraphFileName, radiusRatio);

		String fileName = inputGraphFileName.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
			fileName = fileName.substring(0, pos);
		}
		
		return ply;
	}

}
