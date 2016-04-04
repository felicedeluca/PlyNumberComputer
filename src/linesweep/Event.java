package linesweep;

import java.util.Set;

public class Event {

	Type type;
	Circle circle;
	Circle c1;
	Circle c2;
	
	public enum Type {
		OPENING, CLOSING, INTERSECTION
	}
	
	public Event(Type type, Circle circle){
		
		this.type = type;
		this.circle = circle;
	
	}
	
	public Event(Type type, Circle c1, Circle c2){
		
		this.type = type;
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public Type getType(){
		
		return type;
	}
	
}
