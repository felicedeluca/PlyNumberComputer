package utilities;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class PlyConfigurator {
	
	private static PlyConfigurator instance = null;
		
	private PlyConfigurator(){}
	
	public static boolean debug;
	
	public static PlyConfigurator getInstance(){
		
		if (instance == null) instance = new PlyConfigurator();
		
		return instance;
	}
	
	
	static int apfloatPrecision = 1000;
	static Apfloat epsilon = null;
	
	public static Apfloat zero = new Apfloat("0", PlyConfigurator.apfloatPrecision());
	public static Apfloat one = new Apfloat("1", PlyConfigurator.apfloatPrecision());
	public static Apfloat two = new Apfloat("2", PlyConfigurator.apfloatPrecision());
	public static Apfloat four = new Apfloat("4", PlyConfigurator.apfloatPrecision());

	
	Apfloat radiusRatio;

	
	
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
	
	
	public void setRadiusRatio(Apfloat radiusRatio){
		this.radiusRatio = radiusRatio;
	}
	
	public Apfloat getRadiusRatio(){
		return radiusRatio;
	}
	
	
	
	
	
}
