package utilities;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class Configurator {
	
	private static Configurator instance = null;
	
	private Configurator(){}
	
	public static Configurator getInstance(){
		
		if (instance == null) instance = new Configurator();
		
		return instance;
	}
	
	
	static int apfloatPrecision = 200;
	static Apfloat epsilon = null;
	
	
	public static int apfloatPrecision(){
		return apfloatPrecision;
	}
	
	public static Apfloat epsilon(){
		
		if(epsilon == null){
			
			Apfloat temp = new Apfloat("3");
			
			epsilon = temp.multiply(ApfloatMath.pow(new Apfloat("10", apfloatPrecision), -(apfloatPrecision-2)));
		}
		
		return epsilon;
	}
	
}
