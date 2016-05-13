package gateway;

import java.io.File;

import org.apfloat.Apfloat;

import statemachine.PlyStateMachine;
import utilities.PlyConfigurator;

public class PlyNumber {
	
	static public int computePlyNumber(File inputGraphFileName, String radiusRatioStr) throws Exception{
		
		Apfloat radiusRatio = new Apfloat(radiusRatioStr, PlyConfigurator.apfloatPrecision());
		
		PlyConfigurator.getInstance().setRadiusRatio(radiusRatio);
		
		int ply = PlyStateMachine.computePlyUsingLineSweep(inputGraphFileName, radiusRatio);

		return ply;
	}

}
