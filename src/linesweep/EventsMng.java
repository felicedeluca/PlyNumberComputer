package linesweep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import linesweep.Event.Type;
import utilities.Configurator;

public class EventsMng {

	ArrayList<Event> orderdEvents;

	Map<Apfloat, Set<Event>> eventsMap;

	public EventsMng(){

		this.eventsMap = new HashMap<Apfloat, Set<Event>>();

	}

	public Map<Apfloat, Set<Event>> computeStartingEndingAndIntersectingEvents(Set<Circle> circles){

		for(Circle c : circles){
			
			
			if(c.radius.compareTo(new Apfloat("0"))==0){
				
				//System.out.println("zero radius");
				continue;
			}

			Apfloat startingX = c.getX().subtract(c.radius);
			
			//System.out.println("starting " + startingX.toString(true) + " c: "+ c.getX().toString(true) + " r:  " + c.radius.toString(true));
			
			Apfloat endingX = c.getX().add(c.radius);
			
			//System.out.println("ending " + endingX.toString(true) + " c: "+ c.getX().toString(true) + " r:  " + c.radius.toString(true));


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
					
					Set<Apfloat> intersectionPoints = getIntersectionPoints(c, c2);
					
					if(intersectionPoints.size()>1){
					
					for(Apfloat xi : intersectionPoints){
						
						Event iEvent = new Event(Type.INTERSECTION, c, c2);
						
						Set<Event> eventsOnIntersectionPoint = eventsMap.get(xi);
						if(eventsOnIntersectionPoint == null){
							eventsOnIntersectionPoint = new HashSet<Event>();
						}

						eventsOnIntersectionPoint.add(iEvent);
						eventsMap.put(xi, eventsOnIntersectionPoint);

						
						
					}
					
				}
				}
				
			}

		}

		return this.eventsMap;

	}

	private Set<Apfloat> getIntersectionPoints(Circle c1, Circle c2){
		
		Set<Apfloat> intersectionPoints = new HashSet<Apfloat>();

		Apfloat dx = c2.getX().subtract(c1.getX());
		Apfloat dy = c2.getY().subtract(c1.getY());

		Apfloat d = ApfloatMath.sqrt(ApfloatMath.pow(ApfloatMath.abs(dy), 2).add(ApfloatMath.pow(ApfloatMath.abs(dx), 2)));
		
		if (d.compareTo(c1.radius.add(c2.radius))>=0){
			//no intersection
			return intersectionPoints;
		}

		if(d.compareTo(ApfloatMath.abs(c1.radius.subtract(c2.radius))) < 0){
			//Inclusion  no events needed
			return intersectionPoints;
		}

		Apfloat a = ApfloatMath.pow(c1.radius, 2).subtract(ApfloatMath.pow(c2.radius, 2)).add(ApfloatMath.pow(d, 2)).divide(d.multiply(new Apfloat("2.0", Configurator.apfloatPrecision())));
		Apfloat x2 = c1.getX().add(dx.multiply(a).divide(d));
		Apfloat y2 = c1.getY().add(dy.multiply(a).divide(d));

		Apfloat h = ApfloatMath.sqrt(ApfloatMath.pow(c1.radius, 2).subtract(ApfloatMath.pow(a, 2)));

		Apfloat rx = dy.negate().multiply(h.divide(d));// (0-dy) * (h/d);
		Apfloat ry = dx.multiply(h.divide(d));// dx * (h/d);

		Apfloat xi1 = x2.add(rx);// + rx;
		Apfloat xi2 = x2.subtract(rx);// - rx;
		Apfloat yi1 = y2.add(ry);// + ry;
		Apfloat yi2 = y2.subtract(ry);// - ry;
		
		intersectionPoints.add(xi1);
		intersectionPoints.add(xi2);
		
		return intersectionPoints;


	}


}
