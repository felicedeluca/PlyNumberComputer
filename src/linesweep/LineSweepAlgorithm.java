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
import circlegraph.CircleGraph;
import linesweep.Event.Type;
import utilities.ApfloatInterval;
import utilities.Configurator;
import utilities.Logger;

public class LineSweepAlgorithm {

	Set<Circle> circles;
	Set<Circle> activeCircles;
	public CircleGraph plyCircleGraph;

	public LineSweepAlgorithm(){
		this.circles = new HashSet<Circle>();
		this.activeCircles = new HashSet<Circle>();
		this.plyCircleGraph = null;
	}

	public int computePly(Set<Circle> circles){

		Logger.logln("Starting SweepLine Algorithm");

		activeCircles = new HashSet<Circle>();

		//Compute Events
		EventsMng em = new EventsMng();
		Map<Apfloat, Set<Event>> eventsMap = em.computeEvents(circles);

		//Ordered key list
		ArrayList<Apfloat> eventsX = new ArrayList<Apfloat>(eventsMap.keySet());
		Collections.sort(eventsX);

		int maxPly = 0;
		Set<ApfloatInterval> maxPlyRanges = new HashSet<ApfloatInterval>();
		Apfloat maxX = new Apfloat(0);

		Logger.loglnAlways("Events: " + eventsX.size());

		int ignoredEvents = 0;

		int i = 0;
		double lastPercentage = -1;

		Logger.logAlways("Events: ");
		for(Apfloat x : eventsX){

			//Logging
			double percentage = (i*100.0)/eventsX.size();
			double roundPercentage = Math.floor(percentage);
			if(roundPercentage%10 == 0){
				if(lastPercentage!=roundPercentage){
					lastPercentage = roundPercentage;
					Logger.logAlways(roundPercentage+"%  ");
				}
			}
			i++;

			Set<Event> events = eventsMap.get(x);
			Set<Circle> degenerateCiclesToClose = new HashSet<Circle>();

			Set<Event> openingAndClosingEventsToRemove = new HashSet<Event>();

			for (Event e : events){
				//Setup circles
				prepareForEvent(e);	
				if(e.type == Type.OPENING || e.type == Type.CLOSING){
					openingAndClosingEventsToRemove.add(e);
				}
				if(e.getType() == Type.CENTER){
					degenerateCiclesToClose.add(e.circle);
				}
			}

			//Remove Opening and Closing events, since there are DUPLICATED events
			events.removeAll(openingAndClosingEventsToRemove);
			ignoredEvents += openingAndClosingEventsToRemove.size();
			if(events.size()==0){
				Logger.logln("No need to compute intersection since there are no events");
				continue;
			}

			if(activeCircles.size()==0){ continue; }

			//Compute all Intersections
			ArrayList<ApfloatInterval> intervals = computeIntersections(x);

			int currPly = 0;

			if(Configurator.debug){
				Set<ApfloatInterval> currPlyRanges = pointOfMaximumOverlap(intervals);
				currPly = currPlyRanges.size();	
				if(currPly>maxPly){maxPlyRanges = currPlyRanges;}
			}else{
				currPly = this.numberOfMaxOverlappingIntervals(intervals);
			}

			//Check Ply
			if(currPly>maxPly){//
				Logger.logln("New Ply: " + currPly);
				maxPly = currPly;
				maxX = x;
			}

			for(Circle degenerateCircle : degenerateCiclesToClose){
				if(!activeCircles.contains(degenerateCircle))
					throw new IllegalArgumentException("Trying to close a non active degenerate circle: " + degenerateCircle.toString());
				activeCircles.remove(degenerateCircle);
			}
		}

		Logger.loglnAlways("100 %");
		Logger.loglnAlways("Ignored Events: " + ignoredEvents);

		Logger.logln("Max Ply: " + maxPly);
		Logger.logln("X Coordinate: " + maxX);

		if(Configurator.debug){
			
			Set<Circle> maxPlyCircles = new HashSet<Circle>();
			for(ApfloatInterval range : maxPlyRanges){
				maxPlyCircles.add(range.getCircle());
			}

			CircleGraph cg = new CircleGraph(circles, maxPlyCircles, maxX);
			this.plyCircleGraph = cg;
		}

		return maxPly;

	}

	private void prepareForEvent(Event e){

		switch(e.type){
		case OPENING:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activate a circle twice: " + e.circle.toString());
			activeCircles.add(e.circle);
		}
		break;
		case CLOSING:{
			if(!activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to close a non active circle: " + e.circle.toString());
			activeCircles.remove(e.circle);
		}
		break;
		case INTERSECTION:{
			Circle c1 = e.c1;
			Circle c2 = e.c2;
			if(!activeCircles.contains(c1) &&
					!activeCircles.contains(c2))
				throw new IllegalArgumentException("Intersection between non active circles: " + e.c1.toString()+ " , "+ e.c2.toString() );
		}
		case DUPLICATED:{

		}
		break;
		case CENTER:{
			if(activeCircles.contains(e.circle))
				throw new IllegalArgumentException("Trying to activate a circle twice: " + e.circle.toString());
			activeCircles.add(e.circle);
		}
		break;
		default:
			break;
		}

	}
	
	/***************************************************************************
     *  Intersections
     ***************************************************************************/

	private ArrayList<ApfloatInterval> computeIntersections(Apfloat xLine){

		ArrayList<ApfloatInterval> rangeSet = new ArrayList<ApfloatInterval>();

		for(Circle circle : this.activeCircles){


			Apfloat xCenter = circle.getX();
			Apfloat yCenter = circle.getY();
			Apfloat squaredRadius = circle.getSquaredRadius();

			if(circle.hasRadiusZero()){
				ApfloatInterval currRange = new ApfloatInterval(yCenter, yCenter, circle);
				rangeSet.add(currRange);
				continue;
			}

			//System.out.println("xLine: "+xLine+"\ncenter: (" +xCenter + ", "+ yCenter +") radius: "+ radius );

			//Apfloat a = new Apfloat("1", Apfloat.INFINITE);
			Apfloat b = yCenter.multiply(new Apfloat("2", Configurator.apfloatPrecision())).negate(); //-2yc

			Apfloat c = ApfloatMath.sum(ApfloatMath.pow(yCenter, 2),
					ApfloatMath.pow(ApfloatMath.abs(xLine.subtract(xCenter)), 2),
					squaredRadius.negate()); // yc^2+(xl-xc)^2-r^2


			Apfloat disc = ApfloatMath.sum(
					ApfloatMath.pow(b, 2),
					c.multiply(new Apfloat("4", Configurator.apfloatPrecision())).negate());

			Apfloat sqrt = ApfloatMath.sqrt(disc);

			Apfloat y1 = b.negate().subtract(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));
			Apfloat y2 = b.negate().add(sqrt).divide(new Apfloat("2", Configurator.apfloatPrecision()));

			ApfloatInterval currRange = new ApfloatInterval(y1, y2, circle);
			rangeSet.add(currRange);

		}

		return rangeSet;

	}
	
	/***************************************************************************
     *  Point Of Maximum Overlap
     ***************************************************************************/

	private Set<ApfloatInterval> pointOfMaximumOverlap(ArrayList<ApfloatInterval> intervals){

		Map<Apfloat, ArrayList<ApfloatInterval>> openingRangesMap = new HashMap<Apfloat, ArrayList<ApfloatInterval>>();
		Map<Apfloat, ArrayList<ApfloatInterval>> closingRangesMap = new HashMap<Apfloat, ArrayList<ApfloatInterval>>();
		Map<Apfloat, ArrayList<ApfloatInterval>> degenerateRangesMap = new HashMap<Apfloat, ArrayList<ApfloatInterval>>();

		Set<Apfloat> allKeysSet = new HashSet<Apfloat>();

		for (ApfloatInterval r : intervals){

			if(r.getMaximumValue() == r.getMinimumValue()){
				Apfloat key = r.getMinimumValue();
				ArrayList<ApfloatInterval> dR = degenerateRangesMap.get(key);
				if(dR == null) dR = new ArrayList<ApfloatInterval>();
				dR.add(r);
				degenerateRangesMap.put(key, dR);
				allKeysSet.add(key);

			}else{

				Apfloat minKey = r.getMinimumValue();
				Apfloat maxKey = r.getMaximumValue();

				ArrayList<ApfloatInterval> oR = openingRangesMap.get(minKey);
				if(oR == null) oR = new ArrayList<ApfloatInterval>();
				oR.add(r);
				openingRangesMap.put(minKey, oR);
				allKeysSet.add(minKey);


				ArrayList<ApfloatInterval> cR = closingRangesMap.get(maxKey);
				if(cR == null) cR = new ArrayList<ApfloatInterval>();
				cR.add(r);
				closingRangesMap.put(maxKey, cR);
				allKeysSet.add(maxKey);
			}
		}

		ArrayList<Apfloat> allKeys = new ArrayList<Apfloat>(allKeysSet);
		Collections.sort(allKeys);

		int p = -1;
		int tempP = 0;

		Set<ApfloatInterval> maxOverlappingRanges = new HashSet<ApfloatInterval>();
		Set<ApfloatInterval> openRanges = new HashSet<ApfloatInterval>();

		for(Apfloat k : allKeys){

			ArrayList<ApfloatInterval> cR = closingRangesMap.get(k);
			if(cR != null){
				if(!openRanges.containsAll(cR)) throw new IllegalArgumentException("Closing not open Range");
				openRanges.removeAll(cR);
				tempP -= cR.size();
			}

			ArrayList<ApfloatInterval> oR = openingRangesMap.get(k);
			if(oR != null){
				openRanges.addAll(oR);
				tempP +=oR.size();

			}

			ArrayList<ApfloatInterval> dR = degenerateRangesMap.get(k);
			if(dR != null){
				tempP += dR.size();
				if(tempP > p){
					p = tempP;
					maxOverlappingRanges = new HashSet<ApfloatInterval>(openRanges);
					maxOverlappingRanges.addAll(dR);
				}
				tempP -= dR.size();
			}

			if(tempP > p){
				p = tempP;
				maxOverlappingRanges = new HashSet<ApfloatInterval>(openRanges);
			}


		}


		return maxOverlappingRanges;
	}


	private int numberOfMaxOverlappingIntervals(ArrayList<ApfloatInterval> intervals){

		Map<Apfloat, Integer> overlappingIntervals = new HashMap<Apfloat, Integer>();
		Map<Apfloat, Integer> overlappingDegInt = new HashMap<Apfloat, Integer>();


		for (ApfloatInterval r : intervals){

			if(r.isDegenerate()){

				Apfloat key = r.getMinimumValue();
				int count = 0;
				if(overlappingDegInt.containsKey(key))  count = overlappingDegInt.get(key);
				count += 1;
				overlappingDegInt.put(key, count);

				if(!overlappingIntervals.containsKey(key)) overlappingIntervals.put(key, 0);

				continue;
			}

			Apfloat minKey = r.getMinimumValue();
			Apfloat maxKey = r.getMaximumValue();

			int minCount = 0;
			if(overlappingIntervals.containsKey(minKey))  minCount = overlappingIntervals.get(minKey);
			minCount += 1;
			overlappingIntervals.put(minKey, minCount);

			int maxCount = 0;
			if(overlappingIntervals.containsKey(maxKey)) maxCount = overlappingIntervals.get(maxKey);
			maxCount -= 1;
			overlappingIntervals.put(maxKey, maxCount);
		}

		ArrayList<Apfloat> allKeys = new ArrayList<Apfloat>(overlappingIntervals.keySet());
		Collections.sort(allKeys);

		int mo = -1;
		int currOverlap = 0;

		for(int i=0; i<allKeys.size(); i++){

			Apfloat currKey = allKeys.get(i);
			currOverlap += overlappingIntervals.get(currKey);

			int degInt = 0;
			if(overlappingDegInt.containsKey(currKey)) degInt = overlappingDegInt.get(currKey);

			mo = Math.max(mo, currOverlap+degInt);

		}

		return mo;

	}


	/***************************************************************************
	 *  test client
	 ***************************************************************************/
	public static void main(String[] args) {

		

	}
}
