package linesweep;

import circlegraph.Circle;

public class Event {

	Type type;
	Circle circle;
	Circle c1;
	Circle c2;
	
	public enum Type {
		OPENING, CLOSING, INTERSECTION, DUPLICATED
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
