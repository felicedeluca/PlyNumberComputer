package linesweep;

import java.util.Set;

public class Event {

	Type type;
	Circle circle;
	
	public enum Type {
		OPENING, CLOSING, INTERSECTION
	}
	
	public Event(Type type, Circle circle){
		
		this.type = type;
		this.circle = circle;
	
	}
	
	public Type getType(){
		
		return type;
	}
	
}
