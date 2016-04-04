package utilities;

public class Configurator {
	
	private static Configurator instance = null;
	
	private Configurator(){}
	
	public static Configurator getInstance(){
		
		if (instance == null) instance = new Configurator();
		
		
		return instance;
	}
	
	
	static int apfloatPrecision = 200;
	
	public static int apfloatPrecision(){
		return apfloatPrecision;
	}
	
}
