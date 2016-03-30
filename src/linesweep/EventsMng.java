package linesweep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import linesweep.Event.Type;

public class EventsMng {
	
	ArrayList<Event> orderdEvents;
	
	Map<Double, Set<Event>> eventsMap;
	
	public EventsMng(){
		
		this.eventsMap = new HashMap<Double, Set<Event>>();
		
	}
	
	public Map<Double, Set<Event>> computeStartingAndEndingEvents(Set<Circle> circles){
	
		for(Circle c : circles){
			
			double startingX = c.getX()-c.radius;
			double endingX = c.getX()+c.radius;
			
			Event oEvent = new Event(Type.OPENING, c);
			Event cEvent = new Event(Type.CLOSING, c);
			
			Set<Event> eventsOnStartingPoint = eventsMap.get(startingX);
			if(eventsOnStartingPoint == null){
				eventsOnStartingPoint = new HashSet<Event>();
			}
			
			Set<Event> eventsOnEndingPoint = eventsMap.get(endingX);
			if(eventsOnEndingPoint == null){
				eventsOnEndingPoint = new HashSet<Event>();
			}
			
			eventsOnStartingPoint.add(oEvent);
			eventsOnEndingPoint.add(cEvent);
			
			
			eventsMap.put(startingX, eventsOnStartingPoint);
			eventsMap.put(endingX, eventsOnEndingPoint);
			
		}
		
		return this.eventsMap;
	
	}
	
}
