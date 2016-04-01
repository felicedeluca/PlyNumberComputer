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

	public Map<Double, Set<Event>> computeStartingEndingAndIntersectingEvents(Set<Circle> circles){

		for(Circle c : circles){

			double startingX = c.getX()-c.radius;
			
			System.out.println("starting " + startingX + " c: "+ c.getX() + " r:  " + c.radius);
			
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
			
			for(Circle c2 : circles){
				
				if (!c2.equals(c)){
					
					checkIfCirclesIntersect(c, c2);
					
				}
				
			}

		}

		return this.eventsMap;

	}

	private void checkIfCirclesIntersect(Circle c1, Circle c2){

		double dx = c2.getX() - c1.getX();
		double dy = c2.getY() - c1.getY();

		double d = Math.sqrt((dy*dy) + (dx*dx));

		if (d >= c1.radius + c2.radius){
			//no intersection
			return;
		}

		if(d < Math.abs(c1.radius-c2.radius)){
			//Inclusion  no events needed
			return;
		}

		double a = (Math.pow(c1.radius, 2) - Math.pow(c2.radius, 2) + Math.pow(d, 2)) / (2.0 * d);
		double x2 = c1.getX() + (dx * a/d);
		double y2 = c1.getY() + (dy * a/d);

		double h = Math.sqrt(Math.pow(c1.radius, 2) - Math.pow(a, 2));

		double rx = (0-dy) * (h/d);
		double ry = dx * (h/d);

		double xi1 = x2 + rx;
		double xi2 = x2 - rx;
		double yi1 = y2 + ry;
		double yi2 = y2 - ry;
		
		
		System.out.println("pi1 1 at: ("+ xi1 +", "+ yi1 +")");
		System.out.println("pi1 2 at: ("+ xi2 +", "+ yi2 +")");

	}


}
