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

					ArrayList<Apfloat> intersectionPointsInv = new ArrayList<Apfloat>(getIntersectionPoints(c2, c));



					if(intersectionPoints.size() != intersectionPointsInv.size()){

						System.out.println("wrong intersection points");

						System.out.println(c.toString());
						System.out.println(c2.toString());

						throw new IllegalArgumentException("Different Intersection Points Size");
					}

					/*
					if(intersectionPoints.size() == intersectionPointsInv.size() &&
							intersectionPoints.size() > 0){

						for(Apfloat p1 : intersectionPoints){

							boolean found = false;

							for (int pIndex=0;(pIndex<intersectionPointsInv.size() && !found); pIndex++){

								Apfloat p2 = intersectionPointsInv.get(pIndex);

								if (p1.compareTo(p2) == 0){
									found = true;
									}

							}

							if(!found){



								System.out.println("missing Intersection Point");
								System.out.println(c.toString());
								System.out.println(c2.toString());

								System.out.println("Set 1");
								System.out.println(p1.precision() + " value: " + p1.toString(true));
								System.out.println("Set 2");
								System.out.println(intersectionPointsInv.toString());							}


						}


					}

					 */

					if(intersectionPoints.size()>1){

						for(Apfloat xi : intersectionPoints){

							Event iEvent = new Event(Type.INTERSECTION, c, c2);
							Set<Event> eventsOnIntersectionPoint = prepareForNewEvent(eventsMap, xi);
							eventsOnIntersectionPoint.add(iEvent);
							eventsMap.put(xi, eventsOnIntersectionPoint);

						}
					}

					if(intersectionPointsInv.size()>1){

						for(Apfloat xi : intersectionPointsInv){

							Event iEvent = new Event(Type.INTERSECTION, c2, c);
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


	private int doIntersect(Circle c0, Circle c1){

		Apfloat dx = c1.getX().subtract(c0.getX());
		Apfloat dy = c1.getY().subtract(c0.getY());

		Apfloat r0Squared = c0.getSquaredRadius();
		Apfloat r1Squared = c1.getSquaredRadius();


		Apfloat dSquared = ApfloatMath.sum(ApfloatMath.pow(ApfloatMath.abs(dy), 2), ApfloatMath.pow(ApfloatMath.abs(dx), 2));

		Apfloat alphaSquared = r0Squared.divide(r1Squared);
		Apfloat alpha = ApfloatMath.sqrt(alphaSquared);

		if(c0.getSquaredRadius().compareTo(c1.getSquaredRadius()) == 0){
			alphaSquared = Configurator.one;
			alpha = Configurator.one;
		}

		Apfloat prop = ApfloatMath.sum(alphaSquared, alpha.multiply(Configurator.two), Configurator.one);

		Apfloat propTimesRadius = prop.multiply(r1Squared);
		
		Apfloat diff = dSquared.subtract(propTimesRadius);
		
		int compare = diff.compareTo(Configurator.zero);
				
		return compare;
	}


	private Set<Apfloat> getIntersectionPoints(Circle c0, Circle c1){

		Set<Apfloat> intersectionPoints = new HashSet<Apfloat>();
		
		Apfloat dx = c1.getX().subtract(c0.getX());
		Apfloat dy = c1.getY().subtract(c0.getY());

		Apfloat r0Squared = c0.getSquaredRadius();
		Apfloat r1Squared = c1.getSquaredRadius();

		Apfloat dSquared = ApfloatMath.pow(ApfloatMath.abs(dy), 2).add(ApfloatMath.pow(ApfloatMath.abs(dx), 2));
		Apfloat d = ApfloatMath.sqrt(dSquared);

		int doIntersect_new = doIntersect(c0, c1);

		Apfloat radiiSum = ApfloatMath.sum(c0.getRadius(), c1.getRadius());
		int doIntersect_Old = d.subtract(radiiSum).compareTo(Configurator.zero);


			if(doIntersect_new != doIntersect_Old){
				
				System.out.println("Different intersection Test");


				switch(doIntersect_new){
				case -1 :{
					System.out.println("new says: INTERSECT");
				}
				break;
				case 0 :{
					System.out.println("new says: TANGENT");
				}
				break;
					case +1 :{
							System.out.println("new says: DO NOT INTERSECT");
				}
				break;
				}
				
				switch(doIntersect_Old){
				case -1 :{
					System.out.println("old says: INTERSECT");
				}
				break;
				case 0 :{
					System.out.println("old says: TANGENT");
				}
				break;
					case +1 :{
							System.out.println("old says: DO NOT INTERSECT");
				}
				break;
				}

				System.out.println("c0: " + c0.toEquation());
				System.out.println("c1: " + c1.toEquation());
				System.out.println("_________");

			}



	

		if(r0Squared.compareTo(r1Squared)==0){

			Apfloat radiusRatio = Configurator.getInstance().getRadiusRatio();

			Apfloat half = new Apfloat("0.5", Configurator.apfloatPrecision());

			if(half.compareTo(radiusRatio)==0)
			{
				Apfloat fourRadius = r0Squared.multiply(new Apfloat("4", Configurator.apfloatPrecision()));

				if(fourRadius.compareTo(dSquared)==0){

					return intersectionPoints;

				}

			}
		}
		

		if (d.compareTo(c0.getRadius().add(c1.getRadius()))>=0){
			//no intersection
			return intersectionPoints;
		}

		if(d.compareTo(ApfloatMath.abs(c0.getRadius().subtract(c1.getRadius()))) < 0){
			//Inclusion  no events needed
			return intersectionPoints;
		}



		Apfloat x0 = c0.getX();



		Apfloat two = new Apfloat("2.0", Configurator.apfloatPrecision());
		Apfloat four = new Apfloat("4.0", Configurator.apfloatPrecision());


		Apfloat a = (r0Squared.subtract(r1Squared).add(dSquared)).divide(dSquared.multiply(two));
		Apfloat adx = a.multiply(dx);

		Apfloat hSquared = null;

		try{

			hSquared = (four.multiply(r0Squared).multiply(dSquared)).subtract(ApfloatMath.pow(ApfloatMath.abs((r0Squared.subtract(r1Squared).add(dSquared))), two));



			Apfloat h = ApfloatMath.sqrt(hSquared).divide(two.multiply(dSquared));
			Apfloat hdy = h.multiply(dy);

			Apfloat xi1 = x0.add(adx).add(hdy);
			Apfloat xi2 = x0.add(adx).subtract(hdy);


			/*

			//TODO: could ApfloatMath.pow(d, 2) be removed?
		Apfloat a = c0.getSquaredRadius().subtract(c1.getSquaredRadius()).add(ApfloatMath.pow(d, 2)).divide(d.multiply(new Apfloat("2.0", Configurator.apfloatPrecision())));
		Apfloat x2 = c0.getX().add(dx.multiply(a).divide(d));
		//Apfloat y2 = c1.getY().add(dy.multiply(a).divide(d));

		Apfloat h = ApfloatMath.sqrt(c0.getSquaredRadius().subtract(ApfloatMath.pow(a, 2)));

		Apfloat rx = dy.negate().multiply(h.divide(d));// (0-dy) * (h/d);
		//Apfloat ry = dx.multiply(h.divide(d));// dx * (h/d);

		Apfloat xi1 = x2.add(rx);// + rx;
		Apfloat xi2 = x2.subtract(rx);// - rx;
		//Apfloat yi1 = y2.add(ry);// + ry;
		//Apfloat yi2 = y2.subtract(ry);// - ry;

			 */

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
