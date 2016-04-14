package linesweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;
import linesweep.Event.Type;
import utilities.Configurator;

public class EventsMng {

	ArrayList<Event> orderdEvents;

	public EventsMng(){}

	public Map<Apfloat, Set<Event>> computeStartingEndingAndIntersectingEvents(Set<Circle> circles){

		Map<Apfloat, Set<Event>> eventsMap = new HashMap<Apfloat, Set<Event>>();
		
		ArrayList<Circle> circlesArrList = new ArrayList<Circle>(circles);

		for(int i=0; i<circlesArrList.size(); i++){
			
			Circle c = circlesArrList.get(i);

			if(c.getSquaredRadius().compareTo(new Apfloat("0"))==0){
				continue;
			}

			Apfloat startingX = c.getX().subtract(c.getRadius());

			Apfloat endingX = c.getX().add(c.getRadius());

			Event oEvent = new Event(Type.OPENING, c);

			Event cEvent = new Event(Type.CLOSING, c);

			Set<Event> eventsOnStartingPoint = prepareForNewEvent(eventsMap, startingX);

			Set<Event> eventsOnEndingPoint = prepareForNewEvent(eventsMap, endingX);

			eventsOnStartingPoint.add(oEvent);
			eventsOnEndingPoint.add(cEvent);

			eventsMap.put(startingX, eventsOnStartingPoint);
			eventsMap.put(endingX, eventsOnEndingPoint);

			
			
			for(int j=i+1; j<circlesArrList.size(); j++){
				
				Circle c2 = circlesArrList.get(j);

				if (!c2.equals(c)){

					Set<Apfloat> intersectionPoints = getIntersectionPoints(c, c2);

					if(intersectionPoints.size()>1){

						for(Apfloat xi : intersectionPoints){

							Event iEvent = new Event(Type.INTERSECTION, c, c2);
							Set<Event> eventsOnIntersectionPoint = prepareForNewEvent(eventsMap, xi);
							eventsOnIntersectionPoint.add(iEvent);
							eventsMap.put(xi, eventsOnIntersectionPoint);

						}
					}
				}
			}
		}

		Map<Apfloat, Set<Event>> completeEventsMap =  addFakeEvents(eventsMap);

		return completeEventsMap;

	}

	private Set<Event> prepareForNewEvent(Map<Apfloat, Set<Event>> map, Apfloat x){

		Set<Event> eventsOnX = map.get(x);
		if(eventsOnX == null){
			eventsOnX = new HashSet<Event>();
		}

		return eventsOnX;

	}

	private Map<Apfloat, Set<Event>>  addFakeEvents(Map<Apfloat, Set<Event>> map){

		System.out.println("Adding fake Events");

		Map<Apfloat, Set<Event>> completeEventsMap = new HashMap<Apfloat, Set<Event>>(map);

		ArrayList<Apfloat> keys = new ArrayList<Apfloat>(completeEventsMap.keySet());
		Collections.sort(keys);

		/*
		 * 
		 * DUPLICATE ALL EVENTS


		for(Apfloat key : keys){

			Apfloat preKey = key.subtract(Configurator.epsilon());
			Apfloat postKey = key.add(Configurator.epsilon());

			Event PreEvent = new Event(Type.DUPLICATED, null);
			Event PostEvent = new Event(Type.DUPLICATED, null);

			Set<Event> eventsOnPrePoint = prepareForNewEvent(completeEventsMap, preKey);
			Set<Event> eventsOnPostPoint = prepareForNewEvent(completeEventsMap, postKey);

			eventsOnPrePoint.add(PreEvent);
			eventsOnPostPoint.add(PostEvent);

			completeEventsMap.put(preKey, eventsOnPrePoint);
			completeEventsMap.put(postKey, eventsOnPostPoint);

		}

		 */

		for(int i=0; i<keys.size()-1; i++){

			Apfloat currKey = keys.get(i);
			Apfloat nextKey = keys.get(i+1);

			Apfloat midKey = currKey;

			if(currKey.compareTo(nextKey)!=0){

				currKey = new Apfloat(keys.get(i).toString(), Configurator.apfloatPrecision()+1);
				nextKey = new Apfloat(keys.get(i+1).toString(), Configurator.apfloatPrecision()+1);
				
				Apfloat sum = currKey.add(nextKey);
				midKey = sum.divide(new Apfloat("2", currKey.precision()+1));

				if(!((currKey.compareTo(midKey)<=0 && nextKey.compareTo(midKey)==1) ||
						(currKey.compareTo(nextKey)==0 && currKey.compareTo(nextKey)==0))){

					String err = "MidValue:\n"
							+ "1: " + currKey.toString(true) + "\n"
							+ "2: "+ nextKey.toString(true) +"\n"
							+ "== " + midKey.toString(true);

					throw new IllegalArgumentException("Wrong mid key computation:\n"+err);
				}

			}


			Event midEvent = new Event(Type.DUPLICATED, null);
			Set<Event> eventsOnMidPoint = prepareForNewEvent(completeEventsMap, midKey);
			eventsOnMidPoint.add(midEvent);
			completeEventsMap.put(midKey, eventsOnMidPoint);		

		}

		return completeEventsMap;

	}


	private Set<Apfloat> getIntersectionPoints(Circle c1, Circle c2){

		Set<Apfloat> intersectionPoints = new HashSet<Apfloat>();

		Apfloat dx = c2.getX().subtract(c1.getX());
		Apfloat dy = c2.getY().subtract(c1.getY());
		
		Apfloat dSquared = ApfloatMath.pow(ApfloatMath.abs(dy), 2).add(ApfloatMath.pow(ApfloatMath.abs(dx), 2));

		Apfloat d = ApfloatMath.sqrt(dSquared);

		
		if (d.compareTo(c1.getRadius().add(c2.getRadius()))>=0){
			//no intersection
			return intersectionPoints;
		}

		if(d.compareTo(ApfloatMath.abs(c1.getRadius().subtract(c2.getRadius()))) < 0){
			//Inclusion  no events needed
			return intersectionPoints;
		}
		
		try{

		Apfloat a = c1.getSquaredRadius().subtract(c2.getSquaredRadius()).add(ApfloatMath.pow(d, 2)).divide(d.multiply(new Apfloat("2.0", Configurator.apfloatPrecision())));
		Apfloat x2 = c1.getX().add(dx.multiply(a).divide(d));
		//Apfloat y2 = c1.getY().add(dy.multiply(a).divide(d));

		Apfloat h = ApfloatMath.sqrt(c1.getSquaredRadius().subtract(ApfloatMath.pow(a, 2)));

		Apfloat rx = dy.negate().multiply(h.divide(d));// (0-dy) * (h/d);
		//Apfloat ry = dx.multiply(h.divide(d));// dx * (h/d);

		Apfloat xi1 = x2.add(rx);// + rx;
		Apfloat xi2 = x2.subtract(rx);// - rx;
		//Apfloat yi1 = y2.add(ry);// + ry;
		//Apfloat yi2 = y2.subtract(ry);// - ry;

		intersectionPoints.add(xi1);
		intersectionPoints.add(xi2);

		}catch(Exception e){
			System.out.println("error on sqrt");
		}
		
		return intersectionPoints;


	}



}
