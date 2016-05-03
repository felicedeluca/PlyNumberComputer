package utilities;

public class Logger {

	static boolean log = false;
	
	public static void log(String s){
		if (log)
			System.out.println(s);

	}
	
}
