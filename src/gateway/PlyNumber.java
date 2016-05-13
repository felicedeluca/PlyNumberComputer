package gateway;

import java.io.File;

import org.apfloat.Apfloat;

import statemachine.PlyResult;
import statemachine.PlyStateMachine;
import utilities.PlyConfigurator;

public class PlyNumber {
	
	
	/**
	 * Computes the ply
	 * @param inputGraphFileName Input gml file of the drawing
	 * @param radiusRatioStr Radius Ratio
	 * @return Max Ply
	 * @throws Exception
	 */
	static public int computePlyNumber(File inputGraphFileName, String radiusRatioStr) throws Exception{
		
		Apfloat radiusRatio = new Apfloat(radiusRatioStr, PlyConfigurator.apfloatPrecision());
		
		PlyConfigurator.getInstance().setRadiusRatio(radiusRatio);
		
		PlyResult res = PlyStateMachine.computePly(inputGraphFileName, radiusRatio);

		return res.ply;
	}

}
