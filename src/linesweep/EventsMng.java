package linesweep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import circlegraph.Circle;
import linesweep.Event.Type;
import utilities.PlyConfigurator;
import utilities.PlyLogger;

public class EventsMng {

	ArrayList<Event> orderdEvents;
	
	/**
	 * Computes all events i.e. Opening, Closing and Intersection events.
	 * In addiction computes 'middle events' that are events between two consecutive events.
	 * @param circles Circles on which compute the Events
	 * @return a Map where the key are the X-coordinate of the events and the value is a set of Events
	 */
	public Map<Apfloat, Set<Event>> computeEvents(Set<Circle> circles){

		PlyLogger.logln("Computing Events");

		Map<Apfloat, Set<Event>> eventsMap = new HashMap<Apfloat, Set<Event>>();

		ArrayList<Circle> circlesArrList = new ArrayList<Circle>(circles);
		
		Collections.sort(circlesArrList, new Comparator<Circle>(){
		    public int compare(Circle c1, Circle c2) {
		        return c1.getLeftmostX().compareTo(c2.getLeftmostX());
		    }
		});
		
		
		double lastPercentage = 0.0;
		
		PlyLogger.log("Intersections:");
		
		for(int i=0; i<circlesArrList.size(); i++){

			PlyLogger.logln("Circle: " + i);

			//Logging
			double percentage = (i*100.0)/circlesArrList.size();
			double roundPercentage = Math.floor(percentage);
			if(roundPercentage%10 == 0){
				if(lastPercentage!=roundPercentage){
					lastPercentage = roundPercentage;
					PlyLogger.log(roundPercentage+"%  ");
				}
			}

			Circle c = circlesArrList.get(i);
	

			if(c.hasRadiusZero()){

				Apfloat centerX = c.getX();
				Event centerEvent = new Event(Type.CENTER, c);
				Set<Event> centerEventsOnPoint = prepareForNewEvent(eventsMap, centerX);
				centerEventsOnPoint.add(centerEvent);
				eventsMap.put(centerX, centerEventsOnPoint);
				continue;
			}

			Apfloat startingX = c.getLeftmostX();
			Apfloat endingX = c.getRightmostX();

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

				if(c.equals(c2)){continue;}
				if(c2.hasRadiusZero()){continue;}
				
				
				Apfloat c2_LeftmostX = c2.getLeftmostX();
				if(endingX.compareTo(c2_LeftmostX)==-1){
					//Logger.log("last j: " + j);
					break;
				}

				if(toIgnore(c, c2)){continue;}

				Set<Apfloat> intersectionPoints = new HashSet<Apfloat>();
				intersectionPoints.addAll(getIntersectionPoints(c, c2));
				intersectionPoints.addAll(getIntersectionPoints(c2, c));

				for(Apfloat xi : intersectionPoints){

					Event iEvent = new Event(Type.INTERSECTION, c, c2);
					Set<Event> eventsOnIntersectionPoint = prepareForNewEvent(eventsMap, xi);
					eventsOnIntersectionPoint.add(iEvent);
					eventsMap.put(xi, eventsOnIntersectionPoint);
				}
			}

		}

		PlyLogger.logln("100 %");
		
		PlyLogger.logln("Computed Real Events: " + eventsMap.size());

		Map<Apfloat, Set<Event>> completeEventsMap =  addMidEvents(eventsMap);

		PlyLogger.logln("Total Events: " + completeEventsMap.size());


		return completeEventsMap;

	}

	//Lazy
	private Set<Event> prepareForNewEvent(Map<Apfloat, Set<Event>> map, Apfloat x){

		Set<Event> eventsOnX = map.get(x);
		if(eventsOnX == null){
			eventsOnX = new HashSet<Event>();
		}
		return eventsOnX;
	}

	/**
	 * Add new events between two consecutive computed events.
	 * This reduces the error probability.
	 * @param map
	 * @return
	 */
	private Map<Apfloat, Set<Event>>  addMidEvents(Map<Apfloat, Set<Event>> map){

		PlyLogger.logln("Computing Fake Events");

		Map<Apfloat, Set<Event>> completeEventsMap = new HashMap<Apfloat, Set<Event>>(map);

		ArrayList<Apfloat> keys = new ArrayList<Apfloat>(completeEventsMap.keySet());
		Collections.sort(keys);

		for(int i=0; i<keys.size()-1; i++){

			Apfloat currKey = keys.get(i);
			Apfloat nextKey = keys.get(i+1);

			Apfloat midKey = currKey;
			
		
			if(currKey.compareTo(nextKey)!=0){
				
				//Check if there is a precisione isse
				Set<Event> currEvents = completeEventsMap.get(currKey);
				Set<Event> nextEvents = completeEventsMap.get(nextKey);
				
				if(currEvents.size() == 1 && nextEvents.size() == 1){
					
					Event currEvent = (Event) currEvents.toArray()[0];
					Event nextEvent = (Event) nextEvents.toArray()[0];
					
					if(currEvent.type == Type.OPENING &&
							nextEvent.type == Type.CLOSING){
						
						Circle c1 = currEvent.circle;
						Circle c2 = nextEvent.circle;
						
						if(c1.getRelativeEdge().equals(c2.getRelativeEdge())){
							continue; //do not create mid Event -> precision issue
						}
					}
				}

				currKey = new Apfloat(keys.get(i).toString(), PlyConfigurator.apfloatPrecision()+1);
				nextKey = new Apfloat(keys.get(i+1).toString(), PlyConfigurator.apfloatPrecision()+1);

				Apfloat sum = currKey.add(nextKey);
				midKey = sum.divide(new Apfloat("2", currKey.precision()+1));

				if(!((currKey.compareTo(midKey)<=0 && nextKey.compareTo(midKey)==1) ||
						(currKey.compareTo(nextKey)==0 && currKey.compareTo(nextKey)==0))){

					String err = "MidValue:\n"
							+ "1: " + currKey.toString(true) + "\n"
							+ "2: "+ nextKey.toString(true) +"\n"
							+ "== " + midKey.toString(true);
					
				//System.out.println(err);
					midKey = currKey;
					//throw new IllegalArgumentException("Wrong mid key computation:\n"+err);
				}

			}


			Event midEvent = new Event(Type.DUPLICATED, null);
			Set<Event> eventsOnMidPoint = prepareForNewEvent(completeEventsMap, midKey);
			eventsOnMidPoint.add(midEvent);
			completeEventsMap.put(midKey, eventsOnMidPoint);		

		}

		return completeEventsMap;

	}

	/**
	 * Checks if Circles <tt>c0</tt> and <tt>c1</tt> intersect or are tangent
	 * @param c0 first Circle
	 * @param c1 second Circle
	 * @return true if circles don't touch or are tangent, false otherwise
	 */
	private boolean toIgnore(Circle c0, Circle c1){

		if(c0.getRelativeEdge() != null && c1.getRelativeEdge() != null){
			if (c0.getRelativeEdge().equals(c1.getRelativeEdge())){
				return true;
			}
		}
		
		Apfloat dx = c1.getX().subtract(c0.getX());
		Apfloat dy = c1.getY().subtract(c0.getY());

		Apfloat r0Squared = c0.getSquaredRadius();
		Apfloat r1Squared = c1.getSquaredRadius();


		Apfloat dSquared = ApfloatMath.sum(ApfloatMath.pow(ApfloatMath.abs(dy), 2), ApfloatMath.pow(ApfloatMath.abs(dx), 2));

		Apfloat alphaSquared = r0Squared.divide(r1Squared);
		Apfloat alpha = ApfloatMath.sqrt(alphaSquared);

		if(c0.getSquaredRadius().compareTo(c1.getSquaredRadius()) == 0){
			alphaSquared = PlyConfigurator.one;
			alpha = PlyConfigurator.one;
		}

		Apfloat propPerIntersection = ApfloatMath.sum(alphaSquared, alpha.multiply(PlyConfigurator.two), PlyConfigurator.one);
		Apfloat propTimesRadiusPerIntersection = propPerIntersection.multiply(r1Squared);
		boolean doIntersect = (dSquared.compareTo(propTimesRadiusPerIntersection) == -1);

		Apfloat propInclusion = ApfloatMath.sum(alphaSquared, alpha.multiply(PlyConfigurator.two).negate(), PlyConfigurator.one);
		Apfloat propTimesRadiusInclusion = propInclusion.multiply(r1Squared);
		boolean noInclusion = (dSquared.compareTo(propTimesRadiusInclusion) == 1);

		boolean toIgnore = ((doIntersect && !noInclusion) || !doIntersect || !noInclusion);

		return toIgnore;
	}


	private Set<Apfloat> getIntersectionPoints(Circle c0, Circle c1){

		Set<Apfloat> intersectionPoints = new HashSet<Apfloat>();

		Apfloat dx = c1.getX().subtract(c0.getX());
		Apfloat dy = c1.getY().subtract(c0.getY());

		Apfloat r0Squared = c0.getSquaredRadius();
		Apfloat r1Squared = c1.getSquaredRadius();

		Apfloat dSquared = ApfloatMath.pow(ApfloatMath.abs(dy), 2).add(ApfloatMath.pow(ApfloatMath.abs(dx), 2));

		Apfloat x0 = c0.getX();

		Apfloat two = PlyConfigurator.two;
		Apfloat four = PlyConfigurator.four;


		Apfloat a = (r0Squared.subtract(r1Squared).add(dSquared)).divide(dSquared.multiply(two));
		Apfloat adx = a.multiply(dx);

		Apfloat hSquared = null;

		try{

			hSquared = (four.multiply(r0Squared).multiply(dSquared)).subtract(ApfloatMath.pow(ApfloatMath.abs((r0Squared.subtract(r1Squared).add(dSquared))), two));

			Apfloat h = ApfloatMath.sqrt(hSquared).divide(two.multiply(dSquared));
			Apfloat hdy = h.multiply(dy);

			Apfloat xi1 = x0.add(adx).add(hdy);
			Apfloat xi2 = x0.add(adx).subtract(hdy);


			if(hdy.compareTo(new Apfloat("0"))==0){
				//Tangent
				return intersectionPoints;
			}

			intersectionPoints.add(xi1);
			intersectionPoints.add(xi2);

		}catch(Exception e){
			
			System.out.println("error on sqrt");
			System.out.println(c0.toString());
			System.out.println(c1.toString());
			System.out.println(dx.toString(true));
			System.out.println(dy.toString(true));
			System.out.println("d^2: "+dSquared.toString(true));
			System.out.println("h^2: "+hSquared.toString(true));

		}


		return intersectionPoints;


	}



}
