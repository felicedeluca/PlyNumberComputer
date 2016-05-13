package statemachine;

public class PlyResult {
	
	public String name;
	public int V;
	public int E;
	public int ply;

	
	public String toString(){	
		return "Name: "+ name+"\n"
			 + "Vertices: "+V+"\n"
			 + "Edges: "+E+"\n"
			 + "Ply: "+ply;
	}
	
	public String toCSV(){	
		return name+";"+V+";"+E+";"+ply;
	}
	
}
