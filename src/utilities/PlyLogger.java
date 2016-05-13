package utilities;

public class PlyLogger {
	
	static boolean log = false;
	
	public static void logln(String s){
		PlyLogger.log(s+"\n");
	}
	
	public static void log(String s){
		if (PlyConfigurator.debug)
			System.out.print(s);

	}
	
}
