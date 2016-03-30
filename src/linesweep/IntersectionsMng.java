package linesweep;

public class IntersectionsMng {

	private static IntersectionsMng instance = null;
	
	private IntersectionsMng(){}
	
	public static IntersectionsMng sharedInstance(){
		
		if(instance == null){
			instance = new IntersectionsMng();
		}
		
		return instance;
	}
	
	
	
}
